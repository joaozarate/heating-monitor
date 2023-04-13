package pt.bosch.heatingmonitor.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import pt.bosch.heatingmonitor.exceptions.Error;
import pt.bosch.heatingmonitor.exceptions.NotificationException;
import pt.bosch.heatingmonitor.model.NotificationRequest;
import pt.bosch.heatingmonitor.services.NotificationService;
import reactor.core.publisher.Mono;

import static pt.bosch.heatingmonitor.web.fn.NotificationRouterConfig.NOTIFICATION_PATH_ID;

@Component
@RequiredArgsConstructor
public class NotificationHandler {

    private final NotificationService service;
    private final Validator validator;

    public Mono<ServerResponse> notify(ServerRequest request) {
        return request.bodyToMono(NotificationRequest.class)
                .doOnNext(this::validate)
                .flatMap(dto -> service.notify(Mono.just(dto))
                        .flatMap(entity ->
                                ServerResponse.created(
                                        UriComponentsBuilder.fromPath("http://localhost:8080/" + NOTIFICATION_PATH_ID).build(entity.getId())
                                ).bodyValue(entity)
                        )
                )
                .onErrorResume(ServerWebInputException.class, e -> ServerResponse.badRequest().bodyValue(Error.builder().code("E-123").message(e.getReason()).build()))
                .onErrorResume(NotificationException.class, e -> ServerResponse.badRequest().bodyValue(Error.builder().code("E-123").message(e.getMessage()).build()))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(Error.builder().code("E-999").message("Unexpected error. Please contact support.").build()));
    }

    private void validate(NotificationRequest request) {
        Errors errors = new BeanPropertyBindingResult(request, "notificationRequest");
        validator.validate(request, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException("Payload is not valid.");
        }
    }

}
