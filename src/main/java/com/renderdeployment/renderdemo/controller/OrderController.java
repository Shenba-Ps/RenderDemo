package com.renderdeployment.renderdemo.controller;

import com.renderdeployment.renderdemo.RenderDemoApplication;
import com.renderdeployment.renderdemo.dto.PaymentRequest;
import com.renderdeployment.renderdemo.dto.PaymentResponse;
import com.renderdeployment.renderdemo.services.PaymentClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    Logger logger = LoggerFactory.getLogger(RenderDemoApplication.class);
    @Autowired
    private PaymentClient paymentClient;

    @PostMapping("/pay")
    public ResponseEntity<PaymentResponse> payOrder(@RequestBody PaymentRequest request) {
        logger.info("Hitting controller"+request.toString());
        // Call Payment Service with the request sent from Postman
        PaymentResponse response = paymentClient.createPayment(request);
        return ResponseEntity.ok(response);
    }
}
