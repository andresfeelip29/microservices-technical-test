package com.co.neoristest.transactions.routing;

import com.co.neoristest.transactions.handler.TransactionHandler;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterFunctionConfig {

    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }


    @Bean
    public RouterFunction<ServerResponse> routes(TransactionHandler handler) {
        return RouterFunctions.route(RequestPredicates.GET("/api/v1/transacciones"), handler::findAll)
                .andRoute(RequestPredicates.GET("/api/v1/transacciones/{id}"), handler::findById)
                .andRoute(RequestPredicates.POST("/api/v1/transacciones/"), handler::saveTransaction)
                .andRoute(RequestPredicates.DELETE("/api/v1/transacciones/{id}"), handler::deleteTransaction)
                .andRoute(RequestPredicates.GET("/api/v1/transacciones/filtrado"), handler::filterTransactionForRageDateAndClientId);
    }
}
