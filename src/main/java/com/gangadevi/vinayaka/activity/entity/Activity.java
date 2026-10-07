package com.gangadevi.vinayaka.activity.entity;

import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "festival_year_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_activity_festival_year"))
    private FestivalYear festivalYear;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "telugu_name", length = 200)
    private String teluguName;

    @Column(name = "planned_budget", nullable = false, precision = 14, scale = 2)
    private BigDecimal plannedBudget;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ActivityStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
