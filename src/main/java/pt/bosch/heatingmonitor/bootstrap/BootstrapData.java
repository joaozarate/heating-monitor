package pt.bosch.heatingmonitor.bootstrap;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pt.bosch.heatingmonitor.domain.Notification;
import pt.bosch.heatingmonitor.domain.Subscription;
import pt.bosch.heatingmonitor.repository.NotificationRepository;
import pt.bosch.heatingmonitor.repository.SubscriptionRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;


    @Override
    public void run(String... args) throws Exception {

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
                .receiverUrl("")
                .active("Y")
                .event("eventName_1")
                .appliance("machine_name_1")
                .build();

        Notification notification = Notification.builder()
                .notificationStatus("Y")
                .message("message here")
                .code("A-222")
                .build();

        subscriptionRepository.save(subscription).flatMap(savedSubscription -> {
            notification.setSubscriptionId(savedSubscription.getId());
            return notificationRepository.save(notification);
        }).subscribe();

    }

}
