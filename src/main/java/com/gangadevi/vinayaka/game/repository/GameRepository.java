package com.gangadevi.vinayaka.game.repository;

import com.gangadevi.vinayaka.game.entity.Game;
import com.gangadevi.vinayaka.game.entity.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByFestivalYearYearOrderByIdAsc(Integer year);

    long countByFestivalYearYear(Integer year);

    long countByFestivalYearYearAndStatus(Integer year, GameStatus status);
}
