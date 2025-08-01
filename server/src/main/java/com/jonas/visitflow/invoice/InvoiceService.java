package com.jonas.visitflow.invoice;

import com.jonas.visitflow.exception.AlreadyExistsException;
import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.exception.UnauthorizedException;
import com.jonas.visitflow.invoice.dto.InvoiceDto;
import com.jonas.visitflow.mail.MailService;
import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.model.Invoice;
import com.jonas.visitflow.model.Order;
import com.jonas.visitflow.model.enums.InvoiceStatus;
import com.jonas.visitflow.repository.CompanyRepository;
import com.jonas.visitflow.repository.InvoiceRepository;
import com.jonas.visitflow.repository.OrderRepository;
import com.jonas.visitflow.stripe.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final CompanyRepository companyRepository;
    private final StripeService stripeService;
    private final MailService mailService;

    @Value("${stripe.success.url}")
    private String successUrl;

    @Value("${stripe.cancel.url}")
    private String cancelUrl;

    public InvoiceDto createInvoice(Long orderId, String userId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new NotFoundException("Order not found"));

        //Check if the order belongs to the user's company
        if(!order.getCompany().getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to create an invoice for this order");
        }

        //Check if an invoice already exists for the order
        if (invoiceRepository.existsByOrder(order)) {
            throw new AlreadyExistsException("An invoice already exists for this order");
        }

        Invoice invoice = Invoice.builder()
                .status(InvoiceStatus.PENDING)
                .order(order)
                .build();

        invoice = invoiceRepository.save(invoice);

        //Stripe Checkout Session creation
        String priceId = order.getProduct().getStripePriceId();
        successUrl = successUrl + "/" + invoice.getToken();

        Map<String, String> checkoutSessionId = stripeService.createCheckoutSession(priceId, successUrl, cancelUrl);

        String url = checkoutSessionId.get("url");
        String sessionId = checkoutSessionId.get("sessionId");

        invoice.setStripeSessionId(sessionId);
        invoice = invoiceRepository.save(invoice);

        //Send mail to customer
        String customerEmail = order.getCustomer().getEmail();
        String customerName = order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName();
        String companyName = order.getCompany().getName();

        mailService.sendInvoicePaymentRequest(customerEmail, customerName, "Invoice Payment Request", customerName, companyName, url);

        return InvoiceDto.fromEntity(invoice);
    }

    public List<InvoiceDto> getAllInvoicesByCompanyId(UUID companyId, String userId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        //Check if the company belongs to the user
        if (!company.getUserId().equals(userId)) {
            throw new UnauthorizedException("You are not authorized to view invoices for this company");
        }

        List<Invoice> invoices = invoiceRepository.findAllByOrderCompany(company);

        return invoices.stream()
                .map(InvoiceDto::fromEntity)
                .collect(Collectors.toList());
    }

}
