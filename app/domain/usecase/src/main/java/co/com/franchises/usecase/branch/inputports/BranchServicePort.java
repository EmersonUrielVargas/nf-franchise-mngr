package co.com.franchises.usecase.branch.inputports;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.branch.gateways.BranchPersistencePort;
import co.com.franchises.model.franchise.Franchise;
import co.com.franchises.model.franchise.gateways.FranchisePersistencePort;
import reactor.core.publisher.Mono;

public interface BranchServicePort {

    Mono<Branch> createBranch(Branch newBranch);
}
