package pt.bosch.heatingmonitor.services;

import pt.bosch.heatingmonitor.model.SubscriptionDTO;
import pt.bosch.heatingmonitor.model.SubscriptionRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubscriptionService {

    Mono<SubscriptionDTO> saveSubscription(Mono<SubscriptionRequest> dto);

    Flux<SubscriptionDTO> findByActive(String active);

    Mono<SubscriptionDTO> activate(String subscriptionId);

    Mono<SubscriptionDTO> deactivate(String subscriptionId);
}
