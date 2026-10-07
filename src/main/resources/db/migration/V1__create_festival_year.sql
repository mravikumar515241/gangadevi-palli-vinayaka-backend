CREATE TABLE festival_year (
 id BIGSERIAL PRIMARY KEY,
 year INTEGER NOT NULL,
 status VARCHAR(20) NOT NULL,
 title VARCHAR(150) NOT NULL,
 festival_date DATE,
 description VARCHAR(2000),
 created_at TIMESTAMPTZ NOT NULL,
 updated_at TIMESTAMPTZ NOT NULL,
 CONSTRAINT uk_festival_year_year UNIQUE(year),
 CONSTRAINT chk_festival_year_status CHECK(status IN ('UPCOMING','ACTIVE','COMPLETED','ARCHIVED'))
);