package pt.bosch.heatingmonitor.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import pt.bosch.heatingmonitor.domain.Notification;

import java.util.UUID;

public interface NotificationRepository extends ReactiveCrudRepository<Notification, UUID> {
}
