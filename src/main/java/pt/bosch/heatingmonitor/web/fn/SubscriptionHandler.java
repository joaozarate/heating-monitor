package pt.bosch.heatingmonitor.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import pt.bosch.heatingmonitor.exceptions.Error;
import pt.bosch.heatingmonitor.exceptions.NotificationException;
import pt.bosch.heatingmonitor.model.NotificationResponse;
import pt.bosch.heatingmonitor.model.SubscriptionRequest;
import pt.bosch.heatingmonitor.services.EventService;
import pt.bosch.heatingmonitor.services.SubscriptionService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static pt.bosch.heatingmonitor.web.fn.SubscriptionRouterConfig.SUBSCRIPTION_PATH_ID;

@Component
@RequiredArgsConstructor
public class SubscriptionHandler {

    private final SubscriptionService service;
    private final EventService eventService;
    private final Validator validator;

    public Mono<ServerResponse> subscribe(ServerRequest request) {
        return request.bodyToMono(SubscriptionRequest.class)
                .doOnNext(this::validate)
                .flatMap(dto -> service.saveSubscription(Mono.just(dto))
                        .flatMap(entity ->
                                ServerResponse.created(
                                        UriComponentsBuilder.fromPath("http://localhost:8080/" + SUBSCRIPTION_PATH_ID).build(entity.getId())
                                ).bodyValue(entity)
                        )
                )
                .onErrorResume(ServerWebInputException.class, e -> ServerResponse.badRequest().bodyValue(Error.builder().code("E-123").message(e.getReason()).build()))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(Error.builder().code("E-999").message("Unexpected error. Please contact support.").build()));
    }

    public Mono<ServerResponse> getSubscriptionById(ServerRequest request) {

        String param = request.pathVariable("subscriptionId");
        UUID subscriptionId;
        try {
            subscriptionId = UUID.fromString(param);
        } catch (IllegalArgumentException e) {
            return ServerResponse.badRequest().bodyValue(Error.builder().code("E-123").message("Invalid subscriptionId").build());
        }

        return service.findById(subscriptionId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(dto -> ServerResponse.ok().bodyValue(dto))
                .onErrorResume(ResponseStatusException.class, e -> ServerResponse.notFound().build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(Error.builder().code("E-999").message("Unexpected error. Please contact support.").build()));
    }

    public Mono<ServerResponse> deactivate(ServerRequest request) {
        return service.deactivate(request.pathVariable("subscriptionId"))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .then(ServerResponse.noContent().build())
                .onErrorResume(ResponseStatusException.class, e -> ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listen(ServerRequest request) {

        Flux<NotificationResponse> notification;

        try {
            notification = eventService.getMessage(request.pathVariable("subscriptionId"));
        } catch (NotificationException e) {
            return ServerResponse.notFound().build();
        }

        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(notification, NotificationResponse.class);

    }

    private void validate(SubscriptionRequest dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "subscriptionRequest");
        validator.validate(dto, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException("Payload is not valid.");
        }
    }
}
