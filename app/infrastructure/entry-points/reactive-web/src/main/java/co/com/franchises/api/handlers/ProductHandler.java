package co.com.franchises.api.handlers;

import co.com.franchises.api.dto.request.CreateBranchDto;
import co.com.franchises.api.dto.request.CreateProductDto;
import co.com.franchises.api.mapper.BranchMapper;
import co.com.franchises.api.mapper.ProductMapper;
import co.com.franchises.api.util.ErrorDto;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityNotFoundException;
import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.gateways.ProductPersistencePort;
import co.com.franchises.usecase.branch.inputports.BranchServicePort;
import co.com.franchises.usecase.product.inputports.ProductServicePort;
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
public class ProductHandler {
    private final ProductServicePort productServicePort;
    private final ProductMapper productMapper;

    public Mono<ServerResponse> createProduct(ServerRequest serverRequest) {
        Long branchId = Long.valueOf(serverRequest.pathVariable("id"));
        return serverRequest.bodyToMono(CreateProductDto.class)
                .map(createProductDto -> {
                    Product productToCreate = productMapper.toProduct(createProductDto);
                    productToCreate.setBranchId(branchId);
                    return productToCreate;
                })
                .flatMap(product ->
                        productServicePort.createProduct(product)
                        .doOnSuccess( branchCreated -> log.info("Product created successfully: {}", product))
                ).flatMap(productCreated ->
                        ServerResponse.status(HttpStatus.CREATED)
                        .bodyValue(productMapper.toProductDto(productCreated)))
                .onErrorResume(DomainException.class, ex ->
                        ServerResponse.status(HttpStatus.CONFLICT)
                            .bodyValue(ErrorDto.builder()
                                    .code(ex.getDomainExceptionsMessage().getCode())
                                    .message(ex.getDomainExceptionsMessage().getMessage())
                                    .param(ex.getDomainExceptionsMessage().getParam())
                                    .build()
                            )
                ).onErrorResume(EntityNotFoundException.class, ex ->
                        ServerResponse.status(HttpStatus.NOT_FOUND)
                                .bodyValue(ErrorDto.builder()
                                        .code(ex.getDomainExceptionsMessage().getCode())
                                        .message(ex.getDomainExceptionsMessage().getMessage())
                                        .param(ex.getDomainExceptionsMessage().getParam())
                                        .build()
                                )
                ).onErrorResume(exception -> {
                    log.error("Unexpected error occurred: {}", exception.getMessage(), exception);
                    return  ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(ErrorDto.builder()
                                    .code(DomainExceptionsMessage.INTERNAL_ERROR.getCode())
                                    .message(DomainExceptionsMessage.INTERNAL_ERROR.getMessage())
                                    .param(DomainExceptionsMessage.INTERNAL_ERROR.getParam())
                                    .build()
                            );
                });
    }
}
