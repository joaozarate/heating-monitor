package pt.bosch.heatingmonitor.web.fn;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import pt.bosch.heatingmonitor.bootstrap.BootstrapData;
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
                .build();

        webTestClient.post()
                .uri(SubscriptionRouterConfig.SUBSCRIPTION_PATH)
                .body(Mono.just(request), SubscriptionRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectHeader().exists("location")
                .expectBody()
                .jsonPath("$.active").value(equalTo(true))
                .jsonPath("$.device").value(equalTo(request.getDevice()))
                .jsonPath("$.createdDate").value(notNullValue())
                .jsonPath("$.lastModifiedDate").value(notNullValue());
    }

    @Test
    void testCreateSubscriptionDeviceNull() {
        SubscriptionRequest request = SubscriptionRequest.builder()
                .device(null)
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

    @Test
    void testGetSubscriptionWithInvalidId() {
        webTestClient.get()
                .uri(SubscriptionRouterConfig.SUBSCRIPTION_PATH_ID , 1)
                .exchange()
                .expectStatus().isBadRequest()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody()
                .jsonPath("$.code").value(equalTo("E-123"))
                .jsonPath("$.message").value(equalTo("Invalid subscriptionId"));
    }

    @Test
    void testGetSubscriptionWithUnknownId() {
        webTestClient.get()
                .uri(SubscriptionRouterConfig.SUBSCRIPTION_PATH_ID , UUID.randomUUID().toString())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetSubscriptionWithValidId() {
        webTestClient.get().uri(SubscriptionRouterConfig.SUBSCRIPTION_PATH_ID, BootstrapData.subscriptionVar.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-type", "application/json")
                .expectBody()
                .jsonPath("$.id").value(equalTo(BootstrapData.subscriptionVar.getId().toString()))
                .jsonPath("$.active").value(equalTo(BootstrapData.subscriptionVar.getActive()))
                .jsonPath("$.device").value(equalTo(BootstrapData.subscriptionVar.getDevice().toString()))
                .jsonPath("$.createdDate").value(notNullValue())
                .jsonPath("$.lastModifiedDate").value(notNullValue());
    }

}
