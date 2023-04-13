package pt.bosch.heatingmonitor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.bosch.heatingmonitor.exceptions.NotificationException;
import pt.bosch.heatingmonitor.model.NotificationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final Map<String, Sinks.Many<NotificationResponse>> sinkMap;

    @Override
    public void subscriber(String subscriptionId) {
        sinkMap.put(subscriptionId, Sinks.many().multicast().onBackpressureBuffer());
    }

    @Override
    public void unsubscribe(String subscriptionId) {
        sinkMap.remove(subscriptionId);
    }

    @Override
    public void emitMessage(String subscriptionId, NotificationResponse message) {
        Mono.just(getSink(subscriptionId).tryEmitNext(message))
                .filter(Sinks.EmitResult::isFailure)
                .switchIfEmpty(Mono.error(new NotificationException("Error emitting message.")));
    }

    private Sinks.Many<NotificationResponse> getSink(String subscriptionId) {
        if (!sinkMap.containsKey(subscriptionId))
            throw new NotificationException("Subscription not found.");

        return sinkMap.get(subscriptionId);

    }

    public Flux<NotificationResponse> getMessage(String subscriptionId) {
        return sinkMap.get(subscriptionId).asFlux();
    }

}
