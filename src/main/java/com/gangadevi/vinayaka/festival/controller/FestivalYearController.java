package com.gangadevi.vinayaka.festival.controller;
import com.gangadevi.vinayaka.audit.annotation.Audited;
import com.gangadevi.vinayaka.audit.entity.AuditAction;
import com.gangadevi.vinayaka.festival.dto.*;
import com.gangadevi.vinayaka.festival.service.FestivalYearService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/v1/festivals") @RequiredArgsConstructor
public class FestivalYearController {
 private final FestivalYearService service;
 @GetMapping public List<FestivalYearResponse> getAll(){return service.findAll();}
 @GetMapping("/{year}") public FestivalYearResponse getByYear(@PathVariable Integer year){return service.findByYear(year);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) @Audited(action = AuditAction.CREATE, entityType = "FESTIVAL_YEAR", description = "Created festival year", entityIdFromResult = true)
 public FestivalYearResponse create(@Valid @RequestBody FestivalYearRequest request){return service.create(request);}
 @PutMapping("/{year}") @Audited(action = AuditAction.UPDATE, entityType = "FESTIVAL_YEAR", description = "Updated festival year", entityIdFromResult = true)
 public FestivalYearResponse update(@PathVariable Integer year,@Valid @RequestBody FestivalYearRequest request){return service.update(year,request);}
}