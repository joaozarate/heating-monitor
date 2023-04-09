package pt.bosch.heatingmonitor.mappers;

import org.mapstruct.Mapper;
import pt.bosch.heatingmonitor.domain.Subscription;
import pt.bosch.heatingmonitor.model.SubscriptionRequest;
import pt.bosch.heatingmonitor.model.SubscriptionResponse;

@Mapper
public interface SubscriptionMapper {

    SubscriptionResponse domainToDto(Subscription domain);

    Subscription dtoToDomain(SubscriptionRequest dto);
}
