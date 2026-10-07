package com.gangadevi.vinayaka.laddu.repository;

import com.gangadevi.vinayaka.laddu.entity.InterestSlab;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InterestSlabRepository extends JpaRepository<InterestSlab, Long> {
    List<InterestSlab> findByActiveTrueOrderByMinAmountAsc();
}
