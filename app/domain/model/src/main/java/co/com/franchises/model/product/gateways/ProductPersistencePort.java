package co.com.franchises.model.product.gateways;

import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.ProductRankItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {

    Mono<Product> upsertProduct(Product product);
    Mono<Product> findById(Long productId);
    Mono<Boolean> isExistProductInBranch(String productName, Long branchId);
    Mono<Boolean> deleteProduct(Long productId);
    Flux<ProductRankItem> getRankProductsStockByBranch(Long franchiseId);
}
