package com.gangadevi.vinayaka.festival.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;
@Entity @Table(name="festival_year", uniqueConstraints=@UniqueConstraint(name="uk_festival_year_year",columnNames="year"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FestivalYear {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private Integer year;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private FestivalStatus status;
 @Column(nullable=false,length=150) private String title;
 @Column(name="festival_date") private LocalDate festivalDate;
 @Column(length=2000) private String description;
 @Column(name="created_at",nullable=false) private OffsetDateTime createdAt;
 @Column(name="updated_at",nullable=false) private OffsetDateTime updatedAt;
 @PrePersist void onCreate(){OffsetDateTime n=OffsetDateTime.now();createdAt=n;updatedAt=n;}
 @PreUpdate void onUpdate(){updatedAt=OffsetDateTime.now();}
 public enum FestivalStatus { UPCOMING, ACTIVE, COMPLETED, ARCHIVED }
}