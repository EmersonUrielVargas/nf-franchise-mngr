package co.com.franchises.usecase.franchise.inputports;

import co.com.franchises.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseServicePort {

    Mono<Franchise> createFranchise(String name);
}
