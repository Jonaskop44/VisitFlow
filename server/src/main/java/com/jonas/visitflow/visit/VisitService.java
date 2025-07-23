package com.jonas.visitflow.visit;

import com.jonas.visitflow.company.dto.CompanyDto;
import com.jonas.visitflow.exception.LinkAlreadyUsedException;
import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.model.*;
import com.jonas.visitflow.repository.CompanyRepository;
import com.jonas.visitflow.repository.CustomerRepository;
import com.jonas.visitflow.repository.VisitLinkRepository;
import com.jonas.visitflow.repository.VisitRepository;
import com.jonas.visitflow.visit.dto.CreateVisitDto;
import com.jonas.visitflow.visit.dto.VisitDto;
import com.jonas.visitflow.visit.dto.VisitLinkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitLinkRepository visitLinkRepository;
    private final VisitRepository visitRepository;
    private final CustomerRepository customerRepository;
    private final CompanyRepository companyRepository;

    public VisitLinkDto createVisitLink(String userId) {
        VisitLink visitLink = VisitLink.builder()
                .userId(userId)
                .build();

        return VisitLinkDto.fromEntity(visitLinkRepository.save(visitLink));
    }

    public VisitDto createVisit(CreateVisitDto createVisitDto, String token) {
        VisitLink visitLink = visitLinkRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Invalid or expired visit link"));

        // Validate that the visit link is not expired
        if (visitLink.isUsed() || visitLink.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new LinkAlreadyUsedException("Invalid or expired visit link");
        }

        //Create customer
        Customer customer = Customer.builder()
                .firstName(createVisitDto.getFirstName())
                .lastName(createVisitDto.getLastName())
                .email(createVisitDto.getEmail())
                .phoneNumber(createVisitDto.getPhoneNumber())
                .userId(visitLink.getUserId())
                .build();

        customer = customerRepository.save(customer);

        //Create Address
        Address address = Address.builder()
                .street(createVisitDto.getStreet())
                .city(createVisitDto.getCity())
                .postalCode(createVisitDto.getPostalCode())
                .country(createVisitDto.getCountry())
                .build();

        //Create Visit
        Visit visit = Visit.builder()
                .customer(customer)
                .address(address)
                .requestedDateTime(createVisitDto.getRequestedDateTime())
                .note(createVisitDto.getNote())
                .build();

        visitLink.setUsed(true);
        visitLinkRepository.save(visitLink);
        visit = visitRepository.save(visit);

        return VisitDto.fromEntity(visit);
    }

}
