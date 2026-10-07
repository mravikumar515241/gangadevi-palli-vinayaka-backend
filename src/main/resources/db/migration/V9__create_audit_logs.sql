CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    action VARCHAR(20) NOT NULL,
    entity_type VARCHAR(80) NOT NULL,
    entity_id BIGINT,
    description VARCHAR(500),
    request_id VARCHAR(100),
    ip_address VARCHAR(64),
    created_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT ck_audit_log_action CHECK (action IN ('CREATE', 'UPDATE', 'DELETE', 'LOGIN'))
);

CREATE INDEX idx_audit_log_created_at ON audit_log (created_at DESC, id DESC);
CREATE INDEX idx_audit_log_username ON audit_log (username);
CREATE INDEX idx_audit_log_entity ON audit_log (entity_type, entity_id);
CREATE INDEX idx_audit_log_action ON audit_log (action);
