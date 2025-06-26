package co.com.franchises.api.routers;

import co.com.franchises.api.constants.GeneralConstants;
import co.com.franchises.api.dto.request.CreateBranchDto;
import co.com.franchises.api.dto.request.UpdateBranchNameDto;
import co.com.franchises.api.dto.response.BranchDtoRs;
import co.com.franchises.api.handlers.BranchHandler;
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
public class BranchRouter {

     @RouterOperations(
        {
            @RouterOperation(
                path = "/api/v1/franchise/{id}/branch",
                produces = { MediaType.APPLICATION_JSON_VALUE },
                method = RequestMethod.POST,
                beanClass = BranchHandler.class,
                beanMethod = "createBranch",
                operation = @Operation(
                    operationId = "createBranch",
                    description = GeneralConstants.BRANCH_OFFICE_CREATE_DESC,
                    requestBody = @RequestBody(
                      content = @Content(schema = @Schema(
                              implementation = CreateBranchDto.class
                      ))
                    ),
                    parameters = {
                        @Parameter(
                            in = ParameterIn.PATH,
                            name = GeneralConstants.PATH_PARAMETER_ID_NAME,
                            description = GeneralConstants.FRANCHISE_ID_BRANCH_OFFICE_CREATE_DESC
                        )
                    },
                    responses = {
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_CREATED,
                            description = GeneralConstants.BRANCH_OFFICE_CREATED_RESPONSE_DESC,
                            content = @Content(schema = @Schema(implementation = BranchDtoRs.class))
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
                path = "/api/v1/branch/{id}/name",
                method = RequestMethod.PATCH,
                beanClass = BranchHandler.class,
                beanMethod = "updateBranchName",
                operation = @Operation(
                    operationId = "updateBranchName",
                    description = GeneralConstants.BRANCH_OFFICE_UPDATE_NAME_DESC,
                    parameters = {
                        @Parameter(
                            in = ParameterIn.PATH,
                            name = GeneralConstants.PATH_PARAMETER_ID_NAME,
                            description = GeneralConstants.FRANCHISE_ID_BRANCH_OFFICE_UPDATE_NAME_DESC
                        )
                    },
                    requestBody = @RequestBody(
                        content = @Content(schema = @Schema(
                            implementation = UpdateBranchNameDto.class
                        ))
                    ),
                    responses = {
                        @ApiResponse(
                            responseCode = GeneralConstants.HTTP_OK,
                            description = GeneralConstants.BRANCH_OFFICE_PARAMS_UPDATED_RESPONSE_DESC,
                            content = @Content(
                                mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = BranchDtoRs.class)
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
    public RouterFunction<ServerResponse> branchesRoutes(BranchHandler handler) {
        return route(POST("/franchise/{id}/branch"), handler::createBranch)
                .andRoute(PATCH("/branch/{id}/name"), handler::updateBranchName);
    }
}
