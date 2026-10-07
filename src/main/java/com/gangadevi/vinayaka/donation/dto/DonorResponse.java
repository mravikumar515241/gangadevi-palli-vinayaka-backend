package com.gangadevi.vinayaka.donation.dto;

import java.time.OffsetDateTime;

public record DonorResponse(
        Long id,
        String name,
        String phone,
        String email,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
