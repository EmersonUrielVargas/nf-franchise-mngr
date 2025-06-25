package co.com.franchises.usecase.branch.inputports;

import co.com.franchises.model.branch.Branch;
import reactor.core.publisher.Mono;

public interface BranchServicePort {

    Mono<Branch> createBranch(Branch newBranch);
}
