package co.com.franchises.r2dbc.repository;

import co.com.franchises.r2dbc.entity.BranchEntity;
import co.com.franchises.r2dbc.entity.FranchiseEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BranchRepository extends ReactiveCrudRepository<BranchEntity, Long>, ReactiveQueryByExampleExecutor<BranchEntity> {

    Mono<BranchEntity> findByNameAndFranchiseId(String branchName, Long franchiseId);

}
