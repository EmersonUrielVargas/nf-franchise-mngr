package co.com.franchises.model.branch.gateways;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface BranchPersistencePort {
    Mono<Branch> createBranch(Branch branch);
    Mono<Boolean> isExistBranchInFranchise(String branchName, Long franchiseId);
    Mono<Branch> findById(Long branchId);
}
