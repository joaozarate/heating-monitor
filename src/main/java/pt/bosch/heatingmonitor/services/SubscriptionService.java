package pt.bosch.heatingmonitor.services;

import pt.bosch.heatingmonitor.model.SubscriptionDTO;
import reactor.core.publisher.Mono;

public interface SubscriptionService {

    Mono<SubscriptionDTO> saveSubscription(Mono<SubscriptionDTO> dto);

}
