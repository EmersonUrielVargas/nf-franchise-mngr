package co.com.franchises.api.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI franchisesOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("API de Gestión de Franquicias")
                        .description("Esta API permite gestionar una red de franquicias, sucursales y productos.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación del Scaffold Clean Architecture")
                        .url("https://bancolombia.github.io/scaffold-clean-architecture/"));
    }
}