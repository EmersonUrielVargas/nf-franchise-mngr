package co.com.franchises.api;

import co.com.franchises.api.constants.GeneralConstants;
import co.com.franchises.api.dto.request.CreateBranchDto;
import co.com.franchises.api.dto.request.UpdateBranchNameDto;
import co.com.franchises.api.dto.response.BranchDtoRs;
import co.com.franchises.api.handlers.BranchHandler;
import co.com.franchises.api.mapper.BranchMapper;
import co.com.franchises.api.routers.BranchRouter;
import co.com.franchises.model.branch.Branch;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityNotFoundException;
import co.com.franchises.model.exceptions.InvalidValueParamException;
import co.com.franchises.usecase.branch.inputports.BranchServicePort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {BranchRouter.class, BranchHandler.class})
@WebFluxTest
class BranchRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private BranchServicePort branchServicePort;

    @MockitoBean
    private BranchMapper branchMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("POST /api/v1/franchise/{id}/branch")
    class createBranchTests {
        private final String pathTest = "/franchise/{id}/branch";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new InvalidValueParamException(DomainExceptionsMessage.PARAM_REQUIRED), HttpStatus.BAD_REQUEST.value()),
                Arguments.of(new EntityNotFoundException(DomainExceptionsMessage.FRANCHISE_NOT_FOUND), HttpStatus.NOT_FOUND.value()),
                Arguments.of(new DomainException(DomainExceptionsMessage.BRANCH_CREATION_FAIL), HttpStatus.CONFLICT.value()),
                Arguments.of(new RuntimeException(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testCreateBranchSuccessful() throws Exception {
            Long franchiseId = 10L;
            String branchName = "Sucursal";
            Long branchId = 20L;
            CreateBranchDto createBranchDto = CreateBranchDto.builder().name(branchName).build();
            String jsonBody = objectMapper.writeValueAsString(createBranchDto);

            Branch branch = Branch.builder().id(branchId).name(branchName).franchiseId(franchiseId).build();
            BranchDtoRs branchDtoRs = BranchDtoRs.builder().id(branchId).name(branchName).build();

            when(branchMapper.toBranch(any())).thenReturn(branch);
            when(branchServicePort.createBranch(any())).thenReturn(Mono.just(branch));
            when(branchMapper.toBranchDtoRs(branch)).thenReturn(branchDtoRs);

            webTestClient.post()
                    .uri(pathTest, franchiseId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(BranchDtoRs.class)
                    .value(bodyResponse ->
                        Assertions.assertThat(bodyResponse).isEqualTo(branchDtoRs)
                    );
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testCreateBranchErrorCases(Exception exception, Integer statusCodeRs) throws Exception {
            Long franchiseId = 10L;
            String branchName = "Sucursal";

            CreateBranchDto createBranchDto = CreateBranchDto.builder().name(branchName).build();
            String jsonBody = objectMapper.writeValueAsString(createBranchDto);

            when(branchMapper.toBranch(any())).thenReturn(Branch.builder().build());
            when(branchServicePort.createBranch(any())).thenReturn(Mono.error(exception));

            webTestClient.post()
                    .uri(pathTest, franchiseId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/branch/{id}/name")
    class updateBranchNameTests {
        private final String pathTest = "/branch/{id}/name";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new InvalidValueParamException(DomainExceptionsMessage.PARAM_REQUIRED), HttpStatus.BAD_REQUEST.value()),
                Arguments.of(new EntityNotFoundException(DomainExceptionsMessage.BRANCH_NOT_FOUND), HttpStatus.NOT_FOUND.value()),
                Arguments.of(new DomainException(DomainExceptionsMessage.BRANCH_NAME_ALREADY_EXIST), HttpStatus.CONFLICT.value()),
                Arguments.of(new RuntimeException(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testUpdateBranchNameSuccessful() {
            Long branchId = 20L;
            String newName = "SucursalActualizada";
            UpdateBranchNameDto updateDto = UpdateBranchNameDto.builder().name(newName).build();
            Branch branch = Branch.builder().id(branchId).name(newName).build();
            BranchDtoRs branchDtoRs = BranchDtoRs.builder().id(branchId).name(newName).build();

            when(branchServicePort.updateBranchName(branchId, newName)).thenReturn(Mono.just(branch));
            when(branchMapper.toBranchDtoRs(branch)).thenReturn(branchDtoRs);

            webTestClient.patch()
                    .uri(pathTest, branchId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(BranchDtoRs.class)
                    .value(bodyResponse -> Assertions.assertThat(bodyResponse).isEqualTo(branchDtoRs));
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testUpdateBranchNameErrorCases(Exception exception, Integer statusCodeRs) {
            Long branchId = 20L;
            String branchName = "Sucursal";

            UpdateBranchNameDto updateDto = UpdateBranchNameDto.builder().name(branchName).build();

            when(branchServicePort.updateBranchName(branchId, branchName)).thenReturn(Mono.error(exception));

            webTestClient.patch()
                    .uri(pathTest, branchId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }
    }
}
