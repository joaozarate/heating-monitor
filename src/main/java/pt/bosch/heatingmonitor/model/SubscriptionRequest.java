package pt.bosch.heatingmonitor.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequest {

    @NotBlank
    private String baseReceiverUrl; // Protocol + host + port

    @NotBlank
    private String relativeReceiverUrl; // Path

    @NotBlank
    private String device; // A device or piece of equipment
}
