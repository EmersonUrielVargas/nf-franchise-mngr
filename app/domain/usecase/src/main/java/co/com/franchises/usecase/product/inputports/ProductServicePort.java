package co.com.franchises.usecase.product.inputports;

import co.com.franchises.model.product.Product;
import reactor.core.publisher.Mono;

public interface ProductServicePort {

    Mono<Product> createProduct(Product newProduct);
}
