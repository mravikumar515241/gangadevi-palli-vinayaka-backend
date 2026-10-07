package com.gangadevi.vinayaka.game.service;

import com.gangadevi.vinayaka.common.exception.ResourceNotFoundException;
import com.gangadevi.vinayaka.festival.entity.FestivalYear;
import com.gangadevi.vinayaka.festival.repository.FestivalYearRepository;
import com.gangadevi.vinayaka.game.dto.GameRequest;
import com.gangadevi.vinayaka.game.dto.GameResponse;
import com.gangadevi.vinayaka.game.dto.GameWinnerResponse;
import com.gangadevi.vinayaka.game.entity.Game;
import com.gangadevi.vinayaka.game.repository.GameRepository;
import com.gangadevi.vinayaka.game.repository.GameWinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GameService {
    private final GameRepository gameRepository;
    private final GameWinnerRepository winnerRepository;
    private final FestivalYearRepository festivalYearRepository;

    @Transactional(readOnly = true)
    public List<GameResponse> findByFestivalYear(Integer year) {
        getFestivalYear(year);
        return gameRepository.findByFestivalYearYearOrderByIdAsc(year)
                .stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public GameResponse findById(Long id) {
        return toResponse(getGame(id));
    }

    public GameResponse create(Integer year, GameRequest request) {
        FestivalYear festivalYear = getFestivalYear(year);
        Game game = Game.builder()
                .festivalYear(festivalYear)
                .name(normalizeRequired(request.name()))
                .teluguName(normalize(request.teluguName()))
                .category(request.category())
                .description(normalize(request.description()))
                .status(request.status())
                .build();
        return toResponse(gameRepository.save(game));
    }

    public GameResponse update(Long id, GameRequest request) {
        Game game = getGame(id);
        game.setName(normalizeRequired(request.name()));
        game.setTeluguName(normalize(request.teluguName()));
        game.setCategory(request.category());
        game.setDescription(normalize(request.description()));
        game.setStatus(request.status());
        return toResponse(gameRepository.save(game));
    }

    public void delete(Long id) {
        Game game = getGame(id);
        winnerRepository.deleteAll(winnerRepository.findByGameIdOrderByPodium(id));
        gameRepository.delete(game);
    }

    private Game getGame(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Game not found: " + id));
    }

    private FestivalYear getFestivalYear(Integer year) {
        return festivalYearRepository.findByYear(year)
                .orElseThrow(() -> new ResourceNotFoundException("Festival year not found: " + year));
    }

    private GameResponse toResponse(Game game) {
        List<GameWinnerResponse> winners = winnerRepository.findByGameIdOrderByPodium(game.getId())
                .stream()
                .map(w -> new GameWinnerResponse(
                        w.getId(), w.getGame().getId(), w.getPosition(), w.getWinnerName(),
                        w.getTeamName(), w.getNotes(), w.getCreatedAt(), w.getUpdatedAt()))
                .toList();
        return new GameResponse(
                game.getId(), game.getFestivalYear().getYear(), game.getName(), game.getTeluguName(),
                game.getCategory(), game.getDescription(), game.getStatus(), winners.size(), winners,
                game.getCreatedAt(), game.getUpdatedAt());
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) return null;
        return value.trim();
    }

    private String normalizeRequired(String value) {
        return value.trim();
    }
}
