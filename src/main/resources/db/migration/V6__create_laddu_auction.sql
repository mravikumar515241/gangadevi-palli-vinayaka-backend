CREATE TABLE laddu_interest_slab (
    id BIGSERIAL PRIMARY KEY,
    min_amount NUMERIC(15,2) NOT NULL,
    max_amount NUMERIC(15,2),
    interest_rate NUMERIC(5,2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

INSERT INTO laddu_interest_slab (min_amount, max_amount, interest_rate, active) VALUES
(0.00, 10000.01, 36.00, TRUE),
(10000.01, 20000.01, 24.00, TRUE),
(20000.01, 50000.01, 12.00, TRUE);

CREATE TABLE laddu_auction (
    id BIGSERIAL PRIMARY KEY,
    festival_year_id BIGINT NOT NULL UNIQUE REFERENCES festival_year(id),
    owner_name VARCHAR(120) NOT NULL,
    owner_phone VARCHAR(30),
    winning_amount NUMERIC(15,2) NOT NULL,
    auction_date DATE NOT NULL,
    due_date DATE NOT NULL,
    interest_rate NUMERIC(5,2) NOT NULL DEFAULT 0,
    interest_amount NUMERIC(15,2) NOT NULL DEFAULT 0,
    total_payable NUMERIC(15,2) NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL
);

CREATE TABLE laddu_payment (
    id BIGSERIAL PRIMARY KEY,
    auction_id BIGINT NOT NULL REFERENCES laddu_auction(id) ON DELETE CASCADE,
    amount NUMERIC(15,2) NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(30),
    reference_number VARCHAR(100),
    notes VARCHAR(500)
);
