CREATE TABLE candidates
(
    id VARCHAR(36) PRIMARY KEY ,
    name         VARCHAR(100)             NOT NULL,
    email        VARCHAR(255)             NOT NULL,
    phone        VARCHAR(50),
    position     VARCHAR(100)             NOT NULL,
    pos_label    VARCHAR(255),
    city         VARCHAR(100),
    telegram     VARCHAR(100),
    total_exp    VARCHAR(50),
    stack        TEXT,
    education    VARCHAR(255),
    verdict      VARCHAR(20)              NOT NULL,
    summary      TEXT,
    criteria     JSONB DEFAULT '[]'::jsonb,
    experience   JSONB DEFAULT '[]'::jsonb,
    questions    JSONB DEFAULT '[]'::jsonb,

    status       VARCHAR(20)              NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE UNIQUE INDEX idx_candidates_email ON candidates (email);
CREATE INDEX idx_candidates_verdict ON candidates (verdict);
CREATE INDEX idx_candidates_status ON candidates (status);
CREATE INDEX idx_candidates_position ON candidates (position);
CREATE INDEX idx_candidates_created_at ON candidates (created_at);