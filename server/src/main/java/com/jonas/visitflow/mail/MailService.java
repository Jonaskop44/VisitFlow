package com.jonas.visitflow.mail;

import com.jonas.visitflow.model.enums.OrderStatus;
import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.model.message.Message;
import com.mailgun.util.EmailUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailgunMessagesApi mailgunMessagesApi;

    @Value("${mailgun.domain}")
    private String domain;

    @Value("${mailgun.from}")
    private String fromEmail;

    private void sendTemplateMail(String toEmail, String toName, String subject, String templateName, Map<String, Object> variables) {
        Message message = Message.builder()
                .from(fromEmail)
                .to(EmailUtil.nameWithEmail(toName, toEmail))
                .subject(subject)
                .template(templateName)
                .mailgunVariables(variables)
                .build();

        try {
            mailgunMessagesApi.sendMessage(domain, message);
        } catch (FeignException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    public void sendOrderConfirmation(
            String toEmail,
            String toName,
            String subject,
            String customerName,
            String companyName
    ) {
        Map<String, Object> vars = Map.of(
                "customer_name", customerName,
                "company_name", companyName
        );

        sendTemplateMail(toEmail, toName, subject, "order-confirmation", vars);
    }

    public void sendOrderStatusUpdate(
            String toEmail,
            String toName,
            String subject,
            String customerName,
            String companyName,
            OrderStatus status
    ) {
        Map<String, Object> vars = Map.of(
                "customer_name", customerName,
                "company_name", companyName,
                "order_status", translateOrderStatus(status)
        );

        sendTemplateMail(toEmail, toName, subject, "order-status-update", vars);
    }

    public void sendInvoicePaymentRequest(
            String toEmail,
            String toName,
            String subject,
            String customerName,
            String companyName,
            String paymentLink
    ) {
        Map<String, Object> vars = Map.of(
                "customer_name", customerName,
                "company_name", companyName,
                "payment_link", paymentLink
        );

        sendTemplateMail(toEmail, toName, subject, "invoice-payment-request", vars);
    }

    public void sendInvoicePaymentSuccess(
            String toEmail,
            String toName,
            String subject,
            String customerName,
            String companyName,
            String resourceLink
    ) {
        Map<String, Object> vars = Map.of(
                "customer_name", customerName,
                "company_name", companyName,
                "resource_link", resourceLink
        );

        sendTemplateMail(toEmail, toName, subject, "invoice-payment-success", vars);
    }

    public void sendInvoicePaymentFailure(
            String toEmail,
            String toName,
            String subject,
            String customerName,
            String companyName
    ) {
        Map<String, Object> vars = Map.of(
                "customer_name", customerName,
                "company_name", companyName
        );

        sendTemplateMail(toEmail, toName, subject, "invoice-payment-failure", vars);
    }

    private String translateOrderStatus(OrderStatus status) {
        return switch (status) {
            case REQUESTED -> "Angefragt";
            case CONFIRMED -> "Bestätigt";
            case CANCELLED -> "Storniert";
        };
    }


}
