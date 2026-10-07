package com.gangadevi.vinayaka.donation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DonorRequest(
        @NotBlank @Size(max = 120) String name,
        @Size(max = 20) String phone,
        @Email @Size(max = 255) String email
) {}
