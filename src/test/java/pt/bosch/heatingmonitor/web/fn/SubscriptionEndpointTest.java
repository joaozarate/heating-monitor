package pt.bosch.heatingmonitor.web.fn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import pt.bosch.heatingmonitor.model.SubscriptionRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@AutoConfigureWebTestClient
class SubscriptionEndpointTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void testCreateSubscription() {
        SubscriptionRequest request = SubscriptionRequest.builder()
                .device(UUID.randomUUID().toString())
                .baseReceiverUrl("https://remote.com:8080")
                .relativeReceiverUrl("/api/v1/alert")
                .build();

        webTestClient.post()
                .uri(SubscriptionRouterConfig.SUBSCRIPTION_PATH)
                .body(Mono.just(request), SubscriptionRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectHeader().exists("location")
                .expectBody()
                .jsonPath("$.baseReceiverUrl").value(notNullValue())
                .jsonPath("$.baseReceiverUrl").value(equalTo(request.getBaseReceiverUrl()))
                .jsonPath("$.relativeReceiverUrl").value(equalTo(request.getRelativeReceiverUrl()))
                .jsonPath("$.active").value(equalTo(true))
                .jsonPath("$.device").value(equalTo(request.getDevice()))
                .jsonPath("$.createdDate").value(notNullValue())
                .jsonPath("$.lastModifiedDate").value(notNullValue());
    }

    @Test
    void testCreateSubscriptionDeviceNull() {
        SubscriptionRequest request = SubscriptionRequest.builder()
                .device(null)
                .baseReceiverUrl("https://remote.com:8080")
                .relativeReceiverUrl("/api/v1/alert")
                .build();

        webTestClient.post()
                .uri(SubscriptionRouterConfig.SUBSCRIPTION_PATH)
                .body(Mono.just(request), SubscriptionRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody()
                .jsonPath("$.message").value(notNullValue())
                .jsonPath("$.code").value(notNullValue());
    }

}