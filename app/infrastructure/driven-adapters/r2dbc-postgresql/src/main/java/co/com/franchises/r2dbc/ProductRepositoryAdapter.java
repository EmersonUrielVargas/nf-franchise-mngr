package co.com.franchises.r2dbc;

import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.gateways.ProductPersistencePort;
import co.com.franchises.r2dbc.entity.ProductEntity;
import co.com.franchises.r2dbc.helper.ReactiveAdapterOperations;
import co.com.franchises.r2dbc.repository.ProductRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ProductRepositoryAdapter extends ReactiveAdapterOperations<
    Product,
    ProductEntity,
    Long,
    ProductRepository
> implements ProductPersistencePort {
    public ProductRepositoryAdapter(ProductRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Product.class));
    }


    @Override
    public Mono<Product> createProduct(Product product) {
        return save(product).switchIfEmpty(Mono.empty());
    }

    @Override
    public Mono<Boolean> isExistProductInBranch(String productName, Long branchId) {
        return repository.findByNameAndBranchId(productName, branchId)
                .map(productEntity -> true)
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Product> findById(Long productId) {
        return super.findById(productId)
                .switchIfEmpty(Mono.empty());
    }
}
