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
public class NotificationDTO {

    private UUID id; // UUID
    private String notificationStatus; //'S'ent, 'F'ailed, 'W'aiting
    private String message; // Detailed message
    private String code; // Message code
    private UUID subscriptionId; // Related subscription
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

}
