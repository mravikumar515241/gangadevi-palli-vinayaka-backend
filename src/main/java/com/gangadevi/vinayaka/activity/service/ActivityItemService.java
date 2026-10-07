package com.gangadevi.vinayaka.activity.service;

import com.gangadevi.vinayaka.activity.dto.ActivityItemRequest;
import com.gangadevi.vinayaka.activity.dto.ActivityItemResponse;
import com.gangadevi.vinayaka.activity.entity.Activity;
import com.gangadevi.vinayaka.activity.entity.ActivityItem;
import com.gangadevi.vinayaka.activity.repository.ActivityItemRepository;
import com.gangadevi.vinayaka.activity.repository.ActivityRepository;
import com.gangadevi.vinayaka.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityItemService {
    private final ActivityItemRepository itemRepository;
    private final ActivityRepository activityRepository;

    @Transactional(readOnly = true)
    public List<ActivityItemResponse> findByActivity(Long activityId) {
        getActivity(activityId);
        return itemRepository.findByActivityIdOrderByIdAsc(activityId)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ActivityItemResponse findById(Long id) {
        return toResponse(getItem(id));
    }

    public ActivityItemResponse create(Long activityId, ActivityItemRequest request) {
        Activity activity = getActivity(activityId);
        ActivityItem item = ActivityItem.builder()
                .activity(activity)
                .itemName(request.itemName().trim())
                .quantity(request.quantity())
                .unit(request.unit().trim())
                .unitCost(request.unitCost())
                .notes(normalize(request.notes()))
                .build();
        return toResponse(itemRepository.save(item));
    }

    public ActivityItemResponse update(Long id, ActivityItemRequest request) {
        ActivityItem item = getItem(id);
        item.setItemName(request.itemName().trim());
        item.setQuantity(request.quantity());
        item.setUnit(request.unit().trim());
        item.setUnitCost(request.unitCost());
        item.setNotes(normalize(request.notes()));
        return toResponse(itemRepository.save(item));
    }

    public void delete(Long id) {
        getItem(id);
        itemRepository.deleteById(id);
    }

    private Activity getActivity(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + id));
    }

    private ActivityItem getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity item not found: " + id));
    }

    private ActivityItemResponse toResponse(ActivityItem item) {
        BigDecimal total = item.getQuantity().multiply(item.getUnitCost()).setScale(2);
        return new ActivityItemResponse(
                item.getId(),
                item.getActivity().getId(),
                item.getItemName(),
                item.getQuantity(),
                item.getUnit(),
                item.getUnitCost(),
                total,
                item.getNotes(),
                item.getCreatedAt(),
                item.getUpdatedAt());
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }
}
