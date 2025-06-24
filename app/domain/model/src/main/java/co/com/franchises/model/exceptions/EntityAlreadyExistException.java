package co.com.franchises.model.exceptions;

import co.com.franchises.model.enums.DomainExceptionsMessage;
import lombok.Getter;

@Getter
public class EntityAlreadyExistException extends DomainException {

    public EntityAlreadyExistException(DomainExceptionsMessage domainExceptionsMessage) {
        super(domainExceptionsMessage);
    }
}
