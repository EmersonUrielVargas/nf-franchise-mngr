package co.com.franchises.r2dbc.repository;

import co.com.franchises.model.product.ProductRankItem;
import co.com.franchises.r2dbc.entity.BranchEntity;
import co.com.franchises.r2dbc.entity.ProductEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, Long>, ReactiveQueryByExampleExecutor<ProductEntity> {

    Mono<BranchEntity> findByNameAndBranchId(String branchName, Long branchId);

    @Query("SELECT p.id, p.name, p.stock, br.name as branch_name " +
            "FROM franchises_app.products p " +
            "INNER JOIN (" +
            "    SELECT branch_id, MAX(stock) as max_stock " +
            "    FROM franchises_app.products " +
            "    WHERE branch_id IN (SELECT b.id FROM franchises_app.branches b WHERE b.franchise_id = $1) " +
            "    GROUP BY branch_id" +
            ") rank_br " +
            "ON p.stock = rank_br.max_stock AND p.branch_id = rank_br.branch_id " +
            "INNER JOIN franchises_app.branches br " +
            "ON rank_br.branch_id = br.id " +
            "ORDER BY p.stock, p.name desc")
    Flux<ProductRankItem> getTopProductByBranchForFranchise(Long franchiseId);

}
