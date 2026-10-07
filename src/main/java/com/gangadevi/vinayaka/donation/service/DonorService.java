package com.gangadevi.vinayaka.donation.service;

import com.gangadevi.vinayaka.common.exception.ResourceNotFoundException;
import com.gangadevi.vinayaka.common.exception.ConflictException;
import com.gangadevi.vinayaka.donation.dto.DonorRequest;
import com.gangadevi.vinayaka.donation.dto.DonorResponse;
import com.gangadevi.vinayaka.donation.entity.Donor;
import com.gangadevi.vinayaka.donation.repository.DonationRepository;
import com.gangadevi.vinayaka.donation.repository.DonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DonorService {
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;

    @Transactional(readOnly = true)
    public List<DonorResponse> findAll() {
        return donorRepository.findAll(Sort.by(Sort.Direction.ASC, "name"))
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DonorResponse findById(Long id) {
        return toResponse(getDonor(id));
    }

    public DonorResponse create(DonorRequest request) {
        Donor donor = Donor.builder()
                .name(request.name().trim())
                .phone(normalize(request.phone()))
                .email(normalize(request.email()))
                .build();
        return toResponse(donorRepository.save(donor));
    }

    public DonorResponse update(Long id, DonorRequest request) {
        Donor donor = getDonor(id);
        donor.setName(request.name().trim());
        donor.setPhone(normalize(request.phone()));
        donor.setEmail(normalize(request.email()));
        return toResponse(donorRepository.save(donor));
    }

    public void delete(Long id) {
        getDonor(id);
        if (donationRepository.existsByDonorId(id)) {
            throw new ConflictException("Cannot delete donor because donations are linked to donor: " + id);
        }
        donorRepository.deleteById(id);
    }

    private Donor getDonor(Long id) {
        return donorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found: " + id));
    }

    private DonorResponse toResponse(Donor donor) {
        return new DonorResponse(donor.getId(), donor.getName(), donor.getPhone(), donor.getEmail(),
                donor.getCreatedAt(), donor.getUpdatedAt());
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }
}
