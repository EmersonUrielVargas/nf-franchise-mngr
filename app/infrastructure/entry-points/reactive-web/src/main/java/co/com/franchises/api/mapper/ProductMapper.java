package co.com.franchises.api.mapper;


import co.com.franchises.api.dto.request.CreateProductDto;
import co.com.franchises.api.dto.request.UpdateStockProductDto;
import co.com.franchises.api.dto.response.ProductDtoRs;
import co.com.franchises.model.product.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    Product toProduct(CreateProductDto createProductDto);

    @InheritInverseConfiguration
    CreateProductDto toCreateProductDto(Product product);
    ProductDtoRs toProductDto(Product product);
}