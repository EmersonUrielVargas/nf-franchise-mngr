package co.com.franchises.api.mapper;


import co.com.franchises.api.dto.request.CreateFranchiseDto;
import co.com.franchises.api.dto.response.FranchiseDtoRs;
import co.com.franchises.model.franchise.Franchise;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface FranchiseMapper {
    Franchise toFranchise(CreateFranchiseDto franchiseDTO);

    @InheritInverseConfiguration
    CreateFranchiseDto toCreateFranchiseDto(Franchise franchise);
    FranchiseDtoRs toFranchiseDtoRs(Franchise franchise);
}