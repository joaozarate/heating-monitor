package pt.bosch.heatingmonitor.services;

import org.springframework.web.reactive.function.server.ServerResponse;
import pt.bosch.heatingmonitor.model.NotificationDTO;
import reactor.core.publisher.Mono;

public interface NotificationService {
    Mono<NotificationDTO> notify(Mono<NotificationDTO> dto);
}
