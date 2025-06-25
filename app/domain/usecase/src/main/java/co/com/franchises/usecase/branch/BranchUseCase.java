package co.com.franchises.usecase.branch;

import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.branch.gateways.BranchPersistencePort;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityAlreadyExistException;
import co.com.franchises.model.exceptions.EntityNotFoundException;
import co.com.franchises.model.franchise.gateways.FranchisePersistencePort;
import co.com.franchises.model.helper.Validator;
import co.com.franchises.usecase.branch.inputports.BranchServicePort;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase implements BranchServicePort {

    private final FranchisePersistencePort franchisePersistencePort;
    private final BranchPersistencePort branchPersistencePort;

    @Override
    public Mono<Branch> createBranch(Branch newBranch) {
        Validator.validateNotNull(newBranch.getName(), DomainExceptionsMessage.PARAM_REQUIRED);
        Validator.validateNotNull(newBranch.getFranchiseId(), DomainExceptionsMessage.PARAM_REQUIRED);
        return franchisePersistencePort.findById(newBranch.getFranchiseId())
                .switchIfEmpty( Mono.error(new EntityNotFoundException(DomainExceptionsMessage.FRANCHISE_NOT_FOUND)))
                .flatMap(franchise ->
                    branchPersistencePort.isExistBranchInFranchise(newBranch.getName(), newBranch.getFranchiseId())
                    .filter(exist -> !exist)
                    .switchIfEmpty(Mono.error(new EntityAlreadyExistException(DomainExceptionsMessage.BRANCH_ALREADY_EXIST)))
                    .flatMap(exist ->
                            Mono.defer(()->
                                branchPersistencePort.upsertBranch(newBranch)
                                    .switchIfEmpty(Mono.error(new DomainException(DomainExceptionsMessage.BRANCH_CREATION_FAIL)))
                            )
                    )
                );
    }

    @Override
    public Mono<Branch> updateBranchName(Long branchId, String name) {
        Validator.validateNotNull(name, DomainExceptionsMessage.PARAM_REQUIRED);
        Validator.validateNotNull(branchId, DomainExceptionsMessage.PARAM_REQUIRED);
        return  branchPersistencePort.findById(branchId)
                .switchIfEmpty( Mono.error(new EntityNotFoundException(DomainExceptionsMessage.BRANCH_NOT_FOUND)))
                .flatMap(branchFound ->
                    branchPersistencePort.isExistBranchInFranchise(name, branchFound.getFranchiseId())
                    .filter(exist -> !exist)
                    .switchIfEmpty(Mono.error(new EntityAlreadyExistException(DomainExceptionsMessage.BRANCH_NAME_ALREADY_EXIST)))
                    .flatMap(exist ->
                            Mono.defer(()-> {
                                Branch branchUpdated = Branch.builder()
                                        .id(branchFound.getId())
                                        .franchiseId(branchFound.getFranchiseId())
                                        .name(name)
                                        .build();
                                return branchPersistencePort.upsertBranch(branchUpdated)
                                        .switchIfEmpty(Mono.error(new DomainException(DomainExceptionsMessage.BRANCH_CREATION_FAIL)));
                            })
                    )
                );
    }
}
