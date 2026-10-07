package com.gangadevi.vinayaka.dashboard.service;

import com.gangadevi.vinayaka.activity.repository.ActivityItemRepository;
import com.gangadevi.vinayaka.activity.repository.ActivityRepository;
import com.gangadevi.vinayaka.common.exception.ResourceNotFoundException;
import com.gangadevi.vinayaka.dashboard.dto.*;
import com.gangadevi.vinayaka.donation.repository.DonationRepository;
import com.gangadevi.vinayaka.festival.repository.FestivalYearRepository;
import com.gangadevi.vinayaka.game.entity.GameStatus;
import com.gangadevi.vinayaka.game.repository.GameRepository;
import com.gangadevi.vinayaka.gallery.repository.GalleryImageRepository;
import com.gangadevi.vinayaka.laddu.entity.LadduAuction;
import com.gangadevi.vinayaka.laddu.repository.LadduAuctionRepository;
import com.gangadevi.vinayaka.laddu.repository.LadduPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final FestivalYearRepository festivalYearRepository;
    private final DonationRepository donationRepository;
    private final ActivityRepository activityRepository;
    private final ActivityItemRepository activityItemRepository;
    private final GameRepository gameRepository;
    private final GalleryImageRepository galleryImageRepository;
    private final LadduAuctionRepository ladduAuctionRepository;
    private final LadduPaymentRepository ladduPaymentRepository;

    public DashboardResponse getDashboard(Integer year) {
        festivalYearRepository.findByYear(year)
                .orElseThrow(() -> new ResourceNotFoundException("Festival year not found: " + year));

        BigDecimal plannedDonations = zero(donationRepository.sumPlannedAmountByYear(year));
        BigDecimal receivedDonations = zero(donationRepository.sumReceivedAmountByYear(year));

        DonationDashboardResponse donations = new DonationDashboardResponse(
                plannedDonations,
                receivedDonations,
                plannedDonations.subtract(receivedDonations).setScale(2),
                donationRepository.countDistinctDonorsByYear(year));

        BigDecimal plannedBudget = zero(activityRepository.sumPlannedBudgetByYear(year));
        BigDecimal actualSpent = zero(activityItemRepository.sumTotalCostByFestivalYear(year));

        ActivityDashboardResponse activities = new ActivityDashboardResponse(
                plannedBudget,
                actualSpent,
                plannedBudget.subtract(actualSpent).setScale(2),
                activityRepository.countByFestivalYearYear(year));

        GameDashboardResponse games = new GameDashboardResponse(
                gameRepository.countByFestivalYearYear(year),
                gameRepository.countByFestivalYearYearAndStatus(year, GameStatus.PLANNED),
                gameRepository.countByFestivalYearYearAndStatus(year, GameStatus.ONGOING),
                gameRepository.countByFestivalYearYearAndStatus(year, GameStatus.COMPLETED),
                gameRepository.countByFestivalYearYearAndStatus(year, GameStatus.CANCELLED));

        GalleryDashboardResponse gallery = new GalleryDashboardResponse(
                galleryImageRepository.countByFestivalYearYear(year));

        LadduDashboardResponse laddu = ladduAuctionRepository.findByFestivalYear(
                        festivalYearRepository.findByYear(year).orElseThrow())
                .map(this::toLadduResponse)
                .orElseGet(LadduDashboardResponse::empty);

        return new DashboardResponse(year, donations, activities, games, gallery, laddu);
    }

    private LadduDashboardResponse toLadduResponse(LadduAuction auction) {
        BigDecimal paid = zero(ladduPaymentRepository.sumAmountByAuctionId(auction.getId()));
        BigDecimal totalPayable = zero(auction.getTotalPayable());
        return new LadduDashboardResponse(
                true,
                auction.getId(),
                auction.getOwnerName(),
                zero(auction.getWinningAmount()),
                auction.getAuctionDate(),
                auction.getDueDate(),
                zero(auction.getInterestRate()),
                zero(auction.getInterestAmount()),
                totalPayable,
                paid,
                totalPayable.subtract(paid).setScale(2),
                auction.getStatus());
    }

    private BigDecimal zero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO.setScale(2) : value.setScale(2);
    }
}
