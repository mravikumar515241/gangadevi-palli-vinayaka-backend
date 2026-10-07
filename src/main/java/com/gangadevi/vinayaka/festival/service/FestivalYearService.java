package com.gangadevi.vinayaka.festival.service;
import com.gangadevi.vinayaka.common.exception.ResourceNotFoundException;
import com.gangadevi.vinayaka.common.exception.ConflictException;
import com.gangadevi.vinayaka.festival.dto.*;
import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import com.gangadevi.vinayaka.festival.repository.FestivalYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor @Transactional
public class FestivalYearService {
 private final FestivalYearRepository repository;
 @Transactional(readOnly=true) public List<FestivalYearResponse> findAll(){
  return repository.findAll(Sort.by("year")).stream().map(this::toResponse).toList();
 }
 @Transactional(readOnly=true) public FestivalYearResponse findByYear(Integer year){
  return toResponse(repository.findByYear(year).orElseThrow(()->new ResourceNotFoundException("Festival year not found: "+year)));
 }
 public FestivalYearResponse create(FestivalYearRequest r){
  if(repository.existsByYear(r.year())) throw new ConflictException("Festival year already exists: "+r.year());
  return toResponse(repository.save(FestivalYear.builder().year(r.year()).status(r.status()).title(r.title()).festivalDate(r.festivalDate()).description(r.description()).build()));
 }
 public FestivalYearResponse update(Integer year,FestivalYearRequest r){
  FestivalYear e=repository.findByYear(year).orElseThrow(()->new ResourceNotFoundException("Festival year not found: "+year));
  if(!year.equals(r.year()) && repository.existsByYear(r.year())) throw new ConflictException("Festival year already exists: "+r.year());
  e.setYear(r.year());e.setStatus(r.status());e.setTitle(r.title());e.setFestivalDate(r.festivalDate());e.setDescription(r.description());
  return toResponse(repository.save(e));
 }
 private FestivalYearResponse toResponse(FestivalYear e){return new FestivalYearResponse(e.getId(),e.getYear(),e.getStatus(),e.getTitle(),e.getFestivalDate(),e.getDescription(),e.getCreatedAt(),e.getUpdatedAt());}
}