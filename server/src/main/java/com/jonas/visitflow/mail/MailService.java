package com.jonas.visitflow.mail;

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

    public void sendOrderConfirmation(String toEmail, String toName, String subject, String customerName, String companyName) {

        Map<String, Object> templateVars = Map.of(
                "customer_name", customerName,
                "company_name", companyName);

        Message message = Message.builder()
                .from(fromEmail)
                .to(EmailUtil.nameWithEmail(toName, toEmail))
                .subject(subject)
                .template("orderconfirmation")
                .mailgunVariables(templateVars)
                .build();

        try {
        mailgunMessagesApi.sendMessage(domain, message);
        } catch (FeignException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

}
