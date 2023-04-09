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
public class NotificationRouterConfig {

    public static final String NOTIFICATION_PATH = "api/v1/notifications";
    public static final String NOTIFICATION_PATH_ID = NOTIFICATION_PATH + "/{notificationId}";

    private final NotificationHandler handler;

    @Bean
    public RouterFunction<ServerResponse> notificationRoutes() {
        return route()
                .POST(NOTIFICATION_PATH, accept(APPLICATION_JSON), handler::notify)
                .build();
    }
}
