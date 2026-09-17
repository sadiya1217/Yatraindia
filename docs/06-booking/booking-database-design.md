# YatraIndia — Booking Module Database Design

## 1. Overview

The Booking module manages customer bookings for services available through the YatraIndia platform.

The module supports different service types such as hotels and cabs using a common booking structure.

## 2. Database Table

### Table: `bookings`

The `bookings` table stores the details of each customer booking.

| Column | Data Type | Nullable | Key / Constraint | Description |
|---|---|---|---|---|
| `id` | BIGINT | NO | Primary Key, Auto Increment | Unique booking ID |
| `booking_reference` | VARCHAR(50) | NO | UNIQUE | Unique customer-facing booking reference |
| `user_id` | BIGINT | NO | — | ID of the customer who created the booking |
| `booking_type` | VARCHAR(20) | NO | — | Type of service, such as HOTEL or CAB |
| `service_id` | BIGINT | NO | — | ID of the selected service |
| `check_in` | DATETIME | YES | — | Booking start/check-in date and time |
| `check_out` | DATETIME | YES | — | Booking end/check-out date and time |
| `total_amount` | DECIMAL(12,2) | YES | — | Total booking amount |
| `status` | VARCHAR(20) | NO | — | Current booking status |
| `created_at` | DATETIME | NO | DEFAULT CURRENT_TIMESTAMP | Booking creation timestamp |
| `updated_at` | DATETIME | YES | DEFAULT CURRENT_TIMESTAMP / ON UPDATE | Last modification timestamp |

## 3. Primary Key

The `id` column is the primary key of the `bookings` table.

It is automatically generated using auto-increment.

## 4. Booking Reference

The `booking_reference` column uniquely identifies a booking.

It has a UNIQUE constraint to prevent duplicate booking references.

Example:

`BK-API-TEST-002`

## 5. User Association

The `user_id` column identifies the customer associated with the booking.

The Booking module provides an API to retrieve bookings belonging to a particular user.

## 6. Booking Type

The `booking_type` column identifies the category of service being booked.

Examples:

- `HOTEL`
- `CAB`

The common booking structure allows additional service categories to be added later.

## 7. Service ID

The `service_id` column identifies the selected service.

The Booking module uses `booking_type` together with `service_id` to identify the relevant service.

Examples:

- `HOTEL` → ID from the `hotels` table
- `CAB` → ID from the `cabs` table

The booking table therefore supports multiple service categories without requiring a separate booking table for each service.

## 8. Booking Dates

The following fields store booking period information:

- `check_in` — booking start date and time
- `check_out` — booking end date and time

These fields are particularly useful for hotel bookings.

## 9. Booking Amount

The `total_amount` column uses:

`DECIMAL(12,2)`

This supports monetary values with two decimal places.

Example:

`6000.00`

## 10. Booking Status

The `status` column stores the current state of a booking.

Examples:

- `CONFIRMED`
- `CANCELLED`

The status can be changed using the Booking status update API.

## 11. Timestamp Management

The table contains:

- `created_at` — automatically records when the booking is created.
- `updated_at` — records the latest modification time.

The database provides default timestamp handling.

## 12. Flyway Migration

The Booking table is created through the following Flyway migration:

`V12__create_bookings_table.sql`

The migration creates the complete `bookings` table structure.

## 13. Design Considerations

The Booking table is designed as a common structure for multiple YatraIndia services.

Hotel-specific information remains in the `hotels` table and cab-specific information remains in the `cabs` table.

The Booking module connects these services logically through:

- `booking_type`
- `service_id`

This approach allows the booking system to be extended to additional travel services in the future.