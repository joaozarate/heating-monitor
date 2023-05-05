package pt.bosch.heatingmonitor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.bosch.heatingmonitor.mappers.SubscriptionMapper;
import pt.bosch.heatingmonitor.model.SubscriptionRequest;
import pt.bosch.heatingmonitor.model.SubscriptionResponse;
import pt.bosch.heatingmonitor.repository.SubscriptionRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionMapper mapper;
    private final SubscriptionRepository repository;
    private final EventService eventService;

    @Override
    public Mono<SubscriptionResponse> saveSubscription(Mono<SubscriptionRequest> dto) {
        return dto.map(mapper::dtoToDomain)
                .flatMap(entity -> {
                    entity.setActive(true);
                    return repository.save(entity)
                            .doOnSuccess(subscription -> eventService.subscriber(subscription.getId().toString()));
                })
                .map(mapper::domainToDto);
    }

    @Override
    public Mono<SubscriptionResponse> findById(UUID subscriptionId) {
        return repository.findById(subscriptionId)
                .map(mapper::domainToDto);
    }

    @Override
    public Mono<SubscriptionResponse> deactivate(String subscriptionId) {
        return repository.findById(UUID.fromString(subscriptionId))
                .flatMap(entity -> {
                    entity.setActive(false);
                    return repository.save(entity)
                            .doOnSuccess(subscription -> eventService.unsubscribe(subscription.getId().toString()));
                })
                .map(mapper::domainToDto);
    }
}
