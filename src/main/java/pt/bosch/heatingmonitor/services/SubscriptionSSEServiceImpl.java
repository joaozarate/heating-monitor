package pt.bosch.heatingmonitor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.bosch.heatingmonitor.model.NotificationResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class SubscriptionSSEServiceImpl implements SubscriptionSSEService {

    private final Sinks.Many<NotificationResponse> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public void emitMessage(NotificationResponse message) {
        sink.tryEmitNext(message);
    }

    public Flux<NotificationResponse> getMessage() {
        return sink.asFlux();
    }

}
