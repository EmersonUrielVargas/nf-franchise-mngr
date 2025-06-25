package co.com.franchises.api.mapper;


import co.com.franchises.api.dto.request.CreateBranchDto;
import co.com.franchises.api.dto.request.CreateFranchiseDto;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.franchise.Franchise;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface BranchMapper {
    Branch toBranch(CreateBranchDto createBranchDto);

    @InheritInverseConfiguration
    CreateBranchDto toCreateBranchDto(Branch branch);
}