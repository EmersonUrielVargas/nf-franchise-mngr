package co.com.franchises.model.franchise.gateways;

import co.com.franchises.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchisePersistencePort {

    Mono<Franchise> createFranchise(Franchise franchise);
    Mono<Franchise> findByName(String franchiseName);
    Mono<Franchise> findById(Long franchiseId);
}
