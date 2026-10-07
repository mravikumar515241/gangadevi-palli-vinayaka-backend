package com.gangadevi.vinayaka.donation.repository;

import com.gangadevi.vinayaka.donation.entity.Donor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonorRepository extends JpaRepository<Donor, Long> {
}
