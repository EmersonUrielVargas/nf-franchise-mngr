package co.com.franchises.r2dbc.repository;

import co.com.franchises.r2dbc.entity.BranchEntity;
import co.com.franchises.r2dbc.entity.ProductEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long>, ReactiveQueryByExampleExecutor<ProductEntity> {

    Mono<BranchEntity> findByNameAndBranchId(String branchName, Long branchId);

}
