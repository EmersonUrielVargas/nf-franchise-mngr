package co.com.franchises.api.routers;

import co.com.franchises.api.constants.GeneralConstants;
import co.com.franchises.api.dto.request.CreateFranchiseDto;
import co.com.franchises.api.dto.request.UpdateFranchiseNameDto;
import co.com.franchises.api.dto.response.FranchiseDtoRs;
import co.com.franchises.api.handlers.FranchiseHandler;
import co.com.franchises.api.util.ErrorDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FranchiseRouter {

    @RouterOperations(
        {
            @RouterOperation(
                path = "/api/v1/franchise",
                produces = { MediaType.APPLICATION_JSON_VALUE },
                method = RequestMethod.POST,
                beanClass = FranchiseHandler.class,
                beanMethod = "createFranchise",
                operation = @Operation(
                    operationId = "createFranchise",
                    description = GeneralConstants.FRANCHISE_CREATED_DESC,
                    requestBody = @RequestBody(
                      content = @Content(schema = @Schema(
                              implementation = CreateFranchiseDto.class
                      ))
                    ),
                    responses = {
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_CREATED,
                            description = GeneralConstants.FRANCHISE_CREATED_RESPONSE_DESC,
                            content = @Content(schema = @Schema(implementation = FranchiseDtoRs.class))
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
                path = "/api/v1/franchise/{id}/name",
                method = RequestMethod.PATCH,
                beanClass = FranchiseHandler.class,
                beanMethod = "updateStockProduct",
                operation = @Operation(
                    operationId = "updateStockProduct",
                    description = GeneralConstants.FRANCHISE_UPDATE_NAME_DESC,
                    parameters = {
                        @Parameter(
                            in = ParameterIn.PATH,
                            name = GeneralConstants.PATH_PARAMETER_ID_NAME,
                            description = GeneralConstants.FRANCHISE_ID_UPDATE_NAME_DESC
                        )
                    },
                    requestBody = @RequestBody(
                        content = @Content(schema = @Schema(
                            implementation = UpdateFranchiseNameDto.class
                        ))
                    ),
                    responses = {
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_OK,
                            description = GeneralConstants.FRANCHISE_PARAMS_UPDATED_RESPONSE_DESC,
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = FranchiseDtoRs.class)
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
    public RouterFunction<ServerResponse> franchiseRoutes(FranchiseHandler handler) {
        return route(POST("/franchise"), handler::createFranchise)
                .andRoute(PATCH("/franchise/{id}/name"), handler::updateNameFranchise);
    }
}
