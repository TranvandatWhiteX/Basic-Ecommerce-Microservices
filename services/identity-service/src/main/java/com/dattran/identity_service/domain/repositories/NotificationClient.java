package com.dattran.identity_service.domain.repositories;

import com.dattran.identity_service.app.requests.EmailRequest;
import com.dattran.identity_service.app.responses.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "${services.notification}")
public interface NotificationClient {
    @PostMapping(value = "/notifications/email", produces = MediaType.APPLICATION_JSON_VALUE)
    ApiResponse<Boolean> sendEmail(@RequestBody EmailRequest emailRequest);
}
