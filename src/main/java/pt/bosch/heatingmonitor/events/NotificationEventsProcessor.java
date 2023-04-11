package pt.bosch.heatingmonitor.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import pt.bosch.heatingmonitor.exceptions.NotificationException;
import pt.bosch.heatingmonitor.model.NotificationResponse;
import pt.bosch.heatingmonitor.repository.SubscriptionRepository;
import pt.bosch.heatingmonitor.services.NotificationService;
import pt.bosch.heatingmonitor.services.SubscriptionSSEService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NotificationEventsProcessor {

    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;

    @EventListener
    public Mono<NotificationResponse> handleNotificationEvent(NotificationEvent event) {
        return subscriptionRepository.findById(event.getNotification().getSubscription())
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.printf("Subscription %s not found.%n", event.getNotification().getSubscription());
                    return Mono.empty();
                }))
                .flatMap(entity -> WebClient.create(entity.getBaseReceiverUrl())
                        .post()
                        .uri(entity.getRelativeReceiverUrl())
                        .body(Mono.just(event.getNotification()), NotificationResponse.class)
                        .retrieve()

                        .onStatus(HttpStatusCode::is4xxClientError, response -> {
                            notificationService.updateStatus(Mono.just(event.getNotification().getSubscription()), "F");
                            return Mono.error(new NotificationException("A custom message saying what gets wrong from the client side."));
                        })
                        .onStatus(HttpStatusCode::is5xxServerError, response -> {
                            notificationService.updateStatus(Mono.just(event.getNotification().getSubscription()), "F");
                            return Mono.error(new NotificationException("A custom message saying what gets wrong from the server side."));
                        })

                        .bodyToMono(NotificationResponse.class)

                        .doOnSuccess(test -> {
                            notificationService.updateStatus(Mono.just(event.getNotification().getSubscription()), "S");
                        }).onErrorResume(WebClientRequestException.class, e -> {
                            notificationService.updateStatus(Mono.just(event.getNotification().getSubscription()), "F");
                            return Mono.error(new NotificationException("An unexpected error occurred while notifying.", e));
                        })
                );
    }

}
