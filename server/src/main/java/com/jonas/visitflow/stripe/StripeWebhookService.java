package com.jonas.visitflow.stripe;

import com.jonas.visitflow.mail.MailService;
import com.jonas.visitflow.repository.InvoiceRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookService {

    private final InvoiceRepository invoiceRepository;
    private final MailService mailService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public boolean handleCheckoutStripeEvent(String payload, String sigHeader) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return false;
        }

        switch (event.getType()) {
            case "checkout.session.completed" -> handleCheckoutCompleted(event);
            case "checkout.session.async_payment_succeeded" -> handleAsyncPaymentSuccess(event);
            case "checkout.session.async_payment_failed" -> handleAsyncPaymentFailure(event);
            case "checkout.session.expired" -> handleCheckoutSessionExpired(event);
            default -> System.out.println("Unhandled event type: " + event.getType());
        }

        return true;
    }

    public void handleCheckoutCompleted(Event event) {
        // Session erfolgreich bezahlt
        System.out.println("✅ Payment completed");

        System.out.println("✅ Payment completed");

        // Event Data in checkout.session casten
        var sessionOpt = event.getDataObjectDeserializer().getObject().filter(obj -> obj instanceof Session).map(obj -> (Session)obj);

        if (sessionOpt.isEmpty()) {
            System.err.println("Event data is not a checkout.session");
            return;
        }

        Session session = sessionOpt.get();

        // Hier: z.B. Invoice anhand Stripe Session ID suchen
        String sessionId = session.getId();
        System.out.println("Session ID: " + sessionId);
    }

    public void handleAsyncPaymentSuccess(Event event) {
        // z.B. Überweisung wurde erfolgreich abgeschlossen
        System.out.println("✅ Async Payment succeeded");
    }

    public void handleAsyncPaymentFailure(Event event) {
        // z.B. Überweisung ist fehlgeschlagen
        System.out.println("❌ Async Payment failed");
        // Optional: Mail an Kunde mit Hinweis
    }

    public void handleCheckoutSessionExpired(Event event) {
        System.out.println("⌛️ Checkout session expired");
    }

}
