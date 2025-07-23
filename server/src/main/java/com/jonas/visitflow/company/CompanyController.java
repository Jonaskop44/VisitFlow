package com.jonas.visitflow.company;

import com.jonas.visitflow.company.dto.CompanyDto;
import com.jonas.visitflow.company.dto.CreateCompanyDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

    public final CompanyService companyService;

    @GetMapping()
    public ResponseEntity<List<CompanyDto>> getCompanyInfo(Principal principal) {
        String userId = principal.getName();
        List<CompanyDto> companyDto = companyService.getAllCompanies(userId);
        return ResponseEntity.ok(companyDto);
    }

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

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<CompanyDto> deleteCompany(@PathVariable Long id, Principal principal) {
        String userId = principal.getName();
        CompanyDto companyDto = companyService.deleteCompany(id, userId);
        return ResponseEntity.ok(companyDto);
    }

    @GetMapping("/{token}/info")
    public ResponseEntity<CompanyDto> getCompanyInfoByVisitLink(@PathVariable String token) {
        CompanyDto companyDto = companyService.getCompanyInfoByVisitLink(token);
        return ResponseEntity.ok(companyDto);
    }

}
