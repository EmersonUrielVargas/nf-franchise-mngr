package co.com.franchises.model.branch.gateways;

import co.com.franchises.model.branch.Branch;
import reactor.core.publisher.Mono;

public interface BranchPersistencePort {
    Mono<Branch> createBranch(Branch branch);
    Mono<Boolean> isExistBranchInFranchise(String branchName, Long franchiseId);
}
