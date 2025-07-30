package com.jonas.visitflow.stripe;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
@RequiredArgsConstructor
public class StripeController {

    private final StripeWebhookService stripeWebhookService;

    @PostMapping("/webhook/checkout")
    public ResponseEntity<Void> handleCheckoutStripeEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String signatureHeader
    ) {
        boolean processed = stripeWebhookService.handleCheckoutStripeEvent(payload, signatureHeader);

        if(processed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

}
