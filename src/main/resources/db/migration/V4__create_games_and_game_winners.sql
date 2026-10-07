CREATE TABLE game (
    id BIGSERIAL PRIMARY KEY,
    festival_year_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    telugu_name VARCHAR(200),
    category VARCHAR(30) NOT NULL,
    description VARCHAR(2000),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_game_festival_year
        FOREIGN KEY (festival_year_id) REFERENCES festival_year(id)
);

CREATE INDEX idx_game_festival_year_id ON game(festival_year_id);

CREATE TABLE game_winner (
    id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL,
    position VARCHAR(20) NOT NULL,
    winner_name VARCHAR(150) NOT NULL,
    team_name VARCHAR(150),
    notes VARCHAR(1000),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_game_winner_game
        FOREIGN KEY (game_id) REFERENCES game(id) ON DELETE CASCADE
);

CREATE INDEX idx_game_winner_game_id ON game_winner(game_id);
CREATE UNIQUE INDEX uk_game_winner_podium_position
    ON game_winner(game_id, position)
    WHERE position IN ('FIRST', 'SECOND', 'THIRD');
