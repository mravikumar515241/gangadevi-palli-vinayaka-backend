package com.gangadevi.vinayaka.festival.repository;
import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface FestivalYearRepository extends JpaRepository<FestivalYear,Long>{
 Optional<FestivalYear> findByYear(Integer year);
 boolean existsByYear(Integer year);
}