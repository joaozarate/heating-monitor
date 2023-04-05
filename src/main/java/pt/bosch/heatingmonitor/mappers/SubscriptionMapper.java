package pt.bosch.heatingmonitor.mappers;

import org.mapstruct.Mapper;
import pt.bosch.heatingmonitor.domain.Subscription;
import pt.bosch.heatingmonitor.model.SubscriptionDTO;

@Mapper
public interface SubscriptionMapper {

    SubscriptionDTO domainToDto(Subscription subscription);

    Subscription dtoToDomain(SubscriptionDTO dto);
}
