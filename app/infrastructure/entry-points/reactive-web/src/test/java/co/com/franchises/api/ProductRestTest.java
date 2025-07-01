package co.com.franchises.api;

import co.com.franchises.api.constants.GeneralConstants;
import co.com.franchises.api.dto.request.CreateProductDto;
import co.com.franchises.api.dto.request.UpdateProductNameDto;
import co.com.franchises.api.dto.request.UpdateStockProductDto;
import co.com.franchises.api.dto.response.ProductDtoRs;
import co.com.franchises.api.handlers.ProductHandler;
import co.com.franchises.api.mapper.ProductMapper;
import co.com.franchises.api.routers.ProductRouter;
import co.com.franchises.model.enums.DomainExceptionsMessage;
import co.com.franchises.model.exceptions.DomainException;
import co.com.franchises.model.exceptions.EntityNotFoundException;
import co.com.franchises.model.exceptions.InvalidValueParamException;
import co.com.franchises.model.product.Product;
import co.com.franchises.model.product.ProductRankItem;
import co.com.franchises.usecase.product.inputports.ProductServicePort;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ContextConfiguration(classes = {ProductRouter.class, ProductHandler.class})
@WebFluxTest
class ProductRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ProductServicePort productServicePort;

    @MockitoBean
    private ProductMapper productMapper;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Nested
    @DisplayName("POST /api/v1/branch/{id}/product")
    class createProductTests {
        private final String pathTest = "/branch/{id}/product";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new InvalidValueParamException(DomainExceptionsMessage.PARAM_REQUIRED), HttpStatus.BAD_REQUEST.value()),
                Arguments.of(new EntityNotFoundException(DomainExceptionsMessage.BRANCH_NOT_FOUND), HttpStatus.NOT_FOUND.value()),
                Arguments.of(new DomainException(DomainExceptionsMessage.PRODUCT_CREATION_FAIL), HttpStatus.CONFLICT.value()),
                Arguments.of(new RuntimeException(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testCreateProductSuccessful() throws Exception {
            Long branchId = 1L;
            String productName = "Producto";
            Long productId = 2L;
            CreateProductDto createProductDto = CreateProductDto.builder().name(productName).build();
            String jsonBody = objectMapper.writeValueAsString(createProductDto);

            Product product = Product.builder().id(productId).name(productName).branchId(branchId).build();
            ProductDtoRs productDtoRs = ProductDtoRs.builder().id(productId).name(productName).build();

            when(productMapper.toProduct(any())).thenReturn(product);
            when(productServicePort.createProduct(any())).thenReturn(Mono.just(product));
            when(productMapper.toProductDto(product)).thenReturn(productDtoRs);

            webTestClient.post()
                    .uri(pathTest, branchId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(ProductDtoRs.class)
                    .value(bodyResponse -> Assertions.assertThat(bodyResponse).isEqualTo(productDtoRs));
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testCreateProductErrorCases(Exception exception, Integer statusCodeRs) throws Exception {
            Long branchId = 1L;
            CreateProductDto createProductDto = CreateProductDto.builder().name("Producto").build();
            String jsonBody = objectMapper.writeValueAsString(createProductDto);

            when(productMapper.toProduct(any())).thenReturn(Product.builder().build());
            when(productServicePort.createProduct(any())).thenReturn(Mono.error(exception));

            webTestClient.post()
                    .uri(pathTest, branchId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonBody)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/product/{id}/stock")
    class updateStockProductTests {
        private final String pathTest = "/product/{id}/stock";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new InvalidValueParamException(DomainExceptionsMessage.PARAM_REQUIRED), HttpStatus.BAD_REQUEST.value()),
                Arguments.of(new EntityNotFoundException(DomainExceptionsMessage.PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND.value()),
                Arguments.of(new DomainException(DomainExceptionsMessage.PRODUCT_UPDATE_FAIL), HttpStatus.CONFLICT.value()),
                Arguments.of(new RuntimeException(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testUpdateStockProductSuccessful() {
            Long productId = 2L;
            int newStock = 100;
            UpdateStockProductDto updateDto = UpdateStockProductDto.builder().stock(newStock).build();
            Product product = Product.builder().id(productId).stock(newStock).build();
            ProductDtoRs productDtoRs = ProductDtoRs.builder().id(productId).stock(newStock).build();

            when(productServicePort.updateStockProduct(productId, newStock)).thenReturn(Mono.just(product));
            when(productMapper.toProductDto(product)).thenReturn(productDtoRs);

            webTestClient.patch()
                    .uri(pathTest, productId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(ProductDtoRs.class)
                    .value(bodyResponse -> Assertions.assertThat(bodyResponse).isEqualTo(productDtoRs));
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testUpdateStockProductErrorCases(Exception exception, Integer statusCodeRs) {
            Long productId = 2L;
            int newStock = 100;
            UpdateStockProductDto updateDto = UpdateStockProductDto.builder().stock(newStock).build();

            when(productServicePort.updateStockProduct(productId, newStock)).thenReturn(Mono.error(exception));

            webTestClient.patch()
                    .uri(pathTest, productId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/product/{id}/name")
    class updateNameProductTests {
        private final String pathTest = "/product/{id}/name";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new InvalidValueParamException(DomainExceptionsMessage.PARAM_REQUIRED), HttpStatus.BAD_REQUEST.value()),
                Arguments.of(new EntityNotFoundException(DomainExceptionsMessage.PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND.value()),
                Arguments.of(new DomainException(DomainExceptionsMessage.PRODUCT_NAME_ALREADY_EXIST), HttpStatus.CONFLICT.value()),
                Arguments.of(new RuntimeException(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testUpdateNameProductSuccessful() {
            Long productId = 2L;
            String newName = "ProductoActualizado";
            UpdateProductNameDto updateDto = UpdateProductNameDto.builder().name(newName).build();
            Product product = Product.builder().id(productId).name(newName).build();
            ProductDtoRs productDtoRs = ProductDtoRs.builder().id(productId).name(newName).build();

            when(productServicePort.updateNameProduct(productId, newName)).thenReturn(Mono.just(product));
            when(productMapper.toProductDto(product)).thenReturn(productDtoRs);

            webTestClient.patch()
                    .uri(pathTest, productId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(ProductDtoRs.class)
                    .value(bodyResponse -> Assertions.assertThat(bodyResponse).isEqualTo(productDtoRs));
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testUpdateNameProductErrorCases(Exception exception, Integer statusCodeRs) {
            Long productId = 2L;
            String newName = "Product";
            UpdateProductNameDto updateDto = UpdateProductNameDto.builder().name(newName).build();

            when(productServicePort.updateNameProduct(productId, newName)).thenReturn(Mono.error(exception));

            webTestClient.patch()
                    .uri(pathTest, productId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(updateDto)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/product/{id}")
    class deleteProductTests {
        private final String pathTest = "/product/{id}";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new InvalidValueParamException(DomainExceptionsMessage.PARAM_REQUIRED), HttpStatus.BAD_REQUEST.value()),
                Arguments.of(new EntityNotFoundException(DomainExceptionsMessage.PRODUCT_NOT_FOUND), HttpStatus.NOT_FOUND.value()),
                Arguments.of(new DomainException(DomainExceptionsMessage.PRODUCT_DELETE_FAIL), HttpStatus.CONFLICT.value()),
                Arguments.of(new RuntimeException(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testDeleteProductSuccessful() {
            Long productId = 2L;

            when(productServicePort.deleteProduct(productId)).thenReturn(Mono.empty());

            webTestClient.delete()
                    .uri(pathTest, productId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNoContent();
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testDeleteProductErrorCases(Exception exception, Integer statusCodeRs) {
            Long productId = 2L;

            when(productServicePort.deleteProduct(productId)).thenReturn(Mono.error(exception));

            webTestClient.delete()
                    .uri(pathTest, productId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }
    }

    @Nested
    @DisplayName("GET /api/v1/franchise/{id}/branch/top-products")
    class getTopProductsByBranchInFranchiseTests {
        private final String pathTest = "/franchise/{id}/branch/top-products";

        static Stream<Arguments> exceptionAndStatusProvider() {
            return Stream.of(
                Arguments.of(new InvalidValueParamException(DomainExceptionsMessage.PARAM_REQUIRED), HttpStatus.BAD_REQUEST.value()),
                Arguments.of(new EntityNotFoundException(DomainExceptionsMessage.FRANCHISE_NOT_FOUND), HttpStatus.NOT_FOUND.value()),
                Arguments.of(new DomainException(DomainExceptionsMessage.FRANCHISE_WITHOUT_BRANCH_OFFICES), HttpStatus.CONFLICT.value()),
                Arguments.of(new RuntimeException(GeneralConstants.DEFAULT_ERROR_MESSAGE_LOG), HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }

        @Test
        void testGetTopProductsByBranchInFranchiseSuccessful() {
            Long franchiseId = 1L;
            String branchName1 = "Calle 23";
            String branchName2 = "Camol";
            String productName1 = "Galletas";
            String productName2 = "Yogurt";
            ProductRankItem item1 = ProductRankItem.builder().name(productName1).branchName(branchName1).id(2L).stock(10).build();
            ProductRankItem item2 = ProductRankItem.builder().name(productName2).branchName(branchName2).id(3L).stock(20).build();
            List<ProductRankItem> items = List.of(item1, item2);

            when(productServicePort.getRankProductsStockByBranch(franchiseId)).thenReturn(Flux.fromIterable(items));

            webTestClient.get()
                    .uri(pathTest, franchiseId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(ProductRankItem.class)
                    .value(bodyResponse ->
                        Assertions.assertThat(bodyResponse)
                        .usingRecursiveFieldByFieldElementComparator()
                        .isEqualTo(items)
                    );
        }

        @ParameterizedTest
        @MethodSource("exceptionAndStatusProvider")
        void testGetTopProductsByBranchInFranchiseErrorCases(Exception exception, Integer statusCodeRs) {
            Long franchiseId = 1L;

            when(productServicePort.getRankProductsStockByBranch(franchiseId)).thenReturn(Flux.error(exception));

            webTestClient.get()
                    .uri(pathTest, franchiseId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isEqualTo(statusCodeRs);
        }
    }
}