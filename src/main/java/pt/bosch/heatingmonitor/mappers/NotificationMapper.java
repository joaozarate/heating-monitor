package pt.bosch.heatingmonitor.mappers;

import org.mapstruct.Mapper;
import pt.bosch.heatingmonitor.domain.Notification;
import pt.bosch.heatingmonitor.model.NotificationRequest;
import pt.bosch.heatingmonitor.model.NotificationResponse;

@Mapper
public interface NotificationMapper {

    NotificationResponse domainToDto(Notification domain);

    Notification dtoToDomain(NotificationRequest dto);

}
