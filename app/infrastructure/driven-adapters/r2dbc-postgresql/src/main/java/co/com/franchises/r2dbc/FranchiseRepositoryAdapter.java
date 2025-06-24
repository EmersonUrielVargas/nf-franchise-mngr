package co.com.franchises.r2dbc;

import co.com.franchises.model.franchise.Franchise;
import co.com.franchises.model.franchise.gateways.FranchisePersistencePort;
import co.com.franchises.r2dbc.entity.FranchiseEntity;
import co.com.franchises.r2dbc.helper.ReactiveAdapterOperations;
import co.com.franchises.r2dbc.repository.FranchiseRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class FranchiseRepositoryAdapter extends ReactiveAdapterOperations<
        Franchise,
        FranchiseEntity,
    Long,
        FranchiseRepository
> implements FranchisePersistencePort {
    public FranchiseRepositoryAdapter(FranchiseRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Franchise.class));
    }

    @Override
    public Mono<Franchise> createFranchise(Franchise franchise) {
        return repository.save(toData(franchise))
                .map(this::toEntity);
    }

    @Override
    public Mono<Franchise> findByName(String franchiseName) {
        return repository.findByName(franchiseName)
                .map(this::toEntity)
                .switchIfEmpty(Mono.empty());
    }
}
