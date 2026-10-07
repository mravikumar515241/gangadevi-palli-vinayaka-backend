package com.gangadevi.vinayaka.laddu.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="laddu_payment")
public class LadduPayment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="auction_id", nullable=false)
    private LadduAuction auction;

    @Column(nullable=false, precision=15, scale=2)
    private BigDecimal amount;
    @Column(name="payment_date", nullable=false)
    private LocalDate paymentDate;
    @Column(name="payment_method", length=30)
    private String paymentMethod;
    @Column(name="reference_number", length=100)
    private String referenceNumber;
    @Column(length=500)
    private String notes;

    public Long getId(){return id;}
    public LadduAuction getAuction(){return auction;}
    public void setAuction(LadduAuction v){auction=v;}
    public BigDecimal getAmount(){return amount;}
    public void setAmount(BigDecimal v){amount=v;}
    public LocalDate getPaymentDate(){return paymentDate;}
    public void setPaymentDate(LocalDate v){paymentDate=v;}
    public String getPaymentMethod(){return paymentMethod;}
    public void setPaymentMethod(String v){paymentMethod=v;}
    public String getReferenceNumber(){return referenceNumber;}
    public void setReferenceNumber(String v){referenceNumber=v;}
    public String getNotes(){return notes;}
    public void setNotes(String v){notes=v;}
}
