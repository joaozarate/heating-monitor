package pt.bosch.heatingmonitor.web.fn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import pt.bosch.heatingmonitor.model.NotificationRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static pt.bosch.heatingmonitor.web.fn.NotificationRouterConfig.NOTIFICATION_PATH;

@SpringBootTest
@AutoConfigureWebTestClient
class NotificationEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testCreateSubscription() {
        NotificationRequest request = NotificationRequest.builder()
                .message("Test message")
                .code("TEST")
                .subscription(UUID.randomUUID().toString())
                .build();

        webTestClient.post()
                .uri(NOTIFICATION_PATH)
                .body(Mono.just(request), NotificationRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectHeader().exists("location")
                .expectBody()
                .jsonPath("$.id").value(notNullValue())
                .jsonPath("$.notificationStatus").value(equalTo("W"))
                .jsonPath("$.message").value(equalTo(request.getMessage()))
                .jsonPath("$.code").value(equalTo(request.getCode()))
                .jsonPath("$.createdDate").value(notNullValue())
                .jsonPath("$.lastModifiedDate").value(notNullValue())
                .jsonPath("$.subscription").value(equalTo(request.getSubscription()));
    }

    @Test
    void testCreateSubscriptionMissingSubscriptionId() {
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

}