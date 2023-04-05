package pt.bosch.heatingmonitor.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import pt.bosch.heatingmonitor.model.NotificationDTO;
import pt.bosch.heatingmonitor.services.NotificationService;
import reactor.core.publisher.Mono;

import static pt.bosch.heatingmonitor.web.fn.NotificationRouterConfig.NOTIFICATION_PATH_ID;

@Component
@RequiredArgsConstructor
public class NotificationHandler {

    private final NotificationService service;

    public Mono<ServerResponse> notify(ServerRequest request) {
        return service.notify(request.bodyToMono(NotificationDTO.class))
                .flatMap(
                        dto -> ServerResponse.created(
                                UriComponentsBuilder.fromPath("http://localhost:8080/" + NOTIFICATION_PATH_ID).build(dto.getId())
                        ).build()
                );
    }

}
