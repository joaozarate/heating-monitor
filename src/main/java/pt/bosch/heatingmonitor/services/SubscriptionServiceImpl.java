package pt.bosch.heatingmonitor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.bosch.heatingmonitor.mappers.SubscriptionMapper;
import pt.bosch.heatingmonitor.model.SubscriptionDTO;
import pt.bosch.heatingmonitor.repository.SubscriptionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionMapper mapper;
    private final SubscriptionRepository repository;

    @Override
    public Mono<SubscriptionDTO> saveSubscription(Mono<SubscriptionDTO> dto) {
        return dto.map(mapper::dtoToDomain)
                .flatMap(entity -> {
                    entity.setEvent("event-name");
                    return repository.save(entity);
                })
                .map(mapper::domainToDto);
    }

    @Override
    public Flux<SubscriptionDTO> findByActive(String active) {
        return repository.findByActive(active).map(mapper::domainToDto);
    }

    @Override
    public Mono<SubscriptionDTO> activate(String subscriptionId) {
        return repository.findById(UUID.fromString(subscriptionId))
                .flatMap(entity -> {
                    entity.setActive("Y");
                    return repository.save(entity);
                })
                .map(mapper::domainToDto);
    }

    @Override
    public Mono<SubscriptionDTO> deactivate(String subscriptionId) {
        return repository.findById(UUID.fromString(subscriptionId))
                .flatMap(entity -> {
                    entity.setActive("N");
                    return repository.save(entity);
                })
                .map(mapper::domainToDto);
    }
}
