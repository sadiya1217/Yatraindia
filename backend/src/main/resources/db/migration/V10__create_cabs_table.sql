CREATE TABLE cabs (
    id BIGINT NOT NULL AUTO_INCREMENT,

    name VARCHAR(200) NOT NULL,
    slug VARCHAR(220) NOT NULL,

    cab_type VARCHAR(50) NOT NULL,

    description TEXT,

    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL DEFAULT 'India',

    seating_capacity INT NOT NULL,

    contact_phone VARCHAR(20),
    contact_email VARCHAR(255),

    base_fare DECIMAL(10, 2),
    per_km_rate DECIMAL(10, 2),
    per_hour_rate DECIMAL(10, 2),

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    UNIQUE KEY uk_cabs_slug (slug),

    INDEX idx_cabs_city (city),
    INDEX idx_cabs_state (state),
    INDEX idx_cabs_type (cab_type),
    INDEX idx_cabs_status (status)
);