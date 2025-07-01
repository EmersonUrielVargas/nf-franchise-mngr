package co.com.franchises.api;

import co.com.franchises.api.constants.GeneralConstants;
import co.com.franchises.api.dto.request.CreateFranchiseDto;
import co.com.franchises.api.dto.request.UpdateFranchiseNameDto;
import co.com.franchises.api.dto.response.FranchiseDtoRs;
import co.com.franchises.api.handlers.FranchiseHandler;
import co.com.franchises.api.mapper.FranchiseMapper;
import co.com.franchises.api.routers.FranchiseRouter;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.InvalidValueParamException;
import co.com.franchises.model.franchise.Franchise;
import co.com.franchises.usecase.franchise.inputports.FranchiseServicePort;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {FranchiseRouter.class, FranchiseHandler.class})
@WebFluxTest
class FranchiseRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FranchiseServicePort franchiseServicePort;

    @MockitoBean
    private FranchiseMapper franchiseMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("POST /api/v1/franchise")
    class createFranchiseTests {
        private final String pathTest ="/franchise";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new InvalidValueParamException(DomainExceptionsMessage.PARAM_REQUIRED), HttpStatus.BAD_REQUEST.value()),
                Arguments.of(new DomainException(DomainExceptionsMessage.FRANCHISE_CREATION_FAIL), HttpStatus.CONFLICT.value()),
                Arguments.of(new RuntimeException(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testCreateFranchiseSuccessful() throws Exception {
            String franchiseName = "Franchise";
            Long franchiseId = 12L;
            CreateFranchiseDto createFranchiseDto = CreateFranchiseDto.builder()
                    .name(franchiseName)
                    .build();
            String jsonBody = objectMapper.writeValueAsString(createFranchiseDto);
            Franchise franchise = Franchise.builder()
                    .id(franchiseId)
                    .name(franchiseName).build();

            FranchiseDtoRs franchiseDto = FranchiseDtoRs.builder()
                    .id(franchiseId)
                    .name(franchiseName).build();
            when(franchiseServicePort.createFranchise(anyString())).thenReturn(Mono.just(franchise));
            when(franchiseMapper.toFranchiseDtoRs(any())).thenReturn(franchiseDto);

            webTestClient.post()
                    .uri(pathTest)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(FranchiseDtoRs.class)
                    .value(bodyResponse -> {
                                Assertions.assertThat(bodyResponse).isEqualTo(franchiseDto);
                            }
                    );
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testCreateFranchiseErrorCases( Exception exception, Integer statusCodeRs) throws Exception {
            String franchiseName = "";
            CreateFranchiseDto createFranchiseDto = CreateFranchiseDto.builder().name(franchiseName).build();
            String jsonBody = objectMapper.writeValueAsString(createFranchiseDto);

            when(franchiseServicePort.createFranchise(anyString()))
                    .thenReturn(Mono.error(exception));

            webTestClient.post()
                    .uri(pathTest)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }
    }


    @Nested
    @DisplayName("PATCH /api/v1/franchise/{id}/name")
    class updateFranchiseNameTests {
        private final String pathTest = "/franchise/{id}/name";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new InvalidValueParamException(DomainExceptionsMessage.PARAM_REQUIRED), HttpStatus.BAD_REQUEST.value()),
                Arguments.of(new DomainException(DomainExceptionsMessage.FRANCHISE_NOT_FOUND), HttpStatus.CONFLICT.value()),
                Arguments.of(new RuntimeException(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testUpdateFranchiseNameSuccessful() {
            Long franchiseId = 12L;
            String newName = "NuevoNombre";
            UpdateFranchiseNameDto updateDto = UpdateFranchiseNameDto.builder().name(newName).build();
            Franchise franchise = Franchise.builder().id(franchiseId).name(newName).build();
            FranchiseDtoRs franchiseDto = FranchiseDtoRs.builder().id(franchiseId).name(newName).build();

            when(franchiseServicePort.updateFranchiseName(franchiseId, newName)).thenReturn(Mono.just(franchise));
            when(franchiseMapper.toFranchiseDtoRs(franchise)).thenReturn(franchiseDto);

            webTestClient.patch()
                    .uri(pathTest, franchiseId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(FranchiseDtoRs.class)
                    .value(bodyResponse -> {
                        Assertions.assertThat(bodyResponse).isEqualTo(franchiseDto);
                    });
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testUpdateFranchiseNameErrorCases(Exception exception, Integer statusCodeRs) {
            Long franchiseId = 1L;
            String newName = "Nombre";
            UpdateFranchiseNameDto updateDto = UpdateFranchiseNameDto.builder().name(newName).build();

            when(franchiseServicePort.updateFranchiseName(franchiseId, newName))
                    .thenReturn(Mono.error(exception));

            webTestClient.patch()
                    .uri(pathTest, franchiseId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }

    }
}
