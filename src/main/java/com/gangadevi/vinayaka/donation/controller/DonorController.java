package com.gangadevi.vinayaka.donation.controller;
import com.gangadevi.vinayaka.audit.annotation.Audited;
import com.gangadevi.vinayaka.audit.entity.AuditAction;

import com.gangadevi.vinayaka.donation.dto.DonorRequest;
import com.gangadevi.vinayaka.donation.dto.DonorResponse;
import com.gangadevi.vinayaka.donation.service.DonorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/donors")
@RequiredArgsConstructor
public class DonorController {
    private final DonorService service;

    @GetMapping
    public List<DonorResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public DonorResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Audited(action = AuditAction.CREATE, entityType = "DONOR", description = "Created donor", entityIdFromResult = true)
    public DonorResponse create(@Valid @RequestBody DonorRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    @Audited(action = AuditAction.UPDATE, entityType = "DONOR", description = "Updated donor", entityIdArgument = 0)
    public DonorResponse update(@PathVariable Long id, @Valid @RequestBody DonorRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Audited(action = AuditAction.DELETE, entityType = "DONOR", description = "Deleted donor", entityIdArgument = 0)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
