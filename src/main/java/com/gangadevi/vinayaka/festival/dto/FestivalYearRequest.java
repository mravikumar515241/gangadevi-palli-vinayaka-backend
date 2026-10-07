package com.gangadevi.vinayaka.festival.dto;
import com.gangadevi.vinayaka.festival.entity.FestivalYear.FestivalStatus;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
public record FestivalYearRequest(
 @NotNull @Min(2000) @Max(2100) Integer year,
 @NotNull FestivalStatus status,
 @NotBlank @Size(max=150) String title,
 LocalDate festivalDate,
 @Size(max=2000) String description) {}