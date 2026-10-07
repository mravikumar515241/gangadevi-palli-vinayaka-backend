package com.gangadevi.vinayaka.activity.controller;
import com.gangadevi.vinayaka.audit.annotation.Audited;
import com.gangadevi.vinayaka.audit.entity.AuditAction;

import com.gangadevi.vinayaka.activity.dto.ActivityRequest;
import com.gangadevi.vinayaka.activity.dto.ActivityResponse;
import com.gangadevi.vinayaka.activity.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService service;

    @GetMapping("/api/v1/festivals/{year}/activities")
    public List<ActivityResponse> getByFestivalYear(@PathVariable Integer year) {
        return service.findByFestivalYear(year);
    }

    @PostMapping("/api/v1/festivals/{year}/activities")
    @ResponseStatus(HttpStatus.CREATED)
    @Audited(action = AuditAction.CREATE, entityType = "ACTIVITY", description = "Created activity", entityIdFromResult = true)
    public ActivityResponse create(@PathVariable Integer year, @Valid @RequestBody ActivityRequest request) {
        return service.create(year, request);
    }

    @GetMapping("/api/v1/activities/{id}")
    public ActivityResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/api/v1/activities/{id}")
    @Audited(action = AuditAction.UPDATE, entityType = "ACTIVITY", description = "Updated activity", entityIdArgument = 0)
    public ActivityResponse update(@PathVariable Long id, @Valid @RequestBody ActivityRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/api/v1/activities/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Audited(action = AuditAction.DELETE, entityType = "ACTIVITY", description = "Deleted activity", entityIdArgument = 0)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
