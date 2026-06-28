--liquibase formatted sql
--changeset bubunur:2

CREATE TABLE IF NOT EXISTS candidate_status_history
(
    id           UUID PRIMARY KEY,
    candidate_id VARCHAR(36)              NOT NULL,
    from_status  VARCHAR(20)              NOT NULL,
    to_status    VARCHAR(20)              NOT NULL,
    comment      TEXT,
    changed_at   TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_status_history_candidate
        FOREIGN KEY (candidate_id)
            REFERENCES candidates(id)
            ON DELETE CASCADE
);

CREATE INDEX idx_status_history_candidate_id ON candidate_status_history (candidate_id);