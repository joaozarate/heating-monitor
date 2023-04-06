package pt.bosch.heatingmonitor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pt.bosch.heatingmonitor.events.NotificationEvent;
import pt.bosch.heatingmonitor.mappers.NotificationMapper;
import pt.bosch.heatingmonitor.model.NotificationDTO;
import pt.bosch.heatingmonitor.repository.NotificationRepository;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper mapper;
    private final NotificationRepository repository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Mono<NotificationDTO> notify(Mono<NotificationDTO> dto) {
        return dto.map(mapper::dtoToDomain)
                .flatMap(entity -> {
                    entity.setNotificationStatus("W");
                    return repository.save(entity);
                })
                .map(mapper::domainToDto)
                .doOnSuccess(result -> {
                    applicationEventPublisher.publishEvent(NotificationEvent.builder().dto(result).build());
                });
    }
}
