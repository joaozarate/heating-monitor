package pt.bosch.heatingmonitor.services;

import org.springframework.web.reactive.function.server.ServerResponse;
import pt.bosch.heatingmonitor.model.NotificationDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface NotificationService {
    Mono<NotificationDTO> notify(Mono<NotificationDTO> dto);

    void updateStatus(Mono<UUID> subscriptionId, String status);
}
