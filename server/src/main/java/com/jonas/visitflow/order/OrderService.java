package com.jonas.visitflow.order;

import com.jonas.visitflow.exception.InvalidOrderDateException;
import com.jonas.visitflow.exception.NotEnabledException;
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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.io.NotActiveException;
import java.time.*;
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

    @Transactional
    public OrderDto createOrder(CreateOrderDto createOrderDto, UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        Product product = productRepository.findById(createOrderDto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        LocalDateTime requestedDate = createOrderDto.getRequestedDateTime();

        Hibernate.initialize(company.getWorkSchedules());
        Hibernate.initialize(company.getVacationDays());

        if(company.getWorkSchedules().isEmpty()) {
            throw new NotEnabledException("Company has no work schedules configured.");
        }

        //Check if the requested date is in the past
        if (requestedDate.isBefore(LocalDateTime.now())) {
            throw new InvalidOrderDateException("Requested date cannot be in the past.");
        }

        //Check if the requested date is a vacation day
        List<VacationDay> vacationDays = company.getVacationDays();
        boolean isVacationDay = vacationDays.stream()
                .anyMatch(vd -> vd.getDate().equals(requestedDate.toLocalDate()));
        if (isVacationDay) {
            throw new InvalidOrderDateException("Orders cannot be placed on vacation days.");
        }

        //Check if the requested date is a valid working day
        DayOfWeek requestedDay = requestedDate.getDayOfWeek();
        WorkSchedule workSchedule = company.getWorkSchedules()
                .stream()
                .filter(ws -> ws.getDayOfWeek() == requestedDay)
                .findFirst()
                .orElseThrow(() -> new InvalidOrderDateException("No work schedule found for requested day."));

        LocalTime startTime = workSchedule.getStartTime();
        LocalTime endTime = workSchedule.getEndTime();
        LocalTime requestedTime = requestedDate.toLocalTime();

        if (requestedTime.isBefore(startTime) || requestedTime.isAfter(endTime)) {
            throw new InvalidOrderDateException("Requested time is outside of working hours.");
        }

        //Check if the max number of orders for the requested date is reached
        LocalDate orderDate = requestedDate.toLocalDate();
        long ordersCount = orderRepository.countByCompanyAndRequestedDateTimeBetween(
                company,
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay()
        );

        if (ordersCount >= workSchedule.getMaxOrdersPerDay()) {
            throw new InvalidOrderDateException("Maximum number of orders for the day reached.");
        }

        //Check the minimum minutes between orders
        List<Order> sameDayOrders = orderRepository.findAllByCompanyAndRequestedDateTimeBetween(
                company,
                orderDate.atStartOfDay(),
                orderDate.plusDays(1).atStartOfDay()
        );

        for(Order order : sameDayOrders) {
            long minutesBetween = Math.abs(Duration.between(order.getRequestedDateTime(), requestedDate).toMinutes());
            if(minutesBetween < workSchedule.getMinMinutesBetweenOrders()) {
                throw new InvalidOrderDateException("There must be at least " + workSchedule.getMinMinutesBetweenOrders() + " minutes between orders.");
            }
        }

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
        String customerEmail = customer.getEmail();

        mailService.sendOrderConfirmation(customerEmail, customerName, "Order Confirmation", customerName, company.getName());

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

        //Send status update email
        String customerName = order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName();
        String customerEmail = order.getCustomer().getEmail();
        String companyName = order.getCompany().getName();
        String status = order.getStatus().toString();

        mailService.sendOrderStatusUpdate(customerEmail, companyName, "Order Status Update", customerName, companyName, OrderStatus.valueOf(status));

        return OrderDto.fromEntity(order);
    }

}
