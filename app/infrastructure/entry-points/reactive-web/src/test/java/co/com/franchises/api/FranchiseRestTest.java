package co.com.franchises.api;

import co.com.franchises.api.dto.request.CreateFranchiseDto;
import co.com.franchises.api.dto.response.FranchiseDtoRs;
import co.com.franchises.api.handlers.FranchiseHandler;
import co.com.franchises.api.mapper.FranchiseMapper;
import co.com.franchises.api.routers.FranchiseRouter;
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
