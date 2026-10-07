package com.gangadevi.vinayaka.laddu.entity;

import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="laddu_auction")
public class LadduAuction {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="festival_year_id", nullable=false, unique=true)
    private FestivalYear festivalYear;

    @Column(name="owner_name", nullable=false, length=120)
    private String ownerName;
    @Column(name="owner_phone", length=30)
    private String ownerPhone;
    @Column(name="winning_amount", nullable=false, precision=15, scale=2)
    private BigDecimal winningAmount;
    @Column(name="auction_date", nullable=false)
    private LocalDate auctionDate;
    @Column(name="due_date", nullable=false)
    private LocalDate dueDate;
    @Column(name="interest_rate", nullable=false, precision=5, scale=2)
    private BigDecimal interestRate=BigDecimal.ZERO;
    @Column(name="interest_amount", nullable=false, precision=15, scale=2)
    private BigDecimal interestAmount=BigDecimal.ZERO;
    @Column(name="total_payable", nullable=false, precision=15, scale=2)
    private BigDecimal totalPayable=BigDecimal.ZERO;
    @Column(nullable=false, length=30)
    private String status;

    public Long getId(){return id;}
    public FestivalYear getFestivalYear(){return festivalYear;}
    public void setFestivalYear(FestivalYear v){festivalYear=v;}
    public String getOwnerName(){return ownerName;}
    public void setOwnerName(String v){ownerName=v;}
    public String getOwnerPhone(){return ownerPhone;}
    public void setOwnerPhone(String v){ownerPhone=v;}
    public BigDecimal getWinningAmount(){return winningAmount;}
    public void setWinningAmount(BigDecimal v){winningAmount=v;}
    public LocalDate getAuctionDate(){return auctionDate;}
    public void setAuctionDate(LocalDate v){auctionDate=v;}
    public LocalDate getDueDate(){return dueDate;}
    public void setDueDate(LocalDate v){dueDate=v;}
    public BigDecimal getInterestRate(){return interestRate;}
    public void setInterestRate(BigDecimal v){interestRate=v;}
    public BigDecimal getInterestAmount(){return interestAmount;}
    public void setInterestAmount(BigDecimal v){interestAmount=v;}
    public BigDecimal getTotalPayable(){return totalPayable;}
    public void setTotalPayable(BigDecimal v){totalPayable=v;}
    public String getStatus(){return status;}
    public void setStatus(String v){status=v;}
}
