CREATE TABLE activity (
    id BIGSERIAL PRIMARY KEY,
    festival_year_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    telugu_name VARCHAR(200),
    planned_budget NUMERIC(14,2) NOT NULL CHECK (planned_budget >= 0),
    description VARCHAR(2000),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_activity_festival_year
        FOREIGN KEY (festival_year_id) REFERENCES festival_year(id)
);

CREATE INDEX idx_activity_festival_year_id ON activity(festival_year_id);

CREATE TABLE activity_item (
    id BIGSERIAL PRIMARY KEY,
    activity_id BIGINT NOT NULL,
    item_name VARCHAR(150) NOT NULL,
    quantity NUMERIC(14,3) NOT NULL CHECK (quantity > 0),
    unit VARCHAR(30) NOT NULL,
    unit_cost NUMERIC(14,2) NOT NULL CHECK (unit_cost >= 0),
    notes VARCHAR(1000),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_activity_item_activity
        FOREIGN KEY (activity_id) REFERENCES activity(id) ON DELETE CASCADE
);

CREATE INDEX idx_activity_item_activity_id ON activity_item(activity_id);
