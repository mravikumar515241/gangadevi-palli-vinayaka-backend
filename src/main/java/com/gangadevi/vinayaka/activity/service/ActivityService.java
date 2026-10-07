package com.gangadevi.vinayaka.activity.service;

import com.gangadevi.vinayaka.activity.dto.ActivityRequest;
import com.gangadevi.vinayaka.activity.dto.ActivityResponse;
import com.gangadevi.vinayaka.activity.entity.Activity;
import com.gangadevi.vinayaka.activity.repository.ActivityItemRepository;
import com.gangadevi.vinayaka.activity.repository.ActivityRepository;
import com.gangadevi.vinayaka.common.exception.ResourceNotFoundException;
import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import com.gangadevi.vinayaka.festival.repository.FestivalYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityItemRepository itemRepository;
    private final FestivalYearRepository festivalYearRepository;

    @Transactional(readOnly = true)
    public List<ActivityResponse> findByFestivalYear(Integer year) {
        FestivalYear festivalYear = getFestivalYear(year);
        return activityRepository.findByFestivalYearYearOrderByIdAsc(festivalYear.getYear())
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ActivityResponse findById(Long id) {
        return toResponse(getActivity(id));
    }

    public ActivityResponse create(Integer year, ActivityRequest request) {
        FestivalYear festivalYear = getFestivalYear(year);
        Activity activity = Activity.builder()
                .festivalYear(festivalYear)
                .name(normalizeRequired(request.name()))
                .teluguName(normalize(request.teluguName()))
                .plannedBudget(request.plannedBudget())
                .description(normalize(request.description()))
                .status(request.status())
                .build();
        return toResponse(activityRepository.save(activity));
    }

    public ActivityResponse update(Long id, ActivityRequest request) {
        Activity activity = getActivity(id);
        activity.setName(normalizeRequired(request.name()));
        activity.setTeluguName(normalize(request.teluguName()));
        activity.setPlannedBudget(request.plannedBudget());
        activity.setDescription(normalize(request.description()));
        activity.setStatus(request.status());
        return toResponse(activityRepository.save(activity));
    }

    public void delete(Long id) {
        Activity activity = getActivity(id);
        itemRepository.deleteAll(itemRepository.findByActivityId(id));
        activityRepository.delete(activity);
    }

    private Activity getActivity(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + id));
    }

    private FestivalYear getFestivalYear(Integer year) {
        return festivalYearRepository.findByYear(year)
                .orElseThrow(() -> new ResourceNotFoundException("Festival year not found: " + year));
    }

    private ActivityResponse toResponse(Activity activity) {
        BigDecimal actualSpent = itemRepository.sumTotalCostByActivityId(activity.getId());
        if (actualSpent == null) actualSpent = BigDecimal.ZERO;
        actualSpent = actualSpent.setScale(2);
        BigDecimal remaining = activity.getPlannedBudget().subtract(actualSpent).setScale(2);
        int itemCount = (int) itemRepository.countByActivityId(activity.getId());
        return new ActivityResponse(
                activity.getId(),
                activity.getFestivalYear().getYear(),
                activity.getName(),
                activity.getTeluguName(),
                activity.getPlannedBudget(),
                actualSpent,
                remaining,
                itemCount,
                activity.getDescription(),
                activity.getStatus(),
                activity.getCreatedAt(),
                activity.getUpdatedAt());
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }

    private String normalizeRequired(String value) {
        return value.trim();
    }
}
