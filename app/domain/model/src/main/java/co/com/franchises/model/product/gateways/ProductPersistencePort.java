package co.com.franchises.model.product.gateways;

import co.com.franchises.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductPersistencePort {

    Mono<Product> createProduct(Product product);
    Mono<Product> findById(Long productId);
    Mono<Boolean> isExistProductInBranch(String productName, Long branchId);

}
