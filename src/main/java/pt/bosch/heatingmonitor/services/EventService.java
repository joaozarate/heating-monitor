package pt.bosch.heatingmonitor.services;

import pt.bosch.heatingmonitor.model.NotificationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

public interface EventService {

    void subscriber(String subscriptionId);

    void unsubscribe(String subscriptionId);

    void emitMessage(String subscriptionId, NotificationResponse message);

    Flux<NotificationResponse> getMessage(String subscriptionId);

}
