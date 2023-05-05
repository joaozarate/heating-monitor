package pt.bosch.heatingmonitor.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pt.bosch.heatingmonitor.domain.Notification;
import pt.bosch.heatingmonitor.domain.Subscription;
import pt.bosch.heatingmonitor.repository.NotificationRepository;
import pt.bosch.heatingmonitor.repository.SubscriptionRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;

    public static Subscription subscriptionVar;


    @Override
    public void run(String... args) {

        loadData();

        subscriptionRepository.count().subscribe(count -> {
           System.out.println("Subscription count is: " + count);
        });

        notificationRepository.count().subscribe(count -> {
            System.out.println("Notification count is: " + count);
        });

    }

    private void loadData() {

        Subscription subscription = Subscription.builder()
                .active(true)
                .device(UUID.randomUUID())
                .build();

        Notification notification = Notification.builder()
                .notificationStatus("S")
                .message("message here")
                .code("M-222")
                .build();

        subscriptionRepository.save(subscription).flatMap(savedSubscription -> {
            subscriptionVar = savedSubscription;
            notification.setSubscription(savedSubscription.getId());
            return notificationRepository.save(notification);
        }).subscribe();

    }

}
