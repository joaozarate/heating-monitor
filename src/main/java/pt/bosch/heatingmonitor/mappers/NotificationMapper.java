package pt.bosch.heatingmonitor.mappers;

import org.mapstruct.Mapper;
import pt.bosch.heatingmonitor.domain.Notification;
import pt.bosch.heatingmonitor.model.NotificationDTO;

@Mapper
public interface NotificationMapper {

    NotificationDTO domainToDto(Notification domain);

    Notification dtoToDomain(NotificationDTO dto);

}
