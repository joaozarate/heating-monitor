package pt.bosch.heatingmonitor.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.bosch.heatingmonitor.domain.Notification;
import pt.bosch.heatingmonitor.exceptions.NotificationException;
import pt.bosch.heatingmonitor.mappers.NotificationMapper;
import pt.bosch.heatingmonitor.model.NotificationRequest;
import pt.bosch.heatingmonitor.repository.NotificationRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper mapper;
    private final NotificationRepository repository;
    private final EventService eventService;

    @Override
    public Mono<Notification> notify(Mono<NotificationRequest> request) {
        return request.map(mapper::dtoToDomain)
                .flatMap(notification -> {
                    notification.setNotificationStatus("W");
                    return repository.save(notification);
                })
                .doOnSuccess(entity -> {
                    eventService.emitMessage(entity.getSubscription().toString(), mapper.domainToDto(entity));
                    updateStatus(Mono.just(entity.getSubscription()), "S").subscribe();
                })
                .onErrorResume(NotificationException.class, e ->
                        request.flatMap(notificationRequest ->
                                updateStatus(Mono.just(UUID.fromString(notificationRequest.getSubscription())), "F")
                                        .then(Mono.error(e))
                        )
                );
    }

    @Override
    public Mono<Notification> updateStatus(Mono<UUID> subscriptionId, String newStatus) {
        return subscriptionId
                .flatMap(id -> repository.findById(id)
                        .flatMap(notification -> {
                            notification.setNotificationStatus(newStatus);
                            return repository.save(notification);
                        })
                )
                .switchIfEmpty(Mono.error(new NotificationException("Subscription not found.")));
    }
}
