package pt.bosch.heatingmonitor.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class SubscriptionRouterConfig {

    public static final String SUBSCRIPTION_PATH = "/api/v1/subscriptions";
    public static final String SUBSCRIPTION_PATH_ID = SUBSCRIPTION_PATH + "/{subscriptionId}";
    public static final String SUBSCRIPTION_PATH_LISTEN = SUBSCRIPTION_PATH_ID + ":listen";
    public static final String SUBSCRIPTION_PATH_DEACTIVATE = SUBSCRIPTION_PATH_ID + ":deactivate";

    private final SubscriptionHandler handler;

    @Bean
    public RouterFunction<ServerResponse> subscriptionRoutes() {
        return route()
                .POST(SUBSCRIPTION_PATH, accept(APPLICATION_JSON), handler::subscribe)
                .GET(SUBSCRIPTION_PATH_LISTEN, accept(TEXT_EVENT_STREAM), handler::listen)
                .POST(SUBSCRIPTION_PATH_DEACTIVATE, accept(APPLICATION_JSON), handler::deactivate)
                .GET(SUBSCRIPTION_PATH_ID, accept(APPLICATION_JSON), handler::getSubscriptionById)
                .build();
    }

}
