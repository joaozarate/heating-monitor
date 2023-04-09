package pt.bosch.heatingmonitor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pt.bosch.heatingmonitor.domain.Notification;
import pt.bosch.heatingmonitor.events.NotificationEvent;
import pt.bosch.heatingmonitor.exceptions.NotificationException;
import pt.bosch.heatingmonitor.mappers.NotificationMapper;
import pt.bosch.heatingmonitor.model.NotificationRequest;
import pt.bosch.heatingmonitor.repository.NotificationRepository;
import pt.bosch.heatingmonitor.repository.SubscriptionRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper mapper;
    private final NotificationRepository repository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public Mono<Notification> notify(Mono<NotificationRequest> request) {
        return request.map(mapper::dtoToDomain)
                .flatMap(notification -> {
                    notification.setNotificationStatus("W");
                    return repository.save(notification);
                })
                .doOnSuccess(entity -> {
                    applicationEventPublisher.publishEvent(NotificationEvent.builder().notification(entity).build());
                }).onErrorResume(Exception.class,  e -> Mono.error(new NotificationException("An unexpected error occurred while notifying.", e)));
    }

    @Override
    public void updateStatus(Mono<UUID> subscriptionId, String newStatus) {
        repository.findAllById(subscriptionId).flatMap(entity -> {
            entity.setNotificationStatus(newStatus);
            return repository.save(entity);
        });
    }
}
