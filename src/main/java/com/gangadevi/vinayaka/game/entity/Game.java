package com.gangadevi.vinayaka.game.entity;

import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "game")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "festival_year_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_game_festival_year"))
    private FestivalYear festivalYear;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "telugu_name", length = 200)
    private String teluguName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GameCategory category;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GameStatus status;

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
