package co.com.franchises.config;

import co.com.franchises.model.branch.gateways.BranchPersistencePort;
import co.com.franchises.model.franchise.gateways.FranchisePersistencePort;
import co.com.franchises.model.product.gateways.ProductPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public FranchisePersistencePort franchisePersistencePort() {
            return mock(FranchisePersistencePort.class);
        }

        @Bean
        public BranchPersistencePort branchPersistencePort() {
            return mock(BranchPersistencePort.class);
        }

        @Bean
        public ProductPersistencePort productPersistencePort() {
            return mock(ProductPersistencePort.class);
        }
    }
}