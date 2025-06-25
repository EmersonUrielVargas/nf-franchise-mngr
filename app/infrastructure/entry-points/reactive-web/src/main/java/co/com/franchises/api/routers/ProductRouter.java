package co.com.franchises.api.routers;

import co.com.franchises.api.handlers.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouter {
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler handler) {
        return route(POST("/branch/{id}/product"), handler::createProduct)
            .and(route(DELETE("/product/{id}"), handler::deleteProduct))
            .and(route(PATCH("/product/{id}/stock"), handler::updateStockProduct));
    }
}
