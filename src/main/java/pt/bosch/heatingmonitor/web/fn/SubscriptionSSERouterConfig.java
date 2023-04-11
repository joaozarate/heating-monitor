package pt.bosch.heatingmonitor.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class SubscriptionSSERouterConfig {

    public static final String SUBSCRIPTION_SSE_PATH = "/api/v1/sse";

    private final SubscriptionSSEHandler handler;

    @Bean
    public RouterFunction<ServerResponse> subscriptionSSERoutes() {
        return route()
                .GET(SUBSCRIPTION_SSE_PATH, accept(TEXT_EVENT_STREAM), handler::subscribe)
                .build();
    }

}
