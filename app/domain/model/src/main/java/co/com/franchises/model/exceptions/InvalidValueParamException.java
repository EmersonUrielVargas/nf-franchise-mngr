package co.com.franchises.model.exceptions;

import co.com.franchises.model.enums.DomainExceptionsMessage;
import lombok.Getter;

@Getter
public class InvalidValueParamException extends DomainException {

    public InvalidValueParamException(DomainExceptionsMessage domainExceptionsMessage) {
        super(domainExceptionsMessage);
    }
}
