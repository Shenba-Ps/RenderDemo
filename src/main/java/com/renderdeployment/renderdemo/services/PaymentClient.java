package com.renderdeployment.renderdemo.services;

import com.renderdeployment.renderdemo.dto.PaymentRequest;
import com.renderdeployment.renderdemo.dto.PaymentResponse;
import com.renderdeployment.renderdemo.security.ServiceJwtGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentClient {

    Logger logger = LoggerFactory.getLogger(PaymentClient.class);
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ServiceJwtGenerator jwtGenerator;

    @Value("${payment.service.url}")
    private String paymentServiceUrl;

    public PaymentResponse createPayment(PaymentRequest request) {
        logger.info("Creating Payment Request");
        // Generate service-to-service JWT
        String jwtToken = jwtGenerator.generateToken();
logger.info("JWT Token: " + jwtToken);
        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Idempotency-Key", request.getReferenceId());

        HttpEntity<PaymentRequest> entity = new HttpEntity<>(request, headers);

        // Call Payment Service
        ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(
                paymentServiceUrl,
                entity,
                PaymentResponse.class
        );

        return response.getBody();
    }
}
