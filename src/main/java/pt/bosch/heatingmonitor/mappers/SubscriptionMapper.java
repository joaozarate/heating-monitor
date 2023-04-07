package pt.bosch.heatingmonitor.mappers;

import org.mapstruct.Mapper;
import pt.bosch.heatingmonitor.domain.Subscription;
import pt.bosch.heatingmonitor.model.SubscriptionDTO;
import pt.bosch.heatingmonitor.model.SubscriptionRequest;

@Mapper
public interface SubscriptionMapper {

    SubscriptionDTO domainToDto(Subscription domain);

    Subscription dtoToDomain(SubscriptionRequest dto);
}
