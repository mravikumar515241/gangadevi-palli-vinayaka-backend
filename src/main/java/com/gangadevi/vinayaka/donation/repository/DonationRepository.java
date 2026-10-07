package com.gangadevi.vinayaka.donation.repository;

import com.gangadevi.vinayaka.donation.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;

import java.util.List;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByFestivalYearYearOrderByDonationDateDescIdDesc(Integer year);
    boolean existsByDonorId(Long donorId);

    @Query("select coalesce(sum(d.plannedAmount), 0) from Donation d where d.festivalYear.year = :year")
    BigDecimal sumPlannedAmountByYear(@Param("year") Integer year);

    @Query("select coalesce(sum(d.receivedAmount), 0) from Donation d where d.festivalYear.year = :year")
    BigDecimal sumReceivedAmountByYear(@Param("year") Integer year);

    @Query("select count(distinct d.donor.id) from Donation d where d.festivalYear.year = :year")
    long countDistinctDonorsByYear(@Param("year") Integer year);
}
