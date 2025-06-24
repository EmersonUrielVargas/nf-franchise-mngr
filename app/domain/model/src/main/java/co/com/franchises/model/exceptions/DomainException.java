package co.com.franchises.model.exceptions;

import co.com.franchises.model.enums.DomainExceptionsMessage;
import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {

    private final DomainExceptionsMessage domainExceptionsMessage;

    public DomainException(DomainExceptionsMessage domainExceptionsMessage) {
        super(domainExceptionsMessage.getMessage());
        this.domainExceptionsMessage = domainExceptionsMessage;
    }
}
