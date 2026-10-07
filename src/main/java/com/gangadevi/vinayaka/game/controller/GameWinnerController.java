package com.gangadevi.vinayaka.game.controller;
import com.gangadevi.vinayaka.audit.annotation.Audited;
import com.gangadevi.vinayaka.audit.entity.AuditAction;

import com.gangadevi.vinayaka.game.dto.GameWinnerRequest;
import com.gangadevi.vinayaka.game.dto.GameWinnerResponse;
import com.gangadevi.vinayaka.game.service.GameWinnerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GameWinnerController {
    private final GameWinnerService service;

    @GetMapping("/api/v1/games/{gameId}/winners")
    public List<GameWinnerResponse> getByGame(@PathVariable Long gameId) {
        return service.findByGameId(gameId);
    }

    @PostMapping("/api/v1/games/{gameId}/winners")
    @ResponseStatus(HttpStatus.CREATED)
    @Audited(action = AuditAction.CREATE, entityType = "GAME_WINNER", description = "Created game winner", entityIdFromResult = true)
    public GameWinnerResponse create(@PathVariable Long gameId, @Valid @RequestBody GameWinnerRequest request) {
        return service.create(gameId, request);
    }

    @GetMapping("/api/v1/game-winners/{id}")
    public GameWinnerResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/api/v1/game-winners/{id}")
    @Audited(action = AuditAction.UPDATE, entityType = "GAME_WINNER", description = "Updated game winner", entityIdArgument = 0)
    public GameWinnerResponse update(@PathVariable Long id, @Valid @RequestBody GameWinnerRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/api/v1/game-winners/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Audited(action = AuditAction.DELETE, entityType = "GAME_WINNER", description = "Deleted game winner", entityIdArgument = 0)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
