package co.com.franchises.model.helper;

import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.InvalidValueParamException;

public class Validator {

    public static <T> void validateNotNull(T value, DomainExceptionsMessage message) {
        if (value == null) {
            throw new InvalidValueParamException(message);
        }else if (value instanceof String stringValue && (stringValue).isBlank()) {
            throw new InvalidValueParamException(message);
        }
    }

    public static void validatePositive(int value, DomainExceptionsMessage message) {
        if (value < 0) {
            throw new InvalidValueParamException(message);
        }
    }
}
