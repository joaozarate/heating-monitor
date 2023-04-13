package pt.bosch.heatingmonitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.bosch.heatingmonitor.model.NotificationResponse;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class ConcurrentHashMapSubscriptionSinkMapConfiguration {

    @Bean
    public Map<String, Sinks.Many<NotificationResponse>> sinkMap() {
        return new ConcurrentHashMap<>();
    }

}
