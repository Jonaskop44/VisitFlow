package com.jonas.visitflow.company;

import com.jonas.visitflow.company.dto.CompanyDto;
import com.jonas.visitflow.company.dto.CreateCompanyDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    public final CompanyService companyService;

    @PostMapping("/create")
    public ResponseEntity<CompanyDto> createCompany(@RequestBody @Valid CreateCompanyDto createCompanyDto, Principal principal) {
        String userId = principal.getName();
        CompanyDto companyDto = companyService.createCompany(createCompanyDto, userId);
        return ResponseEntity.ok(companyDto);
    }

    @PatchMapping("/{id}/update")
    public ResponseEntity<CompanyDto> updateCompany(@PathVariable Long id, @RequestBody @Valid CreateCompanyDto createCompanyDto, Principal principal) {
        String userId = principal.getName();
        CompanyDto companyDto = companyService.updateCompany(id, userId, createCompanyDto);
        return ResponseEntity.ok(companyDto);
    }

    @GetMapping("/{token}/info")
    public ResponseEntity<CompanyDto> getCompanyInfoByVisitLink(@PathVariable String token) {
        CompanyDto companyDto = companyService.getCompanyInfoByVisitLink(token);
        return ResponseEntity.ok(companyDto);
    }

}
