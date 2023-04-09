package pt.bosch.heatingmonitor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
    private UUID subscription; // Subscription related to this notification
    private String code; // Message code
    private String message; // Detailed message
}
