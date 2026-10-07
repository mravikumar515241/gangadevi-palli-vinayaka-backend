package com.gangadevi.vinayaka.game.repository;

import com.gangadevi.vinayaka.game.entity.GameWinner;
import com.gangadevi.vinayaka.game.entity.WinnerPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameWinnerRepository extends JpaRepository<GameWinner, Long> {
    @Query("""
            select w from GameWinner w
            where w.game.id = :gameId
            order by case w.position
                when com.gangadevi.vinayaka.game.entity.WinnerPosition.FIRST then 1
                when com.gangadevi.vinayaka.game.entity.WinnerPosition.SECOND then 2
                when com.gangadevi.vinayaka.game.entity.WinnerPosition.THIRD then 3
                else 4
            end, w.id asc
            """)
    List<GameWinner> findByGameIdOrderByPodium(@Param("gameId") Long gameId);

    long countByGameId(Long gameId);

    boolean existsByGameIdAndPosition(Long gameId, WinnerPosition position);

    boolean existsByGameIdAndPositionAndIdNot(Long gameId, WinnerPosition position, Long id);
}
