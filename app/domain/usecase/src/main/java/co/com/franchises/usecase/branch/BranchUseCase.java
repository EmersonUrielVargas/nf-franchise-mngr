package co.com.franchises.usecase.branch;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.branch.gateways.BranchPersistencePort;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityAlreadyExistException;
import co.com.franchises.model.exceptions.EntityNotFoundException;
import co.com.franchises.model.franchise.gateways.FranchisePersistencePort;
import co.com.franchises.usecase.branch.inputports.BranchServicePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase implements BranchServicePort {

    private final FranchisePersistencePort franchisePersistencePort;
    private final BranchPersistencePort branchPersistencePort;

    @Override
    public Mono<Branch> createBranch(Branch newBranch) {
        return franchisePersistencePort.findById(newBranch.getFranchiseId())
                .switchIfEmpty( Mono.error(new EntityNotFoundException(DomainExceptionsMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                    branchPersistencePort.isExistBranchInFranchise(newBranch.getName(), newBranch.getFranchiseId())
                    .filter(exist -> !exist)
                    .switchIfEmpty(Mono.error(new EntityAlreadyExistException(DomainExceptionsMessage.BRANCH_ALREADY_EXIST)))
                    .flatMap(exist ->
                            Mono.defer(()->
                                branchPersistencePort.createBranch(newBranch)
                                    .switchIfEmpty(Mono.error(new DomainException(DomainExceptionsMessage.BRANCH_CREATION_FAIL)))
                            )
                    )
                );
    }
}
