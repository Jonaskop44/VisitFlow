package com.jonas.visitflow.visit;

import com.jonas.visitflow.visit.dto.CreateVisitDto;
import com.jonas.visitflow.visit.dto.VisitDto;
import com.jonas.visitflow.visit.dto.VisitLinkDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping("/create-link")
    public ResponseEntity<VisitLinkDto> createVisitLink(Principal principal) {
        String userId = principal.getName();
        VisitLinkDto visitLinkDto = visitService.createVisitLink(userId);
        return ResponseEntity.ok(visitLinkDto);
    }

    @PostMapping("/{token}/submit")
    public ResponseEntity<VisitDto> createVisit(@PathVariable String token, @RequestBody @Valid CreateVisitDto createVisitDto) {
        VisitDto visitDto = visitService.createVisit(createVisitDto, token);
        return ResponseEntity.ok(visitDto);
    }

}
