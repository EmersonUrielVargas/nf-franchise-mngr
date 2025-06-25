package co.com.franchises.usecase.product.inputports;

import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.ProductRankItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductServicePort {

    Mono<Product> createProduct(Product newProduct);
    Mono<Product> updateStockProduct(Long productId, Integer stock);
    Mono<Void> deleteProduct(Long productId);
    Flux<ProductRankItem> getRankProductsStockByBranch(Long franchiseId);
}
