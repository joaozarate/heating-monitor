package pt.bosch.heatingmonitor.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import pt.bosch.heatingmonitor.services.SubscriptionService;
import pt.bosch.heatingmonitor.model.SubscriptionDTO;
import reactor.core.publisher.Mono;

import static pt.bosch.heatingmonitor.web.fn.SubscriptionRouterConfig.SUBSCRIPTION_PATH_ID;

@Component
@RequiredArgsConstructor
public class SubscriptionHandler {

    private final SubscriptionService service;

    public Mono<ServerResponse> subscribe(ServerRequest request) {
        return service.saveSubscription(request.bodyToMono(SubscriptionDTO.class))
                .flatMap(
                        dto -> ServerResponse.created(
                                UriComponentsBuilder.fromPath("http://localhost:8080/" + SUBSCRIPTION_PATH_ID).build(dto.getId())
                        ).build()
                );
    }

}
