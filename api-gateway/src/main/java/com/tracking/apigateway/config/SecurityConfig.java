package com.tracking.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain filterChain(
            ServerHttpSecurity serverHttpSecurity,
            AuthenticationEntryPointConfig authenticationEntryPointConfig,
            AccessDeniedHandlerConfig accessDeniedHandlerConfig
    ) throws Exception {
        final String[] PUBLIC_MATCHERS = {
                "/actuator/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/api/v1/users/v3/api-docs",
                "/api/v1/tasks/v3/api-docs",
                "/api/v1/tasks/actuator/**",
                "/api/v1/auth/**",
                "/api/v1/profiles/v3/api-docs"
        };

        serverHttpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange -> exchange
                                .pathMatchers(PUBLIC_MATCHERS).permitAll()
                                .pathMatchers("/api/v1/tasks/**").authenticated()
                                .pathMatchers("/api/v1/profiles/**").authenticated()
                                .pathMatchers("/api/v1/users/**").hasRole("ADMIN")
                                .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesConverter())))
                .exceptionHandling(exceptionSpec -> exceptionSpec
                        .authenticationEntryPoint(authenticationEntryPointConfig)
                        .accessDeniedHandler(accessDeniedHandlerConfig)
                );
        return serverHttpSecurity.build();
    }

    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> grantedAuthoritiesConverter() {
        return Jwt -> {
            List<String> roles = Jwt.getClaimAsStringList("roles");
            System.out.println(roles);
            var authorities = roles.stream().map(SimpleGrantedAuthority::new).toList();
            return Mono.just(new JwtAuthenticationToken(Jwt, authorities));
        };
    }
}


