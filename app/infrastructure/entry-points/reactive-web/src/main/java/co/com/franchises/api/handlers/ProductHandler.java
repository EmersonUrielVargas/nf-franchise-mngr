package co.com.franchises.api.handlers;

import co.com.franchises.api.constants.GeneralConstants;
import co.com.franchises.api.dto.request.CreateBranchDto;
import co.com.franchises.api.dto.request.CreateProductDto;
import co.com.franchises.api.dto.request.UpdateProductNameDto;
import co.com.franchises.api.dto.request.UpdateStockProductDto;
import co.com.franchises.api.mapper.BranchMapper;
import co.com.franchises.api.mapper.ProductMapper;
import co.com.franchises.api.util.ErrorDto;
import co.com.franchises.api.util.GenerateResponse;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityNotFoundException;
import co.com.franchises.model.exceptions.InvalidValueParamException;
import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.ProductRankItem;
import co.com.franchises.model.product.gateways.ProductPersistencePort;
import co.com.franchises.usecase.branch.inputports.BranchServicePort;
import co.com.franchises.usecase.product.inputports.ProductServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductHandler {
    private final ProductServicePort productServicePort;
    private final ProductMapper productMapper;

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        Long branchId = Long.valueOf(serverRequest.pathVariable(GeneralConstants.PATH_PARAMETER_ID_NAME));
        return serverRequest.bodyToMono(CreateProductDto.class)
                .map(createProductDto -> {
                    Product productToCreate = productMapper.toProduct(createProductDto);
                    productToCreate.setBranchId(branchId);
                    return productToCreate;
                })
                .flatMap(product ->
                        productServicePort.createProduct(product)
                        .doOnSuccess( productCreated -> log.info("Product created successfully: {}", productCreated))
                ).flatMap(productCreated ->
                        ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(productMapper.toProductDto(productCreated)))
                .onErrorResume(InvalidValueParamException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.BAD_REQUEST, ex.getDomainExceptionsMessage()))
                .onErrorResume(EntityNotFoundException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.NOT_FOUND, ex.getDomainExceptionsMessage())
                ).onErrorResume(DomainException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.CONFLICT, ex.getDomainExceptionsMessage())
                ).onErrorResume(exception -> {
                    log.error(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG, exception.getMessage(), exception);
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainExceptionsMessage.INTERNAL_ERROR);
                });
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest){
        Long productId = Long.valueOf(serverRequest.pathVariable(GeneralConstants.PATH_PARAMETER_ID_NAME));
        return productServicePort.deleteProduct(productId)
                .then(ServerResponse.noContent().build())
                .onErrorResume(InvalidValueParamException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.BAD_REQUEST, ex.getDomainExceptionsMessage()))
                .onErrorResume(EntityNotFoundException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.NOT_FOUND, ex.getDomainExceptionsMessage())
                ).onErrorResume(DomainException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.CONFLICT, ex.getDomainExceptionsMessage())
                ).onErrorResume(exception -> {
                    log.error(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG, exception.getMessage(), exception);
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainExceptionsMessage.INTERNAL_ERROR);
                });
    }

    public Mono<ServerResponse> updateStockProduct(ServerRequest serverRequest) {
        Long productId = Long.valueOf(serverRequest.pathVariable(GeneralConstants.PATH_PARAMETER_ID_NAME));
        return serverRequest.bodyToMono(UpdateStockProductDto.class)
                .flatMap(requestBody ->
                        productServicePort.updateStockProduct(productId, requestBody.getStock())
                                .doOnSuccess( product -> log.info("Product stock updated successfully: {}", product))
                ).flatMap(productUpdated ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(productMapper.toProductDto(productUpdated)))
                .onErrorResume(InvalidValueParamException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.BAD_REQUEST, ex.getDomainExceptionsMessage()))
                .onErrorResume(EntityNotFoundException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.NOT_FOUND, ex.getDomainExceptionsMessage()))
                .onErrorResume(DomainException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.CONFLICT, ex.getDomainExceptionsMessage()))
                .onErrorResume(exception -> {
                    log.error(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG, exception.getMessage(), exception);
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainExceptionsMessage.INTERNAL_ERROR);
                });
    }

    public Mono<ServerResponse> getTopProductsByBranchInFranchise(ServerRequest serverRequest) {
        Long franchiseId = Long.valueOf(serverRequest.pathVariable(GeneralConstants.PATH_PARAMETER_ID_NAME));
        return productServicePort.getRankProductsStockByBranch(franchiseId)
            .collectList()
            .flatMap(productList ->
                ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(productList)
            ).onErrorResume(InvalidValueParamException.class, ex ->
                    GenerateResponse.generateErrorResponse(HttpStatus.BAD_REQUEST, ex.getDomainExceptionsMessage()))
            .onErrorResume(EntityNotFoundException.class, ex ->
                    GenerateResponse.generateErrorResponse(HttpStatus.NOT_FOUND, ex.getDomainExceptionsMessage()))
            .onErrorResume(DomainException.class, ex ->
                    GenerateResponse.generateErrorResponse(HttpStatus.CONFLICT, ex.getDomainExceptionsMessage()))
            .onErrorResume(exception -> {
                log.error(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG, exception.getMessage(), exception);
                return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainExceptionsMessage.INTERNAL_ERROR);
            });
    }

    public Mono<ServerResponse> updateNameProduct(ServerRequest serverRequest){
        Long productId = Long.valueOf(serverRequest.pathVariable(GeneralConstants.PATH_PARAMETER_ID_NAME));
        return serverRequest.bodyToMono(UpdateProductNameDto.class)
                .flatMap(updateProductNameDto ->
                        productServicePort.updateNameProduct(productId, updateProductNameDto.getName())
                                .doOnSuccess( product -> log.info("Product name updated successfully: {}", product))
                ).flatMap(productUpdated ->
                        ServerResponse.status(HttpStatus.OK)
                                .bodyValue(productMapper.toProductDto(productUpdated)))
                .onErrorResume(InvalidValueParamException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.BAD_REQUEST, ex.getDomainExceptionsMessage()))
                .onErrorResume(EntityNotFoundException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.NOT_FOUND, ex.getDomainExceptionsMessage())
                ).onErrorResume(DomainException.class, ex ->
                        GenerateResponse.generateErrorResponse(HttpStatus.CONFLICT, ex.getDomainExceptionsMessage())
                ).onErrorResume(exception -> {
                    log.error(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG, exception.getMessage(), exception);
                    return  GenerateResponse.generateErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, DomainExceptionsMessage.INTERNAL_ERROR);
                });
    }
}
