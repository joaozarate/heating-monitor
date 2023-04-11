package pt.bosch.heatingmonitor.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pt.bosch.heatingmonitor.model.NotificationResponse;
import pt.bosch.heatingmonitor.services.SubscriptionSSEService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SubscriptionSSEHandler {

    private final SubscriptionSSEService service;

    public Mono<ServerResponse> subscribe(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(org.springframework.http.MediaType.TEXT_EVENT_STREAM)
                .body(service.getMessage(), NotificationResponse.class);
    }

}
