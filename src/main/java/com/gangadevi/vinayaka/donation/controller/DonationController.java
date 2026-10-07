package com.gangadevi.vinayaka.donation.controller;
import com.gangadevi.vinayaka.audit.annotation.Audited;
import com.gangadevi.vinayaka.audit.entity.AuditAction;

import com.gangadevi.vinayaka.donation.dto.DonationRequest;
import com.gangadevi.vinayaka.donation.dto.DonationResponse;
import com.gangadevi.vinayaka.donation.service.DonationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DonationController {
    private final DonationService service;

    @GetMapping("/api/v1/festivals/{year}/donations")
    public List<DonationResponse> getByFestivalYear(@PathVariable Integer year) {
        return service.findByFestivalYear(year);
    }

    @PostMapping("/api/v1/festivals/{year}/donations")
    @ResponseStatus(HttpStatus.CREATED)
    @Audited(action = AuditAction.CREATE, entityType = "DONATION", description = "Created donation", entityIdFromResult = true)
    public DonationResponse create(@PathVariable Integer year, @Valid @RequestBody DonationRequest request) {
        return service.create(year, request);
    }

    @GetMapping("/api/v1/donations/{id}")
    public DonationResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/api/v1/donations/{id}")
    @Audited(action = AuditAction.UPDATE, entityType = "DONATION", description = "Updated donation", entityIdArgument = 0)
    public DonationResponse update(@PathVariable Long id, @Valid @RequestBody DonationRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/api/v1/donations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Audited(action = AuditAction.DELETE, entityType = "DONATION", description = "Deleted donation", entityIdArgument = 0)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
