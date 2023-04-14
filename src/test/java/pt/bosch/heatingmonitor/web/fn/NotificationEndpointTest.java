package pt.bosch.heatingmonitor.web.fn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import pt.bosch.heatingmonitor.domain.Subscription;
import pt.bosch.heatingmonitor.model.NotificationRequest;
import pt.bosch.heatingmonitor.model.SubscriptionRequest;
import pt.bosch.heatingmonitor.repository.SubscriptionRepository;
import reactor.core.publisher.Mono;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static pt.bosch.heatingmonitor.web.fn.NotificationRouterConfig.NOTIFICATION_PATH;
import static pt.bosch.heatingmonitor.web.fn.SubscriptionRouterConfig.SUBSCRIPTION_PATH;

@SpringBootTest
@AutoConfigureWebTestClient
class NotificationEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Test
    void testCreateNotification() {

        Subscription subscription = createSubscription();

        NotificationRequest notificationRequest = NotificationRequest.builder()
                .message("Temperature has reached the limit.")
                .code("A-322")
                .subscription(subscription.getId().toString())
                .build();

        webTestClient.post()
                .uri(NOTIFICATION_PATH)
                .header("Content-type", "application/json")
                .body(Mono.just(notificationRequest), NotificationRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectHeader().exists("location")
                .expectBody()
                .jsonPath("$.id").value(notNullValue())
                .jsonPath("$.notificationStatus").value(equalTo("W"))
                .jsonPath("$.message").value(equalTo(notificationRequest.getMessage()))
                .jsonPath("$.code").value(equalTo(notificationRequest.getCode()))
                .jsonPath("$.createdDate").value(notNullValue())
                .jsonPath("$.lastModifiedDate").value(notNullValue())
                .jsonPath("$.subscription").value(equalTo(notificationRequest.getSubscription()));
    }

    private Subscription createSubscription() {
        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .device(UUID.randomUUID().toString())
                .build();

        webTestClient.post()
                .uri(SUBSCRIPTION_PATH)
                .body(Mono.just(subscriptionRequest), SubscriptionRequest.class)
                .exchange().expectBody();

        Subscription subscription = subscriptionRepository.findAll().collectList().block().stream()
                .filter(s -> s.getDevice().toString().equals(subscriptionRequest.getDevice().toString()))
                .findFirst()
                .get();
        return subscription;
    }

    @Test
    void testCreateNotificationMissingSubscriptionId() {
        NotificationRequest request = NotificationRequest.builder()
                .message("Test message")
                .code("TEST")
                .build();

        webTestClient.post()
                .uri(NOTIFICATION_PATH)
                .body(Mono.just(request), NotificationRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody()
                .jsonPath("$.code").value(equalTo("E-123"))
                .jsonPath("$.message").value(notNullValue());
    }

    @Test
    void testCreateNotificationWithInvalidSubscriptionId() {
        NotificationRequest request = NotificationRequest.builder()
                .message("Test message")
                .code("TEST")
                .subscription("invalid")
                .build();

        webTestClient.post()
                .uri(NOTIFICATION_PATH)
                .body(Mono.just(request), NotificationRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody()
                .jsonPath("$.code").value(equalTo("E-123"))
                .jsonPath("$.message").value(notNullValue());
    }

}