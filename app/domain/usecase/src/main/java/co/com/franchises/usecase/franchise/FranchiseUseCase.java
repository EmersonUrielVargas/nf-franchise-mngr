package co.com.franchises.usecase.franchise;

import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityAlreadyExistException;
import co.com.franchises.model.franchise.Franchise;
import co.com.franchises.model.franchise.gateways.FranchisePersistencePort;
import co.com.franchises.model.helper.Validator;
import co.com.franchises.usecase.franchise.inputports.FranchiseServicePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase implements FranchiseServicePort {

    private final FranchisePersistencePort franchisePersistencePort;


    @Override
    public Mono<Franchise> createFranchise(String name) {
        Validator.validateNotNull(name, DomainExceptionsMessage.PARAM_REQUIRED);
        Franchise franchise = Franchise.builder()
                .name(name)
                .build();
        return franchisePersistencePort.findByName(name)
                .flatMap(existingFranchise -> Mono.error(new EntityAlreadyExistException(DomainExceptionsMessage.FRANCHISE_ALREADY_EXIST)))
                .switchIfEmpty(
                        Mono.defer(()->
                                franchisePersistencePort.upsertFranchise(franchise)
                                .switchIfEmpty(Mono.error(new DomainException(DomainExceptionsMessage.FRANCHISE_CREATION_FAIL)))
                        ))
                .cast(Franchise.class);
    }
}
