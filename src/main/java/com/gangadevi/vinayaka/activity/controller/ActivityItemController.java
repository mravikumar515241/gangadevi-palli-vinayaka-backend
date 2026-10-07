package com.gangadevi.vinayaka.activity.controller;
import com.gangadevi.vinayaka.audit.annotation.Audited;
import com.gangadevi.vinayaka.audit.entity.AuditAction;

import com.gangadevi.vinayaka.activity.dto.ActivityItemRequest;
import com.gangadevi.vinayaka.activity.dto.ActivityItemResponse;
import com.gangadevi.vinayaka.activity.service.ActivityItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityItemController {
    private final ActivityItemService service;

    @GetMapping("/api/v1/activities/{activityId}/items")
    public List<ActivityItemResponse> getByActivity(@PathVariable Long activityId) {
        return service.findByActivity(activityId);
    }

    @PostMapping("/api/v1/activities/{activityId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    @Audited(action = AuditAction.CREATE, entityType = "ACTIVITY_ITEM", description = "Created activity item", entityIdFromResult = true)
    public ActivityItemResponse create(@PathVariable Long activityId,
                                       @Valid @RequestBody ActivityItemRequest request) {
        return service.create(activityId, request);
    }

    @GetMapping("/api/v1/activity-items/{id}")
    public ActivityItemResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/api/v1/activity-items/{id}")
    @Audited(action = AuditAction.UPDATE, entityType = "ACTIVITY_ITEM", description = "Updated activity item", entityIdArgument = 0)
    public ActivityItemResponse update(@PathVariable Long id,
                                       @Valid @RequestBody ActivityItemRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/api/v1/activity-items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Audited(action = AuditAction.DELETE, entityType = "ACTIVITY_ITEM", description = "Deleted activity item", entityIdArgument = 0)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
