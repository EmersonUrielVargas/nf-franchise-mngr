package co.com.franchises.usecase.product;

import co.com.franchises.model.branch.gateways.BranchPersistencePort;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityAlreadyExistException;
import co.com.franchises.model.exceptions.EntityNotFoundException;
import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.gateways.ProductPersistencePort;
import co.com.franchises.usecase.product.inputports.ProductServicePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase implements ProductServicePort {
    private final ProductPersistencePort productPersistencePort;
    private final BranchPersistencePort branchPersistencePort;

    public Mono<Product> createProduct(Product newProduct) {
        return branchPersistencePort.findById(newProduct.getBranchId())
                .switchIfEmpty( Mono.error(new EntityNotFoundException(DomainExceptionsMessage.BRANCH_NOT_FOUND)))
                .flatMap(branch ->
                        productPersistencePort.isExistProductInBranch(newProduct.getName(), newProduct.getBranchId())
                        .filter(exist -> !exist)
                        .switchIfEmpty(Mono.error(new EntityAlreadyExistException(DomainExceptionsMessage.PRODUCT_ALREADY_EXIST)))
                        .flatMap(exist ->
                                Mono.defer(()->
                                        productPersistencePort.createProduct(newProduct)
                                                .switchIfEmpty(Mono.error(new DomainException(DomainExceptionsMessage.PRODUCT_CREATION_FAIL)))
                                )
                        )
                );
    }

    @Override
    public Mono<Void> deleteProduct(Long productId) {
        return productPersistencePort.findById(productId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(DomainExceptionsMessage.PRODUCT_NOT_FOUND)))
                .flatMap(product ->
                    Mono.defer(()->
                        productPersistencePort.deleteProduct(productId)
                        .filter(isDeleted -> isDeleted)
                        .switchIfEmpty(Mono.error(new DomainException(DomainExceptionsMessage.PRODUCT_DELETE_FAIL)))
                    )
                ).then();
    }
}
