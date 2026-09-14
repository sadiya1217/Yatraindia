
  CREATE TABLE destinations (
    id BIGINT NOT NULL AUTO_INCREMENT,

    name VARCHAR(150) NOT NULL,
    slug VARCHAR(180) NOT NULL,

    short_description VARCHAR(500),
    description TEXT,

    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'India',

    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),

    best_time_to_visit VARCHAR(255),

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    UNIQUE KEY uk_destinations_slug (slug),

    INDEX idx_destinations_name (name),
    INDEX idx_destinations_state (state),
    INDEX idx_destinations_status (status)
);