package com.gangadevi.vinayaka.festival.dto;
import com.gangadevi.vinayaka.festival.entity.FestivalYear.FestivalStatus;
import java.time.*;
public record FestivalYearResponse(Long id,Integer year,FestivalStatus status,String title,LocalDate festivalDate,String description,OffsetDateTime createdAt,OffsetDateTime updatedAt) {}