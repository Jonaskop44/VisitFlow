package com.jonas.visitflow.stripe;

import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.mail.MailService;
import com.jonas.visitflow.model.Invoice;
import com.jonas.visitflow.model.enums.InvoiceStatus;
import com.jonas.visitflow.repository.InvoiceRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookService {

    private final InvoiceRepository invoiceRepository;
    private final MailService mailService;
    private final StripeService stripeService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

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
        var sessionOpt = event.getDataObjectDeserializer().getObject().filter(obj -> obj instanceof Session).map(obj -> (Session)obj);

        if (sessionOpt.isEmpty()) {
            System.err.println("Event data is not a checkout.session");
            return;
        }

        Session session = sessionOpt.get();
        String sessionId = session.getId();

        Invoice invoice = invoiceRepository.findByStripeSessionId(sessionId)
                .orElseThrow(() -> new NotFoundException("No invoice found for session ID: " + sessionId));

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(LocalDateTime.now());
        invoice.setStripePaymentId(session.getPaymentIntent());
        invoiceRepository.save(invoice);
    }

    public void handleAsyncPaymentSuccess(Event event) {
        var sessionOpt = event.getDataObjectDeserializer().getObject().filter(obj -> obj instanceof Session).map(obj -> (Session)obj);

        if (sessionOpt.isEmpty()) {
            System.err.println("Event data is not a checkout.session");
            return;
        }

        Session session = sessionOpt.get();
        String sessionId = session.getId();

        Invoice invoice = invoiceRepository.findByStripeSessionId(sessionId)
                .orElseThrow(() -> new NotFoundException("No invoice found for session ID: " + sessionId));

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPaidAt(LocalDateTime.now());
        invoice.setStripePaymentId(session.getPaymentIntent());
        invoiceRepository.save(invoice);

        String email = invoice.getOrder().getCustomer().getEmail();
        String name = invoice.getOrder().getCustomer().getFirstName() + " " + invoice.getOrder().getCustomer().getLastName();
        String company = invoice.getOrder().getCompany().getName();

        String priceId = invoice.getOrder().getProduct().getStripePriceId();
        successUrl = successUrl + "/" + invoice.getToken();

        mailService.sendInvoicePaymentSuccess(email, name, "Payment successful", name, company, successUrl);
     }

    public void handleAsyncPaymentFailure(Event event) {
        var sessionOpt = event.getDataObjectDeserializer().getObject().filter(obj -> obj instanceof Session).map(obj -> (Session)obj);

        if (sessionOpt.isEmpty()) {
            System.err.println("Event data is not a checkout.session");
            return;
        }

        Session session = sessionOpt.get();
        String sessionId = session.getId();

        Invoice invoice = invoiceRepository.findByStripeSessionId(sessionId)
                .orElseThrow(() -> new NotFoundException("No invoice found for session ID: " + sessionId));

        invoice.setStatus(InvoiceStatus.FAILED);
        invoiceRepository.save(invoice);

        String email = invoice.getOrder().getCustomer().getEmail();
        String name = invoice.getOrder().getCustomer().getFirstName() + " " + invoice.getOrder().getCustomer().getLastName();
        String company = invoice.getOrder().getCompany().getName();

        mailService.sendInvoicePaymentFailure(email, name, "Payment failed", name, company);
    }

    public void handleCheckoutSessionExpired(Event event) {
        var sessionOpt = event.getDataObjectDeserializer().getObject().filter(obj -> obj instanceof Session).map(obj -> (Session)obj);

        if (sessionOpt.isEmpty()) {
            System.err.println("Event data is not a checkout.session");
            return;
        }

        Session session = sessionOpt.get();
        String sessionId = session.getId();

        Invoice invoice = invoiceRepository.findByStripeSessionId(sessionId).
                orElseThrow(() -> new NotFoundException("No invoice found for session ID: " + sessionId));


        String email = invoice.getOrder().getCustomer().getEmail();
        String name = invoice.getOrder().getCustomer().getFirstName() + " " + invoice.getOrder().getCustomer().getLastName();
        String company = invoice.getOrder().getCompany().getName();

        String priceId = invoice.getOrder().getProduct().getStripePriceId();
        successUrl = successUrl + "/" + invoice.getToken();

        Map<String, String> sessionData = stripeService.createCheckoutSession(priceId, successUrl, cancelUrl);
        String url = sessionData.get("url");
        String newSessionId = sessionData.get("sessionId");

        invoice.setStripeSessionId(newSessionId);
        invoice.setStatus(InvoiceStatus.PENDING);
        invoiceRepository.save(invoice);

        mailService.sendInvoicePaymentRequest(email, name, "Pay again", name, company, url);
    }

}
