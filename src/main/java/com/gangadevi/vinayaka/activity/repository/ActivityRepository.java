package com.gangadevi.vinayaka.activity.repository;

import com.gangadevi.vinayaka.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByFestivalYearYearOrderByIdAsc(Integer year);

    long countByFestivalYearYear(Integer year);

    @Query("select coalesce(sum(a.plannedBudget), 0) from Activity a where a.festivalYear.year = :year")
    BigDecimal sumPlannedBudgetByYear(@Param("year") Integer year);
}
