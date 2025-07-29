package com.jonas.visitflow.order;

import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.mail.MailService;
import com.jonas.visitflow.model.*;
import com.jonas.visitflow.model.enums.OrderStatus;
import com.jonas.visitflow.order.dto.UpdateOrderDto;
import com.jonas.visitflow.repository.CompanyRepository;
import com.jonas.visitflow.repository.CustomerRepository;
import com.jonas.visitflow.repository.ProductRepository;
import com.jonas.visitflow.repository.OrderRepository;
import com.jonas.visitflow.order.dto.CreateOrderDto;
import com.jonas.visitflow.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;
    private final ProductRepository productRepository;
    private final MailService mailService;

    public OrderDto createOrder(CreateOrderDto createOrderDto, UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        Product product = productRepository.findById(createOrderDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        //Create customer
        Customer customer = Customer.builder()
                .firstName(createOrderDto.getFirstName())
                .lastName(createOrderDto.getLastName())
                .email(createOrderDto.getEmail())
                .phoneNumber(createOrderDto.getPhoneNumber())
                .company(company)
                .build();

        customer = customerRepository.save(customer);

        //Create Address
        Address address = Address.builder()
                .street(createOrderDto.getStreet())
                .city(createOrderDto.getCity())
                .postalCode(createOrderDto.getPostalCode())
                .country(createOrderDto.getCountry())
                .build();

        //Create Order
        Order order = Order.builder()
                .requestedDateTime(createOrderDto.getRequestedDateTime())
                .note(createOrderDto.getNote())
                .status(OrderStatus.REQUESTED)
                .company(company)
                .customer(customer)
                .address(address)
                .product(product)
                .build();

        //Send confirmation email
        String customerName = customer.getFirstName() + " " + customer.getLastName();

        mailService.sendOrderConfirmation(customer.getEmail(), customerName, "Order Confirmation", customerName, company.getName());

        order = orderRepository.save(order);
        return OrderDto.fromEntity(order);
    }

    public List<OrderDto> getAllOrders(String userId, UUID companyId, LocalDate start, LocalDate end) {
        Company company = companyRepository.findByIdAndUserId(companyId, userId)
                .orElseThrow(() -> new NotFoundException("Company not found for the given user"));

        List<Order> orders;

        if(start != null && end != null) {
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(23, 59, 59);

            orders = orderRepository.findByCompanyIdAndRequestedDateTimeBetween(company.getId(), startDateTime, endDateTime);
        } else {
            orders = orderRepository.findByCompanyId(company.getId());
        }

        if (orders.isEmpty()) {
            throw new NotFoundException("No orders found for the given company");
        }

        return orders.stream()
                .map(OrderDto::fromEntity)
                .toList();
    }

    public OrderDto updateOrderStatus(Long orderId, UpdateOrderDto updateOrderDto, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        if (!order.getCompany().getUserId().equals(userId)) {
            throw new NotFoundException("Order not found for the given user");
        }

        order.setStatus(updateOrderDto.getStatus());
        order = orderRepository.save(order);

        return OrderDto.fromEntity(order);
    }

}
