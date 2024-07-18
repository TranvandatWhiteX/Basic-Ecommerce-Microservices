package com.dattran.api_gateway.services;

import com.dattran.api_gateway.dtos.IntrospectDTO;
import com.dattran.api_gateway.repositories.IdentityClient;
import com.dattran.api_gateway.responses.ApiResponse;
import com.dattran.api_gateway.responses.IntrospectResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(IntrospectDTO introspectDTO) {
        return identityClient.introspect(introspectDTO);
    }
}
