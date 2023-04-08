package pt.bosch.heatingmonitor.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class SubscriptionRouterConfig {

    public static final String SUBSCRIPTION_PATH = "/subscriptions";
    public static final String SUBSCRIPTION_PATH_ID = SUBSCRIPTION_PATH + "/{subscriptionId}";
    public static final String SUBSCRIPTION_PATH_ACTIVE = SUBSCRIPTION_PATH + ":active";
    public static final String SUBSCRIPTION_PATH_ACTIVATE = SUBSCRIPTION_PATH_ID + ":activate";
    public static final String SUBSCRIPTION_PATH_DEACTIVATE = SUBSCRIPTION_PATH_ID + ":deactivate";

    private final SubscriptionHandler handler;

    @Bean
    public RouterFunction<ServerResponse> subscriptionRoutes() {
        return route()
                .POST(SUBSCRIPTION_PATH, accept(APPLICATION_JSON), handler::subscribe)
                .GET(SUBSCRIPTION_PATH_ID, accept(APPLICATION_JSON), handler::getSubscriptionById)
                .GET(SUBSCRIPTION_PATH_ACTIVE, accept(APPLICATION_JSON), handler::listSubscriptions)
                .POST(SUBSCRIPTION_PATH_ACTIVATE, accept(APPLICATION_JSON), handler::activate)
                .POST(SUBSCRIPTION_PATH_DEACTIVATE, accept(APPLICATION_JSON), handler::deactivate)
                .build();
    }

}
