package co.com.franchises.api.routers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;

@Configuration
@RequiredArgsConstructor
public class RouterRest {
    private final FranchiseRouter franchiseRouter;
    @Bean
    public RouterFunction<ServerResponse> routerFunction(
            @Qualifier("franchiseRoutes") RouterFunction<ServerResponse> franchiseRoutes,
            @Qualifier("branchesRoutes") RouterFunction<ServerResponse> branchesRoutes
    ) {
        return nest(path("/api/v1"),
            franchiseRoutes.and(branchesRoutes)
        );
    }
}
