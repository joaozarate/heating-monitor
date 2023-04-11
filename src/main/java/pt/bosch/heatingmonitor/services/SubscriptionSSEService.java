package pt.bosch.heatingmonitor.services;

import pt.bosch.heatingmonitor.model.NotificationResponse;
import reactor.core.publisher.Flux;

public interface SubscriptionSSEService {

    void emitMessage(NotificationResponse message);

    Flux<NotificationResponse> getMessage();

}
