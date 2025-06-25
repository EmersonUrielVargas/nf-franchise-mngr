package co.com.franchises.api.handlers;

import co.com.franchises.api.dto.request.CreateBranchDto;
import co.com.franchises.api.dto.request.CreateFranchiseDto;
import co.com.franchises.api.dto.request.UpdateBranchNameDto;
import co.com.franchises.api.mapper.BranchMapper;
import co.com.franchises.api.mapper.FranchiseMapper;
import co.com.franchises.api.util.ErrorDto;
import co.com.franchises.api.util.GenerateResponse;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.branch.gateways.BranchPersistencePort;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityNotFoundException;
import co.com.franchises.model.exceptions.InvalidValueParamException;
import co.com.franchises.usecase.branch.inputports.BranchServicePort;
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
public class BranchHandler {
    private final BranchServicePort branchServicePort;
    private final BranchMapper branchMapper;

    public Mono<ServerResponse> createBranch(ServerRequest serverRequest) {
        Long franchiseId = Long.valueOf(serverRequest.pathVariable("id"));
        return serverRequest.bodyToMono(CreateBranchDto.class)
                .flatMap(createFranchiseDto ->{
                    Branch branchToCreate = branchMapper.toBranch(createFranchiseDto);
                    branchToCreate.setFranchiseId(franchiseId);
                    return  branchServicePort.createBranch(branchToCreate)
                            .doOnSuccess( branchCreated -> log.info("Branch office created successfully: {}", branchToCreate));
                })
                .flatMap(branchCreated ->
                        ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(branchMapper.toBranchDtoRs(branchCreated)))
                .onErrorResume(InvalidValueParamException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.BAD_REQUEST, ex.getDomainExceptionsMessage()))
                .onErrorResume(EntityNotFoundException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.NOT_FOUND, ex.getDomainExceptionsMessage())
                ).onErrorResume(DomainException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.CONFLICT, ex.getDomainExceptionsMessage())
                ).onErrorResume(exception -> {
                    log.error("Unexpected error occurred: {}", exception.getMessage(), exception);
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainExceptionsMessage.INTERNAL_ERROR);
                });
    }

    public Mono<ServerResponse> updateBranchName(ServerRequest serverRequest) {
        Long branchId = Long.valueOf(serverRequest.pathVariable("id"));
        return serverRequest.bodyToMono(UpdateBranchNameDto.class)
                .flatMap(updateBranchNameDto ->
                        branchServicePort.updateBranchName(branchId, updateBranchNameDto.getName())
                            .doOnSuccess( branchUpdated -> log.info("Branch office name updated successfully: {}", branchUpdated))
                )
                .flatMap(branchUpdated ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(branchMapper.toBranchDtoRs(branchUpdated)))
                .onErrorResume(InvalidValueParamException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.BAD_REQUEST, ex.getDomainExceptionsMessage()))
                .onErrorResume(EntityNotFoundException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.NOT_FOUND, ex.getDomainExceptionsMessage())
                ).onErrorResume(DomainException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.CONFLICT, ex.getDomainExceptionsMessage())
                ).onErrorResume(exception -> {
                    log.error("Unexpected error occurred: {}", exception.getMessage(), exception);
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainExceptionsMessage.INTERNAL_ERROR);
                });
    }
}
