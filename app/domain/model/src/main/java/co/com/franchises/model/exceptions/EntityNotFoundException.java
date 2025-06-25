package co.com.franchises.model.exceptions;

import co.com.franchises.model.enums.DomainExceptionsMessage;
import lombok.Getter;

@Getter
public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(DomainExceptionsMessage domainExceptionsMessage) {
        super(domainExceptionsMessage);
    }
}
