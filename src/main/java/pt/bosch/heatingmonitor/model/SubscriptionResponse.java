package pt.bosch.heatingmonitor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class SubscriptionResponse {
    private UUID id; // UUID
//    private String baseReceiverUrl; // Protocol + host + port
//    private String relativeReceiverUrl; // Path
    private Boolean active;
    private UUID device; // A device or piece of equipment

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd hh:mm:ss a")
    private LocalDateTime createdDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd hh:mm:ss a")
    private LocalDateTime lastModifiedDate;
}
