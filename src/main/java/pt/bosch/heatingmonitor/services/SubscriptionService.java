package pt.bosch.heatingmonitor.services;

import pt.bosch.heatingmonitor.model.SubscriptionRequest;
import pt.bosch.heatingmonitor.model.SubscriptionResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SubscriptionService {

    Mono<SubscriptionResponse> saveSubscription(Mono<SubscriptionRequest> dto);

    Mono<SubscriptionResponse> findById(UUID subscriptionId);

    Flux<SubscriptionResponse> findByActive(Boolean active);

    Mono<SubscriptionResponse> activate(String subscriptionId);

    Mono<SubscriptionResponse> deactivate(String subscriptionId);
}
