CREATE TABLE donor (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE donation (
    id BIGSERIAL PRIMARY KEY,
    festival_year_id BIGINT NOT NULL,
    donor_id BIGINT NOT NULL,
    planned_amount NUMERIC(14,2) NOT NULL,
    received_amount NUMERIC(14,2) NOT NULL,
    donation_date DATE,
    payment_method VARCHAR(30),
    reference_number VARCHAR(100),
    notes VARCHAR(1000),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_donation_festival_year FOREIGN KEY (festival_year_id) REFERENCES festival_year(id),
    CONSTRAINT fk_donation_donor FOREIGN KEY (donor_id) REFERENCES donor(id),
    CONSTRAINT chk_donation_planned_non_negative CHECK (planned_amount >= 0),
    CONSTRAINT chk_donation_received_non_negative CHECK (received_amount >= 0),
    CONSTRAINT chk_donation_received_not_over_planned CHECK (received_amount <= planned_amount),
    CONSTRAINT chk_donation_payment_method CHECK (
        payment_method IS NULL OR payment_method IN ('CASH','UPI','BANK_TRANSFER','CHEQUE','OTHER')
    )
);

CREATE INDEX idx_donation_festival_year_id ON donation(festival_year_id);
CREATE INDEX idx_donation_donor_id ON donation(donor_id);
CREATE INDEX idx_donation_date ON donation(donation_date);
