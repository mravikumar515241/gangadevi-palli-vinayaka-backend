package com.gangadevi.vinayaka.laddu.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "laddu_interest_slab")
public class InterestSlab {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="min_amount", nullable=false, precision=15, scale=2)
    private BigDecimal minAmount;

    @Column(name="max_amount", precision=15, scale=2)
    private BigDecimal maxAmount;

    @Column(name="interest_rate", nullable=false, precision=5, scale=2)
    private BigDecimal interestRate;

    @Column(nullable=false)
    private Boolean active = true;

    public Long getId(){ return id; }
    public BigDecimal getMinAmount(){ return minAmount; }
    public void setMinAmount(BigDecimal v){ minAmount=v; }
    public BigDecimal getMaxAmount(){ return maxAmount; }
    public void setMaxAmount(BigDecimal v){ maxAmount=v; }
    public BigDecimal getInterestRate(){ return interestRate; }
    public void setInterestRate(BigDecimal v){ interestRate=v; }
    public Boolean getActive(){ return active; }
    public void setActive(Boolean v){ active=v; }
}
