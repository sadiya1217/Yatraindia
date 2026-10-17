-- Add a configurable nightly rate to each hotel.
-- NULL means the hotel's price has not yet been configured.
ALTER TABLE hotels
    ADD COLUMN nightly_rate DECIMAL(10, 2) NULL
    AFTER star_rating;

-- Store the trusted route metrics used to calculate a cab fare.
ALTER TABLE bookings
    ADD COLUMN distance_km DECIMAL(10, 2) NULL
    AFTER total_amount;

ALTER TABLE bookings
    ADD COLUMN duration_minutes INT NULL
    AFTER distance_km;