package pt.bosch.heatingmonitor.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import pt.bosch.heatingmonitor.services.SubscriptionService;
import pt.bosch.heatingmonitor.model.SubscriptionDTO;
import reactor.core.publisher.Flux;
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

    public Mono<ServerResponse> listSubscriptions(ServerRequest request) {
        Flux<SubscriptionDTO> flux = service.findByActive("Y");
        return ServerResponse.ok().body(flux, SubscriptionDTO.class);
    }

    public Mono<ServerResponse> activate(ServerRequest request) {
        return service.activate(request.pathVariable("subscriptionId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deactivate(ServerRequest request) {
        return service.deactivate(request.pathVariable("subscriptionId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .then(ServerResponse.noContent().build());
    }

}
