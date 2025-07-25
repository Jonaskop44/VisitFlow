package com.jonas.visitflow.company;

import com.jonas.visitflow.company.dto.CompanyDto;
import com.jonas.visitflow.company.dto.CreateCompanyDto;
import com.jonas.visitflow.exception.AlreadyExistsException;
import com.jonas.visitflow.exception.NotFoundException;
import com.jonas.visitflow.exception.UnauthorizedException;
import com.jonas.visitflow.model.Address;
import com.jonas.visitflow.model.Company;
import com.jonas.visitflow.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyDto createCompany(CreateCompanyDto companyDto, String userId) {
        //Check if the company domain is already used
        if (companyRepository.existsByDomain(companyDto.getDomain())) {
            throw new AlreadyExistsException("Company with domain " + companyDto.getDomain() + " already exists");
        }

        Address address = Address.builder()
                .street(companyDto.getStreet())
                .city(companyDto.getCity())
                .postalCode(companyDto.getPostalCode())
                .country(companyDto.getCountry())
                .build();

        Company company = Company.builder()
                .name(companyDto.getName())
                .description(companyDto.getDescription())
                .domain(companyDto.getDomain())
                .userId(userId)
                .address(address)
                .build();

        company = companyRepository.save(company);

        return CompanyDto.fromEntity(company);
    }

    public CompanyDto updateCompany(UUID id, String userId, CreateCompanyDto companyDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        if (!company.getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized to update this customer");
        }

        company.setName(companyDto.getName());
        company.setDescription(companyDto.getDescription());
        company.setDomain(companyDto.getDomain());

        Address address = company.getAddress();
        address.setStreet(companyDto.getStreet());
        address.setCity(companyDto.getCity());
        address.setPostalCode(companyDto.getPostalCode());
        address.setCountry(companyDto.getCountry());

        company = companyRepository.save(company);

        return CompanyDto.fromEntity(company);
    }

    public CompanyDto deleteCompany(UUID id, String userId) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Company not found"));

        if (!company.getUserId().equals(userId)) {
            throw new UnauthorizedException("Unauthorized to delete this company");
        }

        companyRepository.delete(company);
        return CompanyDto.fromEntity(company);
    }

    public CompanyDto getCompanyInfo(UUID id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new NotFoundException("Company not found"));

        return CompanyDto.fromEntity(company);
    }

    public List<CompanyDto> getAllCompanies(String userId) {
        List<Company> companies = companyRepository.findAllByUserId(userId);

        if (companies.isEmpty()) {
            throw new NotFoundException("Company not found");
        }

        return companies.stream()
                .map(CompanyDto::fromEntity)
                .toList();
    }

}
