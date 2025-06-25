package co.com.franchises.api.handlers;

import co.com.franchises.api.dto.request.CreateFranchiseDto;
import co.com.franchises.api.mapper.FranchiseMapper;
import co.com.franchises.api.util.ErrorDto;
import co.com.franchises.api.util.GenerateResponse;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityNotFoundException;
import co.com.franchises.usecase.franchise.inputports.FranchiseServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class FranchiseHandler {
    private final FranchiseServicePort franchiseServicePort;
    private final FranchiseMapper franchiseMapper;

    public Mono<ServerResponse> createFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CreateFranchiseDto.class)
                .flatMap(createFranchiseDto ->
                        franchiseServicePort.createFranchise(createFranchiseDto.getName())
                        .doOnSuccess( franchiseSaved -> log.info("Franchise created successfully: {}", franchiseSaved.getName())))
                .flatMap(franchise ->
                        ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(franchiseMapper.toFranchiseDtoRs(franchise)))
                .onErrorResume(DomainException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.CONFLICT, ex.getDomainExceptionsMessage())
                ).onErrorResume(exception -> {
                    log.error("Unexpected error occurred: {}", exception.getMessage(), exception);
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainExceptionsMessage.INTERNAL_ERROR);
                });
    }
}
