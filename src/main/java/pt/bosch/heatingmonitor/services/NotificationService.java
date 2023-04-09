package pt.bosch.heatingmonitor.services;

import pt.bosch.heatingmonitor.domain.Notification;
import pt.bosch.heatingmonitor.model.NotificationRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NotificationService {
    Mono<Notification> notify(Mono<NotificationRequest> dto);

    void updateStatus(Mono<UUID> subscriptionId, String status);
}
