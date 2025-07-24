package com.jonas.visitflow.visit;

import com.jonas.visitflow.visit.dto.CreateVisitDto;
import com.jonas.visitflow.visit.dto.VisitDto;
import com.jonas.visitflow.visit.dto.VisitLinkDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @PostMapping("/{id}/create-link")
    public ResponseEntity<VisitLinkDto> createVisitLink(Principal principal, @PathVariable Long id) {
        String userId = principal.getName();
        VisitLinkDto visitLinkDto = visitService.createVisitLink(userId, id);
        return ResponseEntity.ok(visitLinkDto);
    }

    @PostMapping("/{token}/submit")
    public ResponseEntity<VisitDto> createVisit(@PathVariable String token, @RequestBody @Valid CreateVisitDto createVisitDto) {
        VisitDto visitDto = visitService.createVisit(createVisitDto, token);
        return ResponseEntity.ok(visitDto);
    }

    @GetMapping("/{id}/all")
    public ResponseEntity<List<VisitDto>> getAllVisits(@PathVariable Long id,
                                                       @RequestParam(required = false)
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                                       @RequestParam(required = false)
                                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                                       Principal principal) {
        String userId = principal.getName();
        List<VisitDto> visits = visitService.getAllVisits(userId, id, start, end);
        return ResponseEntity.ok(visits);
    }

}
