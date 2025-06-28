package com.tracking.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.factory.SpringCloudCircuitBreakerFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

//    @Bean
//    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
//        return routeLocatorBuilder.routes()
//                .route("auth-service", r -> r
//                        .path(apiPrefix + "/auth/**")
//                        .filters(filterSpec -> filterSpec
//                                .stripPrefix(2)
//                                .circuitBreaker(config -> config
//                                        .setName("authServiceCircuitBreaker")
//                                        .setFallbackUri("forward:/fallback/Auth")
//                                )
//                        )
//                        .uri("http://localhost:9000")
//                )
//                .route("user-service", predicateSpec -> predicateSpec
//                        .path(apiPrefix + "/users/**")
//                        .filters(filterSpec -> filterSpec
//                                .stripPrefix(2)
//                                .circuitBreaker(config -> config
//                                        .setName("userServiceCircuitBreaker")
//                                        .setFallbackUri("forward:/fallback/User")
//                                )
//                        )
//                        .uri("http://localhost:8080")
//                )
//                .route("task-service", predicateSpec -> predicateSpec
//                        .path(apiPrefix + "/tasks/**")
//                        .filters(filterSpec -> filterSpec
//                                .stripPrefix(2)
//                                .circuitBreaker(config -> config
//                                        .setName("taskServiceCircuitBreaker")
//                                        .setFallbackUri("forward:/fallback/Task")
//                                )
//                        )
//                        .uri("http://localhost:8081")
//                )
//                .build();
//    }
}
