CREATE TABLE places (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    destination_id BIGINT NOT NULL,

    name VARCHAR(150) NOT NULL,

    category VARCHAR(30) NOT NULL,

    description VARCHAR(500),

    address VARCHAR(300),

    city VARCHAR(100) NOT NULL,

    state VARCHAR(100) NOT NULL,

    country VARCHAR(100) NOT NULL DEFAULT 'India',

    latitude DECIMAL(10,7),

    longitude DECIMAL(10,7),

    opening_time TIME,

    closing_time TIME,

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_places_destination
        FOREIGN KEY (destination_id)
        REFERENCES destinations(id)
);