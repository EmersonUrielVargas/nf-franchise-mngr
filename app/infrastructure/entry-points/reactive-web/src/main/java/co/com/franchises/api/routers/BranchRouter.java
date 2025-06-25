package co.com.franchises.api.routers;

import co.com.franchises.api.handlers.BranchHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BranchRouter {
    @Bean
    public RouterFunction<ServerResponse> branchesRoutes(BranchHandler handler) {
        return route(POST("/franchise/{id}/branch"), handler::createBranch);
    }
}
