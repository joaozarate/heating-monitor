package pt.bosch.heatingmonitor.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import pt.bosch.heatingmonitor.exceptions.NotificationException;
import pt.bosch.heatingmonitor.model.NotificationDTO;
import pt.bosch.heatingmonitor.repository.SubscriptionRepository;
import pt.bosch.heatingmonitor.services.NotificationService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NotificationEventsProcessor {

    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;

    @EventListener
    public Mono<NotificationDTO> handleNotificationEvent(NotificationEvent event) {
        return subscriptionRepository.findById(event.getDto().getSubscriptionId())
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.printf("Subscription %s not found.%n", event.getDto().getSubscriptionId());
                    return Mono.empty();
                }))
                .flatMap(entity -> WebClient.create(entity.getBaseReceiverUrl())
                        .post()
                        .uri(entity.getRelativeReceiverUrl())
                        .body(Mono.just(event.getDto()), NotificationDTO.class)
                        .retrieve()

                        .onStatus(HttpStatusCode::is4xxClientError, response -> {
                            notificationService.updateStatus(Mono.just(event.getDto().getSubscriptionId()), "F");
                            return Mono.error(new NotificationException("A custom message saying what gets wrong from the client side."));
                        })
                        .onStatus(HttpStatusCode::is5xxServerError, response -> {
                            notificationService.updateStatus(Mono.just(event.getDto().getSubscriptionId()), "F");
                            return Mono.error(new NotificationException("A custom message saying what gets wrong from the server side."));
                        })

                        .bodyToMono(NotificationDTO.class)

                        .doOnSuccess(test -> {
                            notificationService.updateStatus(Mono.just(event.getDto().getSubscriptionId()), "S");
                        }).onErrorResume(WebClientRequestException.class, e -> {
                            notificationService.updateStatus(Mono.just(event.getDto().getSubscriptionId()), "F");
                            return Mono.error(new NotificationException("An unexpected error occurred while notifying.", e));
                        })
                );
    }

}
