package com.gangadevi.vinayaka.game.controller;
import com.gangadevi.vinayaka.audit.annotation.Audited;
import com.gangadevi.vinayaka.audit.entity.AuditAction;

import com.gangadevi.vinayaka.game.dto.GameRequest;
import com.gangadevi.vinayaka.game.dto.GameResponse;
import com.gangadevi.vinayaka.game.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameService service;

    @GetMapping("/api/v1/festivals/{year}/games")
    public List<GameResponse> getByFestivalYear(@PathVariable Integer year) {
        return service.findByFestivalYear(year);
    }

    @PostMapping("/api/v1/festivals/{year}/games")
    @ResponseStatus(HttpStatus.CREATED)
    @Audited(action = AuditAction.CREATE, entityType = "GAME", description = "Created game", entityIdFromResult = true)
    public GameResponse create(@PathVariable Integer year, @Valid @RequestBody GameRequest request) {
        return service.create(year, request);
    }

    @GetMapping("/api/v1/games/{id}")
    public GameResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/api/v1/games/{id}")
    @Audited(action = AuditAction.UPDATE, entityType = "GAME", description = "Updated game", entityIdArgument = 0)
    public GameResponse update(@PathVariable Long id, @Valid @RequestBody GameRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/api/v1/games/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Audited(action = AuditAction.DELETE, entityType = "GAME", description = "Deleted game", entityIdArgument = 0)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
