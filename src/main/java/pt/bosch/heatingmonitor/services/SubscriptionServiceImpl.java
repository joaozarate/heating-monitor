package pt.bosch.heatingmonitor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.bosch.heatingmonitor.mappers.SubscriptionMapper;
import pt.bosch.heatingmonitor.model.SubscriptionDTO;
import pt.bosch.heatingmonitor.repository.SubscriptionRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionMapper mapper;
    private final SubscriptionRepository repository;

    @Override
    public Mono<SubscriptionDTO> saveSubscription(Mono<SubscriptionDTO> dto) {
        return dto.map(mapper::dtoToDomain)
                .flatMap(repository::save)
                .map(mapper::domainToDto);
    }
}
