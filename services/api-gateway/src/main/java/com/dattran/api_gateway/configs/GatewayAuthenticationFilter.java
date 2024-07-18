package com.dattran.api_gateway.configs;

import com.dattran.api_gateway.dtos.IntrospectDTO;
import com.dattran.api_gateway.responses.ApiResponse;
import com.dattran.api_gateway.services.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class GatewayAuthenticationFilter implements GlobalFilter, Ordered {
    IdentityService identityService;
    ObjectMapper objectMapper;

    @NonFinal
    private String[] publicEndpoints = {
            "/accounts", "/accounts/verify", "/auth/login"
    };

    @Value("${api.prefix}")
    @NonFinal
    private String apiPrefix;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Enter gateway authentication filter.....");
        log.info("Request path: {}", exchange.getRequest().getPath());
        if (isPublicEndpoint(exchange.getRequest()))
            return chain.filter(exchange);
        // Get token
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) return unauthenticated(exchange.getResponse());
        String token = authHeader.get(0).replace("Bearer ", "");
        log.info("Token: {}", token);
        IntrospectDTO introspectDTO = IntrospectDTO.builder().token(token).build();
        return identityService.introspect(introspectDTO).flatMap(introspectResponse -> {
            if (introspectResponse.getResult().isValid()) return chain.filter(exchange);
            return unauthenticated(exchange.getResponse());
        });
    }

    private boolean isPublicEndpoint(ServerHttpRequest request){
        return Arrays.stream(publicEndpoints)
                .anyMatch(s -> request.getURI().getPath().matches(apiPrefix + s));
    }

    private Mono<Void> unauthenticated(ServerHttpResponse serverHttpResponse) {
        ApiResponse<?> response = ApiResponse.builder()
                .message("Unauthenticated.")
                .timestamp(LocalDateTime.now().toString())
                .status(HttpStatus.UNAUTHORIZED)
                .build();
        String body;
        try {
            body = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
        serverHttpResponse.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return serverHttpResponse.writeWith(
                Mono.just(serverHttpResponse.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
