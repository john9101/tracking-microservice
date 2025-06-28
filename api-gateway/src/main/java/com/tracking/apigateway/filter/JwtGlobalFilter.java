package com.tracking.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.JwtClaimAccessor;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtGlobalFilter implements GlobalFilter {
    private final ReactiveJwtDecoder jwtDecoder;

    public JwtGlobalFilter(ReactiveJwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = extractToken(exchange);
        List<String> excludedPaths = List.of("/auth", "/v3/api-docs", "/actuator");
        String path = exchange.getRequest().getURI().getPath();
        boolean isExcluded = excludedPaths.stream().anyMatch(path::contains);
        if (isExcluded) {
            return chain.filter(exchange);
        }
        return jwtDecoder.decode(token)
                .map(JwtClaimAccessor::getSubject)
                .flatMap(username -> {
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-Username", username)
                            .build();
                    ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                    return chain.filter(mutatedExchange);
                });
    }

    private String extractToken(ServerWebExchange exchange) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : "";
    }
}
