package com.gangadevi.vinayaka.laddu.repository;

import com.gangadevi.vinayaka.laddu.entity.LadduAuction;
import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LadduAuctionRepository extends JpaRepository<LadduAuction, Long> {
    Optional<LadduAuction> findByFestivalYear(FestivalYear festivalYear);
}
