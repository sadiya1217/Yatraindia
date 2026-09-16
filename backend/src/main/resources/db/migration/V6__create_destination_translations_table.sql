CREATE TABLE destination_translations (
    id BIGINT NOT NULL AUTO_INCREMENT,

    destination_id BIGINT NOT NULL,

    language_code VARCHAR(10) NOT NULL,

    name VARCHAR(150) NOT NULL,

    short_description VARCHAR(500),

    description TEXT,

    best_time_to_visit VARCHAR(255),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    CONSTRAINT fk_destination_translations_destination
        FOREIGN KEY (destination_id)
        REFERENCES destinations(id)
        ON DELETE CASCADE,

    UNIQUE KEY uk_destination_language
        (destination_id, language_code),

    INDEX idx_destination_translations_language
        (language_code)
);