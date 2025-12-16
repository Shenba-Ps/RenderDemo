package com.renderdeployment.renderdemo.dto;

import lombok.Data;

@Data
public class PaymentRequest {

    private Long amount;          // Payment amount in smallest currency unit (e.g., cents)
    private String currency;      // Currency code, e.g., "usd"
    private String referenceId;   // Order ID or booking ID for tracking
    private String serviceName;   // Calling service name, e.g., "ORDER_SERVICE"
    private String description;   // Optional description for the payment

    private String successUrl;    // Optional, URL to redirect on payment success
    private String cancelUrl;
}
