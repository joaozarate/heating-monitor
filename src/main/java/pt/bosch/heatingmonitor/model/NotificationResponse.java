package pt.bosch.heatingmonitor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Notification response
 * Using Lombok to generate getters, setters, toString, equals and hashCode
 * Using Builder pattern to create instances
 * Using Google naming conventions for class names.
 * @https://cloud.google.com/apis/design/naming_convention
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private UUID id; // UUID
    private String notificationStatus; //'S'ent, 'F'ailed, 'W'aiting
    private String message; // Detailed message
    private String code; // Message code
    private UUID subscription; // Related subscription

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd hh:mm:ss a")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd hh:mm:ss a")
    private LocalDateTime lastModifiedDate;

}
