package co.com.franchises.r2dbc;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.branch.gateways.BranchPersistencePort;
import co.com.franchises.r2dbc.entity.BranchEntity;
import co.com.franchises.r2dbc.helper.ReactiveAdapterOperations;
import co.com.franchises.r2dbc.repository.BranchRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BranchRepositoryAdapter extends ReactiveAdapterOperations<
    Branch,
    BranchEntity,
    Long,
    BranchRepository
> implements BranchPersistencePort {
    public BranchRepositoryAdapter(BranchRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Branch.class));
    }

    @Override
    public Mono<Branch> upsertBranch(Branch branch) {
        return save(branch).switchIfEmpty(Mono.empty()) ;
    }

    @Override
    public Mono<Boolean> isExistBranchInFranchise(String branchName, Long franchiseId) {
        return repository.findByNameAndFranchiseId(branchName, franchiseId)
                .map(branchEntity -> true)
                .defaultIfEmpty(false);
    }
}
