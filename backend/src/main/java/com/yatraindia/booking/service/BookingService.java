package com.yatraindia.booking.service;

import com.yatraindia.booking.entity.Booking;
import com.yatraindia.booking.exception.BookingValidationException;
import com.yatraindia.booking.repository.BookingRepository;
import com.yatraindia.common.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BookingService {

    private static final String HOTEL = "HOTEL";
    private static final String CAB = "CAB";

    private static final String PENDING = "PENDING";
    private static final String CONFIRMED = "CONFIRMED";
    private static final String CANCELLED = "CANCELLED";
    private static final String COMPLETED = "COMPLETED";

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Get booking by ID
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + id));
    }

    // Get booking by booking reference
    public Booking getBookingByReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with reference: "
                                        + bookingReference));
    }

    // Get bookings by user
    public List<Booking> getBookingsByUser(Long userId) {

        if (userId == null || userId <= 0) {
            throw new BookingValidationException(
                    "User ID must be a positive number");
        }

        return bookingRepository.findByUserId(userId);
    }

    // Get bookings by booking type
    public List<Booking> getBookingsByType(String bookingType) {

        validateBookingType(bookingType);

        return bookingRepository.findByBookingTypeIgnoreCase(
                bookingType.trim());
    }

    // Get bookings by status
    public List<Booking> getBookingsByStatus(String status) {

        validateStatus(status);

        return bookingRepository.findByStatusIgnoreCase(
                status.trim());
    }

    // Get bookings by service and booking type
    public List<Booking> getBookingsByService(
            Long serviceId,
            String bookingType) {

        if (serviceId == null || serviceId <= 0) {
            throw new BookingValidationException(
                    "Service ID must be a positive number");
        }

        validateBookingType(bookingType);

        return bookingRepository.findByServiceIdAndBookingType(
                serviceId,
                bookingType.trim().toUpperCase());
    }

    // Create booking
    public Booking createBooking(Booking booking) {

        if (booking == null) {
            throw new BookingValidationException(
                    "Booking data is required");
        }

        validateBooking(booking);

        if (bookingRepository.existsByBookingReference(
                booking.getBookingReference())) {

            throw new BookingValidationException(
                    "Booking reference already exists: "
                            + booking.getBookingReference());
        }

        // Default status
        if (booking.getStatus() == null
                || booking.getStatus().isBlank()) {

            booking.setStatus(PENDING);
        }

        // Normalize booking type
        booking.setBookingType(
                booking.getBookingType()
                        .trim()
                        .toUpperCase());

        // Normalize status
        booking.setStatus(
                booking.getStatus()
                        .trim()
                        .toUpperCase());

        return bookingRepository.save(booking);
    }

    // Update booking status
    public Booking updateBookingStatus(
            Long id,
            String status) {

        Booking booking = getBookingById(id);

        validateStatus(status);

        String currentStatus = booking.getStatus()
                .trim()
                .toUpperCase();

        String newStatus = status
                .trim()
                .toUpperCase();

        validateStatusTransition(
                currentStatus,
                newStatus);

        booking.setStatus(newStatus);

        return bookingRepository.save(booking);
    }

    // Validate complete booking
    private void validateBooking(Booking booking) {

        // Booking reference
        if (booking.getBookingReference() == null
                || booking.getBookingReference().isBlank()) {

            throw new BookingValidationException(
                    "Booking reference is required");
        }

        // User ID
        if (booking.getUserId() == null
                || booking.getUserId() <= 0) {

            throw new BookingValidationException(
                    "User ID must be a positive number");
        }

        // Service ID
        if (booking.getServiceId() == null
                || booking.getServiceId() <= 0) {

            throw new BookingValidationException(
                    "Service ID must be a positive number");
        }

        // Booking type
        validateBookingType(
                booking.getBookingType());

        // Amount
        if (booking.getTotalAmount() != null
                && booking.getTotalAmount()
                .compareTo(BigDecimal.ZERO) < 0) {

            throw new BookingValidationException(
                    "Total amount cannot be negative");
        }

        // Check-in / check-out
        if (booking.getCheckIn() != null
                && booking.getCheckOut() != null
                && booking.getCheckOut()
                .isBefore(booking.getCheckIn())) {

            throw new BookingValidationException(
                    "Check-out cannot be before check-in");
        }

        // CAB-specific validation
        if (booking.getBookingType() != null
                && booking.getBookingType()
                .equalsIgnoreCase(CAB)) {

            validateCabBooking(booking);
        }

        // Status
        if (booking.getStatus() != null
                && !booking.getStatus().isBlank()) {

            validateStatus(booking.getStatus());
        }
    }

    // Validate cab booking
    private void validateCabBooking(
            Booking booking) {

        if (booking.getPassengers() != null
                && booking.getPassengers() <= 0) {

            throw new BookingValidationException(
                    "Passengers must be greater than zero");
        }

        if (booking.getPassengers() != null
                && booking.getPassengers() > 50) {

            throw new BookingValidationException(
                    "Passengers cannot exceed 50");
        }
    }

    // Validate booking type
    private void validateBookingType(
            String bookingType) {

        if (bookingType == null
                || bookingType.isBlank()) {

            throw new BookingValidationException(
                    "Booking type is required");
        }

        String type = bookingType
                .trim()
                .toUpperCase();

        if (!HOTEL.equals(type)
                && !CAB.equals(type)) {

            throw new BookingValidationException(
                    "Unsupported booking type: "
                            + bookingType);
        }
    }

    // Validate status
    private void validateStatus(
            String status) {

        if (status == null
                || status.isBlank()) {

            throw new BookingValidationException(
                    "Booking status is required");
        }

        String normalizedStatus = status
                .trim()
                .toUpperCase();

        if (!PENDING.equals(normalizedStatus)
                && !CONFIRMED.equals(normalizedStatus)
                && !CANCELLED.equals(normalizedStatus)
                && !COMPLETED.equals(normalizedStatus)) {

            throw new BookingValidationException(
                    "Unsupported booking status: "
                            + status);
        }
    }

    // Validate status transition rules
    private void validateStatusTransition(
            String currentStatus,
            String newStatus) {

        // Same status is allowed
        if (currentStatus.equals(newStatus)) {
            return;
        }

        // Completed/cancelled bookings are final
        if (CANCELLED.equals(currentStatus)
                || COMPLETED.equals(currentStatus)) {

            throw new BookingValidationException(
                    "Booking cannot be changed after it is "
                            + currentStatus.toLowerCase());
        }

        // PENDING -> CONFIRMED or CANCELLED
        if (PENDING.equals(currentStatus)) {

            if (!CONFIRMED.equals(newStatus)
                    && !CANCELLED.equals(newStatus)) {

                throw new BookingValidationException(
                        "Pending booking can only become "
                                + "CONFIRMED or CANCELLED");
            }

            return;
        }

        // CONFIRMED -> COMPLETED or CANCELLED
        if (CONFIRMED.equals(currentStatus)) {

            if (!COMPLETED.equals(newStatus)
                    && !CANCELLED.equals(newStatus)) {

                throw new BookingValidationException(
                        "Confirmed booking can only become "
                                + "COMPLETED or CANCELLED");
            }
        }
    }
}