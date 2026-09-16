CREATE TABLE hotels (
    id BIGINT NOT NULL AUTO_INCREMENT,

    name VARCHAR(200) NOT NULL,
    slug VARCHAR(220) NOT NULL,

    description TEXT,

    address VARCHAR(500),
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'India',

    latitude DECIMAL(10, 7),
    longitude DECIMAL(10, 7),

    star_rating DECIMAL(2, 1),

    contact_phone VARCHAR(20),
    contact_email VARCHAR(255),

    check_in_time TIME,
    check_out_time TIME,

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    UNIQUE KEY uk_hotels_slug (slug),

    INDEX idx_hotels_city (city),
    INDEX idx_hotels_state (state),
    INDEX idx_hotels_status (status)
);