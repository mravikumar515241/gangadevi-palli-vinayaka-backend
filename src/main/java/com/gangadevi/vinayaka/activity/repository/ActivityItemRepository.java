package com.gangadevi.vinayaka.activity.repository;

import com.gangadevi.vinayaka.activity.entity.ActivityItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ActivityItemRepository extends JpaRepository<ActivityItem, Long> {

    List<ActivityItem> findByActivityId(Long activityId);

    List<ActivityItem> findByActivityIdOrderByIdAsc(Long activityId);

    long countByActivityId(Long activityId);

    @Query("""
        select coalesce(sum(i.quantity * i.unitCost), 0)
        from ActivityItem i
        where i.activity.id = :activityId
    """)
    BigDecimal sumTotalCostByActivityId(@Param("activityId") Long activityId);

    @Query("""
        select coalesce(sum(i.quantity * i.unitCost), 0)
        from ActivityItem i
        where i.activity.festivalYear.year = :year
    """)
    BigDecimal sumTotalCostByFestivalYear(@Param("year") Integer year);
}
