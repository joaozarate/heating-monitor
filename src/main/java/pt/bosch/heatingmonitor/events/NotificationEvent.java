package pt.bosch.heatingmonitor.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.bosch.heatingmonitor.model.NotificationDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {

    private String eventType;

    private NotificationDTO dto;

}
