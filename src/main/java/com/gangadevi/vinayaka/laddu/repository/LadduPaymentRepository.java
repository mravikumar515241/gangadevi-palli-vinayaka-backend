package com.gangadevi.vinayaka.laddu.repository;

import com.gangadevi.vinayaka.laddu.entity.LadduPayment;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;

public interface LadduPaymentRepository extends JpaRepository<LadduPayment, Long> {
    List<LadduPayment> findByAuctionIdOrderByPaymentDateAscIdAsc(Long auctionId);

    @Query("select coalesce(sum(p.amount),0) from LadduPayment p where p.auction.id=:auctionId")
    BigDecimal sumAmountByAuctionId(@Param("auctionId") Long auctionId);
}
