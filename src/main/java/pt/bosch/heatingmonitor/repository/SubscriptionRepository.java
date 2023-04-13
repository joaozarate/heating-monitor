package pt.bosch.heatingmonitor.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pt.bosch.heatingmonitor.domain.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SubscriptionRepository extends ReactiveCrudRepository<Subscription, UUID> {}
