package co.com.franchises.api;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

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

    }


    @Nested
    @DisplayName("PATCH /api/v1/franchise/{id}/name")
    class updateFranchiseNameTests {
        private final String pathTest = "/franchise/{id}/name";

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
    }

    @Nested
    @DisplayName("Errores en FranchiseHandler")
    class FranchiseHandlerErrorTests {

        @Test
        void testCreateFranchiseInvalidValueParamException() throws Exception {
            String franchiseName = "";
            CreateFranchiseDto createFranchiseDto = CreateFranchiseDto.builder().name(franchiseName).build();
            String jsonBody = objectMapper.writeValueAsString(createFranchiseDto);

            when(franchiseServicePort.createFranchise(anyString()))
                    .thenReturn(Mono.error(new InvalidValueParamException(DomainExceptionsMessage.PARAM_REQUIRED)));

            webTestClient.post()
                    .uri("/franchise")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isBadRequest();
        }

        @Test
        void testUpdateFranchiseNameDomainException() {
            Long franchiseId = 1L;
            String newName = "Nombre";
            UpdateFranchiseNameDto updateDto = UpdateFranchiseNameDto.builder().name(newName).build();

            when(franchiseServicePort.updateFranchiseName(franchiseId, newName))
                    .thenReturn(Mono.error(new DomainException(DomainExceptionsMessage.FRANCHISE_CREATION_FAIL)));

            webTestClient.patch()
                    .uri("/franchise/{id}/name", franchiseId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .exchange()
                    .expectStatus().isEqualTo(409);
        }
    }
    /*
    @Test
    void testListenGETUseCase() {
        webTestClient.get()
                .uri("/api/usecase/path")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isEmpty();
                        }
                );
    }

    @Test
    void testListenGETOtherUseCase() {
        webTestClient.get()
                .uri("/api/otherusercase/path")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(userResponse -> {
                            Assertions.assertThat(userResponse).isEmpty();
                        }
                );
    }

*/
}
