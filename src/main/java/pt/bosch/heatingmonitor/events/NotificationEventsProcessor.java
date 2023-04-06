package pt.bosch.heatingmonitor.events;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pt.bosch.heatingmonitor.model.NotificationDTO;
import pt.bosch.heatingmonitor.repository.SubscriptionRepository;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NotificationEventsProcessor {

    private final SubscriptionRepository subscriptionRepository;

    @EventListener
    public Mono<NotificationDTO> handleNotificationEvent(NotificationEvent event) {

        System.out.println(event);

        return subscriptionRepository.findById(event.getDto().getSubscriptionId())
                .flatMap(entity -> WebClient.create(entity.getBaseReceiverUrl())
                        .post()
                        .uri(entity.getRelativeReceiverUrl())
                        .body(Mono.just(event.getDto()), NotificationDTO.class)
                        .retrieve().bodyToMono(NotificationDTO.class));
    }

}
