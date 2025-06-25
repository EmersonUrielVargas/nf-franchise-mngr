package co.com.franchises.api.util;

import co.com.franchises.model.enums.DomainExceptionsMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public class GenerateResponse {

    public static Mono<ServerResponse> generateErrorResponse(HttpStatus status, DomainExceptionsMessage errorInfo) {
        return ServerResponse.status(status)
                .bodyValue(ErrorDto.builder()
                        .code(errorInfo.getCode())
                        .message(errorInfo.getMessage())
                        .param(errorInfo.getParam())
                        .build());
    }
}
