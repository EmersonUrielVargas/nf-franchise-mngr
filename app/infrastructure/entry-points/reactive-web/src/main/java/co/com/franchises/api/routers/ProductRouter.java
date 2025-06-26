package co.com.franchises.api.routers;

import co.com.franchises.api.constants.GeneralConstants;
import co.com.franchises.api.dto.request.CreateProductDto;
import co.com.franchises.api.dto.request.UpdateProductNameDto;
import co.com.franchises.api.dto.request.UpdateStockProductDto;
import co.com.franchises.api.dto.response.ProductDtoRs;
import co.com.franchises.api.handlers.ProductHandler;
import co.com.franchises.api.util.ErrorDto;
import co.com.franchises.model.product.ProductRankItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouter {

    @RouterOperations(
        {
            @RouterOperation(
                path = "/api/v1/branch/{id}/product",
                produces = { MediaType.APPLICATION_JSON_VALUE },
                method = RequestMethod.POST,
                beanClass = ProductHandler.class,
                beanMethod = "createProduct",
                operation = @Operation(
                    operationId = "createProduct",
                    description = GeneralConstants.PRODUCT_CREATE_DESC,
                    parameters = {
                        @Parameter(
                            in = ParameterIn.PATH,
                            name = GeneralConstants.PATH_PARAMETER_ID_NAME,
                            description = GeneralConstants.BRANCH_ID_BRANCH_OFFICE_CREATE_DESC
                        )
                    },
                    requestBody = @RequestBody(
                      content = @Content(schema = @Schema(
                              implementation = CreateProductDto.class
                      ))
                    ),
                    responses = {
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_CREATED,
                            description = GeneralConstants.PRODUCT_CREATED_RESPONSE_DESC,
                            content = @Content(schema = @Schema(implementation = ProductDtoRs.class))
                        ),
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_CONFLICT,
                            description = GeneralConstants.ERROR_CONFLICT_RESPONSE_DESC,
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorDto.class)
                            )
                        )
                    }
                )

            ),
            @RouterOperation(
                path = "/api/v1/product/{id}",
                method = RequestMethod.DELETE,
                beanClass = ProductHandler.class,
                beanMethod = "deleteProduct",
                operation = @Operation(
                    operationId = "deleteProduct",
                    description = GeneralConstants.PRODUCT_DELETE_DESC,
                    parameters = {
                        @Parameter(
                            in = ParameterIn.PATH,
                            name = GeneralConstants.PATH_PARAMETER_ID_NAME,
                            description = GeneralConstants.PRODUCT_ID_DELETE_DESC
                        )
                    },
                    responses = {
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_OK,
                            description = GeneralConstants.PRODUCT_DELETE_RESPONSE_DESC,
                            content = @Content()
                        ),
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_CONFLICT,
                            description = GeneralConstants.ERROR_CONFLICT_RESPONSE_DESC,
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorDto.class)
                            )
                        )
                    }
                )

            ),
            @RouterOperation(
                path = "/api/v1/product/{id}/stock",
                method = RequestMethod.PATCH,
                beanClass = ProductHandler.class,
                beanMethod = "updateStockProduct",
                operation = @Operation(
                    operationId = "updateStockProduct",
                    description = GeneralConstants.PRODUCT_UPDATE_STOCK_DESC,
                    parameters = {
                        @Parameter(
                            in = ParameterIn.PATH,
                            name = GeneralConstants.PATH_PARAMETER_ID_NAME,
                            description = GeneralConstants.PRODUCT_ID_UPDATE_PARAMS_DESC
                        )
                    },
                    requestBody = @RequestBody(
                        content = @Content(schema = @Schema(
                            implementation = UpdateStockProductDto.class
                        ))
                    ),
                    responses = {
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_OK,
                            description = GeneralConstants.PRODUCT_PARAMS_UPDATED_RESPONSE_DESC,
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ProductDtoRs.class)
                            )
                        ),
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_CONFLICT,
                            description = GeneralConstants.ERROR_CONFLICT_RESPONSE_DESC,
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorDto.class)
                            )
                        )
                    }
                )
            ),
            @RouterOperation(
                path = "/api/v1/franchise/{id}/branch/top-products",
                method = RequestMethod.GET,
                beanClass = ProductHandler.class,
                beanMethod = "getTopProductsByBranchInFranchise",
                operation = @Operation(
                    operationId = "getTopProductsByBranchInFranchise",
                    description = GeneralConstants.PRODUCT_RANK_STOCK_BY_OFFICE_BRANCH_DESC,
                    parameters = {
                        @Parameter(
                            in = ParameterIn.PATH,
                            name = GeneralConstants.PATH_PARAMETER_ID_NAME,
                            description = GeneralConstants.FRANCHISE_ID_RANK_PRODUCTS_PARAMS_DESC
                        )
                    },
                    responses = {
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_OK,
                            description = GeneralConstants.PRODUCT_RANK_STOCK_FRANCHISE_RESPONSE_OK_DESC,
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                array = @ArraySchema(
                                    schema = @Schema(implementation = ProductRankItem.class)
                                )
                            )
                        ),
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_CONFLICT,
                            description = GeneralConstants.ERROR_CONFLICT_RESPONSE_DESC,
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorDto.class)
                            )
                        )
                    }
                )
            ),
            @RouterOperation(
                path = "/api/v1/product/{id}/name",
                method = RequestMethod.PATCH,
                beanClass = ProductHandler.class,
                beanMethod = "updateNameProduct",
                operation = @Operation(
                    operationId = "updateNameProduct",
                    description = GeneralConstants.PRODUCT_UPDATE_NAME_DESC,
                    parameters = {
                        @Parameter(
                            in = ParameterIn.PATH,
                            name = GeneralConstants.PATH_PARAMETER_ID_NAME,
                            description = GeneralConstants.PRODUCT_ID_UPDATE_PARAMS_DESC
                        )
                    },
                    requestBody = @RequestBody(
                        content = @Content(schema = @Schema(
                            implementation = UpdateProductNameDto.class
                        ))
                    ),
                    responses = {
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_OK,
                            description = GeneralConstants.PRODUCT_PARAMS_UPDATED_RESPONSE_DESC,
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                array = @ArraySchema(
                                    schema = @Schema(implementation = ProductDtoRs.class)
                                )
                            )
                        ),
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_CONFLICT,
                            description = GeneralConstants.ERROR_CONFLICT_RESPONSE_DESC,
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorDto.class)
                            )
                        )
                    }
                )
            )
        }
    )
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {

        return route(POST("/branch/{id}/product"), handler::createProduct)
            .and(route(DELETE("/product/{id}"), handler::deleteProduct))
            .and(route(PATCH("/product/{id}/stock"), handler::updateStockProduct))
            .and(route(GET("/franchise/{id}/branch/top-products"), handler::getTopProductsByBranchInFranchise))
            .and(route(PATCH("/product/{id}/name"), handler::updateNameProduct));
    }
}
