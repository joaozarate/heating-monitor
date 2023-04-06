package pt.bosch.heatingmonitor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionDTO {

    private UUID id; // UUID
    private String baseReceiverUrl; // Protocol + host + port
    private String relativeReceiverUrl; // Path
    private String active = "Y"; // Y or N
    private String event; // Event name subscribed
    private String appliance; // A device or piece of equipment
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}
