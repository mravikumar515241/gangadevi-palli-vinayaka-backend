package com.gangadevi.vinayaka.donation.service;

import com.gangadevi.vinayaka.donation.dto.DonationRequest;
import com.gangadevi.vinayaka.donation.entity.Donor;
import com.gangadevi.vinayaka.donation.repository.DonationRepository;
import com.gangadevi.vinayaka.donation.repository.DonorRepository;
import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import com.gangadevi.vinayaka.festival.repository.FestivalYearRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {
    @Mock DonationRepository donationRepository;
    @Mock DonorRepository donorRepository;
    @Mock FestivalYearRepository festivalYearRepository;
    @InjectMocks DonationService service;

    @Test
    void createCalculatesPendingAmount() {
        FestivalYear festival = FestivalYear.builder().id(1L).year(2026).build();
        Donor donor = Donor.builder().id(2L).name("Anji").build();
        DonationRequest request = new DonationRequest(2L, new BigDecimal("5000.00"), new BigDecimal("3000.00"), null, null, null, null);

        when(festivalYearRepository.findByYear(2026)).thenReturn(Optional.of(festival));
        when(donorRepository.findById(2L)).thenReturn(Optional.of(donor));
        when(donationRepository.save(any())).thenAnswer(invocation -> {
            var d = invocation.getArgument(0, com.gangadevi.vinayaka.donation.entity.Donation.class);
            d.setId(10L);
            return d;
        });

        var response = service.create(2026, request);

        assertEquals(new BigDecimal("2000.00"), response.pendingAmount());
        assertEquals("Anji", response.donorName());
    }

    @Test
    void rejectsReceivedAmountGreaterThanPlanned() {
        DonationRequest request = new DonationRequest(2L, new BigDecimal("1000.00"), new BigDecimal("1001.00"), null, null, null, null);
        var exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.create(2026, request));
        assertEquals("Received amount cannot be greater than planned amount", exception.getMessage());
        verifyNoInteractions(festivalYearRepository, donorRepository, donationRepository);
    }
}
