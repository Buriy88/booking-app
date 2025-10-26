package com.example.booking_app.controller;

import com.example.booking_app.dto.PaymentRequestDto;
import com.example.booking_app.dto.PaymentResponseDto;
import com.example.booking_app.service.implementation.PaymentService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Managing payments through Stripe")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    @Operation(summary = "Get payments by id",
        description = "Retrieves payments for a specific user. "
            + "Managers can view any user’s payments, while customers only see their own.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payments retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403",
            description = "Forbidden for non-managers when accessing other users’ payments")
    })
    public List<PaymentResponseDto> getPayments(
        @Parameter(description = "Payment ID") @RequestParam(required = false) Long paymentId) {
        return paymentService.getPayments(paymentId);
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Initiate a new payment",
        description = "Creates a new Stripe Checkout session for the given booking."
            + " Returns a URL where the user can complete the payment.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment session created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid payment request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public PaymentResponseDto createPayment(@RequestBody @Valid PaymentRequestDto request)
        throws StripeException {
        return paymentService.createPayment(request);
    }

    @GetMapping("/success")
    @Operation(summary = "Handle successful payment",
        description = "Stripe redirects to this endpoint after a successful payment. "
            + "Updates the payment status to PAID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment marked as successful"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public PaymentResponseDto paymentSuccess(@RequestParam("session_id") String sessionId)
        throws StripeException {
        return paymentService.handleSuccess(sessionId);
    }

    @GetMapping("/cancel")
    @Operation(summary = "Handle canceled payment",
        description = "Stripe redirects to this endpoint when a payment is canceled. "
            + "Updates the payment status to CANCELED.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Payment marked as canceled"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public PaymentResponseDto paymentCancel(@RequestParam("session_id") String sessionId) {
        return paymentService.handleCancel(sessionId);
    }
}
