package pt.bosch.heatingmonitor.model;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private String subscription; // Subscription related to this notification

    @NotBlank
    private String code; // Message code

    @NotBlank
    private String message; // Detailed message
}
