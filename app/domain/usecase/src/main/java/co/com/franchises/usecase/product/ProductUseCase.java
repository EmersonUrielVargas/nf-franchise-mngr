package co.com.franchises.usecase.product;

import co.com.franchises.model.branch.gateways.BranchPersistencePort;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityAlreadyExistException;
import co.com.franchises.model.exceptions.EntityNotFoundException;
import co.com.franchises.model.franchise.gateways.FranchisePersistencePort;
import co.com.franchises.model.helper.Validator;
import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.ProductRankItem;
import co.com.franchises.model.product.gateways.ProductPersistencePort;
import co.com.franchises.usecase.product.inputports.ProductServicePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductUseCase implements ProductServicePort {
    private final ProductPersistencePort productPersistencePort;
    private final BranchPersistencePort branchPersistencePort;
    private final FranchisePersistencePort franchisePersistencePort;


    public Mono<Product> createProduct(Product newProduct) {
        Validator.validateNotNull(newProduct.getBranchId(), DomainExceptionsMessage.PARAM_REQUIRED);
        Validator.validateNotNull(newProduct.getName(), DomainExceptionsMessage.PARAM_REQUIRED);
        Validator.validateNotNull(newProduct.getStock(), DomainExceptionsMessage.PARAM_REQUIRED);
        Validator.validatePositive(newProduct.getStock(), DomainExceptionsMessage.STOCK_INVALID);
        return branchPersistencePort.findById(newProduct.getBranchId())
                .switchIfEmpty( Mono.error(new EntityNotFoundException(DomainExceptionsMessage.BRANCH_NOT_FOUND)))
                .flatMap(branch ->
                        productPersistencePort.isExistProductInBranch(newProduct.getName(), newProduct.getBranchId())
                        .filter(exist -> !exist)
                        .switchIfEmpty(Mono.error(new EntityAlreadyExistException(DomainExceptionsMessage.PRODUCT_ALREADY_EXIST)))
                        .flatMap(exist ->
                                Mono.defer(()->
                                        productPersistencePort.upsertProduct(newProduct)
                                                .switchIfEmpty(Mono.error(new DomainException(DomainExceptionsMessage.PRODUCT_CREATION_FAIL)))
                                )
                        )
                );
    }

    @Override
    public Mono<Product> updateStockProduct(Long productId, Integer stock) {
        Validator.validateNotNull(productId, DomainExceptionsMessage.PARAM_REQUIRED);
        Validator.validateNotNull(stock, DomainExceptionsMessage.PARAM_REQUIRED);
        Validator.validatePositive(stock, DomainExceptionsMessage.STOCK_INVALID);
        return  productPersistencePort.findById(productId)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(DomainExceptionsMessage.PRODUCT_NOT_FOUND)))
                .map(productFound -> {
                    productFound.setStock(stock);
                    return productFound;
                })
                .flatMap(productUpdate ->
                    Mono.defer(()->
                        Mono.defer(()->
                            productPersistencePort.upsertProduct(productUpdate)
                            .switchIfEmpty(Mono.error(new DomainException(DomainExceptionsMessage.PRODUCT_UPDATE_FAIL)))
                        )
                    )
                );
    }

    @Override
    public Mono<Void> deleteProduct(Long productId) {
        Validator.validateNotNull(productId, DomainExceptionsMessage.PARAM_REQUIRED);
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

    @Override
    public Flux<ProductRankItem> getRankProductsStockByBranch(Long franchiseId) {
        Validator.validateNotNull(franchiseId, DomainExceptionsMessage.PARAM_REQUIRED);
        return franchisePersistencePort.findById(franchiseId)
                .switchIfEmpty( Mono.error(new EntityNotFoundException(DomainExceptionsMessage.FRANCHISE_NOT_FOUND)))
                .thenMany(productPersistencePort.getRankProductsStockByBranch(franchiseId)
                        .switchIfEmpty(Mono.error(new DomainException(DomainExceptionsMessage.FRANCHISE_WITHOUT_BRANCH_OFFICES)))
                );
    }

}
