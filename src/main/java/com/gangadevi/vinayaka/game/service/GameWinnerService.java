package com.gangadevi.vinayaka.game.service;

import com.gangadevi.vinayaka.common.exception.ResourceNotFoundException;
import com.gangadevi.vinayaka.common.exception.ConflictException;
import com.gangadevi.vinayaka.game.dto.GameWinnerRequest;
import com.gangadevi.vinayaka.game.dto.GameWinnerResponse;
import com.gangadevi.vinayaka.game.entity.Game;
import com.gangadevi.vinayaka.game.entity.GameWinner;
import com.gangadevi.vinayaka.game.entity.WinnerPosition;
import com.gangadevi.vinayaka.game.repository.GameRepository;
import com.gangadevi.vinayaka.game.repository.GameWinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GameWinnerService {
    private final GameWinnerRepository winnerRepository;
    private final GameRepository gameRepository;

    @Transactional(readOnly = true)
    public List<GameWinnerResponse> findByGameId(Long gameId) {
        getGame(gameId);
        return winnerRepository.findByGameIdOrderByPodium(gameId)
                .stream().map(this::toResponse).toList();
    }

    public GameWinnerResponse create(Long gameId, GameWinnerRequest request) {
        Game game = getGame(gameId);
        validatePosition(gameId, request.position(), null);
        GameWinner winner = GameWinner.builder()
                .game(game)
                .position(request.position())
                .winnerName(normalizeRequired(request.winnerName()))
                .teamName(normalize(request.teamName()))
                .notes(normalize(request.notes()))
                .build();
        return toResponse(winnerRepository.save(winner));
    }

    @Transactional(readOnly = true)
    public GameWinnerResponse findById(Long id) {
        return toResponse(getWinner(id));
    }

    public GameWinnerResponse update(Long id, GameWinnerRequest request) {
        GameWinner winner = getWinner(id);
        validatePosition(winner.getGame().getId(), request.position(), id);
        winner.setPosition(request.position());
        winner.setWinnerName(normalizeRequired(request.winnerName()));
        winner.setTeamName(normalize(request.teamName()));
        winner.setNotes(normalize(request.notes()));
        return toResponse(winnerRepository.save(winner));
    }

    public void delete(Long id) {
        winnerRepository.delete(getWinner(id));
    }

    private void validatePosition(Long gameId, WinnerPosition position, Long currentWinnerId) {
        if (position == WinnerPosition.SPECIAL) return;

        boolean exists = currentWinnerId == null
                ? winnerRepository.existsByGameIdAndPosition(gameId, position)
                : winnerRepository.existsByGameIdAndPositionAndIdNot(gameId, position, currentWinnerId);

        if (exists) {
            throw new ConflictException("A " + position + " winner already exists for this game");
        }
    }

    private Game getGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found: " + id));
    }

    private GameWinner getWinner(Long id) {
        return winnerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game winner not found: " + id));
    }

    private GameWinnerResponse toResponse(GameWinner winner) {
        return new GameWinnerResponse(
                winner.getId(), winner.getGame().getId(), winner.getPosition(),
                winner.getWinnerName(), winner.getTeamName(), winner.getNotes(),
                winner.getCreatedAt(), winner.getUpdatedAt());
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }

    private String normalizeRequired(String value) {
        return value.trim();
    }
}
