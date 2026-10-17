
package com.yatraindia.booking.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import com.yatraindia.cab.entity.Cab;
import com.yatraindia.cab.repository.CabRepository;
import com.yatraindia.auth.security.AuthenticatedUserService;
import com.yatraindia.booking.entity.Booking;
import com.yatraindia.booking.exception.BookingValidationException;
import com.yatraindia.booking.repository.BookingRepository;
import com.yatraindia.common.exception.ResourceNotFoundException;

@Service
public class BookingService {

    private static final String HOTEL = "HOTEL";
    private static final String CAB = "CAB";

    private static final String PENDING = "PENDING";
    private static final String CONFIRMED = "CONFIRMED";
    private static final String CANCELLED = "CANCELLED";
    private static final String COMPLETED = "COMPLETED";

    private final BookingRepository bookingRepository;
private final CabRepository cabRepository;
private final AuthenticatedUserService authenticatedUserService;

    public BookingService(
        BookingRepository bookingRepository,
        CabRepository cabRepository,
        AuthenticatedUserService authenticatedUserService) {

    this.bookingRepository = bookingRepository;
    this.cabRepository = cabRepository;
    this.authenticatedUserService = authenticatedUserService;
}
    // Require administrator privileges for administrative operations.
    private void requireAdministrator() {
        if (!authenticatedUserService.isCurrentUserAdmin()) {
            throw new BookingValidationException(
                    "Administrator access is required");
        }
    }

    // Get all bookings: administrators only.
    public List<Booking> getAllBookings() {
        requireAdministrator();
        return bookingRepository.findAll();
    }

    // Get booking by ID: administrators only.
    public Booking getBookingById(Long id) {
        requireAdministrator();

        if (id == null || id <= 0) {
            throw new BookingValidationException(
                    "Booking ID must be a positive number");
        }

        return bookingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with id: " + id));
    }

    // Get booking by reference: administrators only.
    public Booking getBookingByReference(String bookingReference) {
        requireAdministrator();

        if (bookingReference == null
                || bookingReference.isBlank()) {
            throw new BookingValidationException(
                    "Booking reference is required");
        }

        return bookingRepository
                .findByBookingReference(bookingReference.trim())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found with reference: "
                                        + bookingReference));
    }

    // Customers can retrieve their own bookings.
    // Administrators can retrieve bookings for any user.
    public List<Booking> getBookingsByUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new BookingValidationException(
                    "User ID must be a positive number");
        }

        Long currentUserId =
                authenticatedUserService.getCurrentUserId();

        boolean isAdministrator =
                authenticatedUserService.isCurrentUserAdmin();

        if (!isAdministrator && !currentUserId.equals(userId)) {
            throw new BookingValidationException(
                    "You are not allowed to view these bookings");
        }

        return bookingRepository.findByUserId(userId);
    }

    // Get bookings by booking type: administrators only.
    public List<Booking> getBookingsByType(String bookingType) {
        requireAdministrator();
        validateBookingType(bookingType);

        return bookingRepository.findByBookingTypeIgnoreCase(
                bookingType.trim());
    }

    // Get bookings by status: administrators only.
    public List<Booking> getBookingsByStatus(String status) {
        requireAdministrator();
        validateStatus(status);

        return bookingRepository.findByStatusIgnoreCase(
                status.trim());
    }

    // Get bookings by service and booking type: administrators only.
     private void calculateCabFare(Booking booking) {
    if (booking.getServiceId() == null
            || booking.getServiceId() <= 0) {
        throw new BookingValidationException(
                "A valid cab ID is required");
    }

    Cab cab = cabRepository.findById(booking.getServiceId())
            .orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Cab not found with id: "
                                    + booking.getServiceId()));

    if (!"ACTIVE".equalsIgnoreCase(cab.getStatus())) {
        throw new BookingValidationException(
                "This cab is not currently available");
    }

    if (cab.getBaseFare() == null
            || cab.getBaseFare().compareTo(BigDecimal.ZERO) < 0) {
        throw new BookingValidationException(
                "Cab pricing is not configured correctly");
    }

    Integer passengers = booking.getPassengers();

    if (passengers == null || passengers <= 0) {
        throw new BookingValidationException(
                "Passenger count must be greater than zero");
    }

    if (cab.getSeatingCapacity() == null
            || passengers > cab.getSeatingCapacity()) {
        throw new BookingValidationException(
                "Passenger count exceeds the cab seating capacity");
    }

    // Stage 1 uses a flat fare from the trusted database.
    // Do not trust prices or route metrics sent by the browser.
    booking.setTotalAmount(cab.getBaseFare());
    booking.setDistanceKm(null);
    booking.setDurationMinutes(null);
}

    public List<Booking> getBookingsByService(
            Long serviceId,
            String bookingType) {
        requireAdministrator();

        if (serviceId == null || serviceId <= 0) {
            throw new BookingValidationException(
                    "Service ID must be a positive number");
        }

        validateBookingType(bookingType);

        return bookingRepository.findByServiceIdAndBookingType(
                serviceId,
                bookingType.trim().toUpperCase());
    }

    // Create a booking for the authenticated user.
    public Booking createBooking(Booking booking) {
        if (booking == null) {
            throw new BookingValidationException(
                    "Booking data is required");
        }

        Long authenticatedUserId =
                authenticatedUserService.getCurrentUserId();

        // Never trust a client-supplied owner ID.
        if (booking.getUserId() != null
                && !booking.getUserId().equals(authenticatedUserId)) {
            throw new BookingValidationException(
                    "Booking user ID must match the authenticated user");
        }

        // The authenticated identity determines the owner.
        booking.setUserId(authenticatedUserId);

        // Customers cannot create bookings with a forged status.
        if (booking.getStatus() != null
                && !booking.getStatus().isBlank()
                && !PENDING.equalsIgnoreCase(
                        booking.getStatus().trim())) {
            throw new BookingValidationException(
                    "New bookings must have PENDING status");
        }

        booking.setStatus(PENDING);

        validateBookingType(booking.getBookingType());
	booking.setBookingType(
        booking.getBookingType().trim().toUpperCase());

if (CAB.equals(booking.getBookingType())) {
    calculateCabFare(booking);
}

validateBooking(booking);

        if (bookingRepository.existsByBookingReference(
                booking.getBookingReference())) {
            throw new BookingValidationException(
                    "Booking reference already exists: "
                            + booking.getBookingReference());
        }

        return bookingRepository.save(booking);
    }

    // Change status: administrators only.
    public Booking updateBookingStatus(
            Long id,
            String status) {
        requireAdministrator();

        if (id == null || id <= 0) {
            throw new BookingValidationException(
                    "Booking ID must be a positive number");
        }

        validateStatus(status);

        // This method also verifies administrator privileges.
        Booking booking = getBookingById(id);

        if (booking.getStatus() == null
                || booking.getStatus().isBlank()) {
            throw new BookingValidationException(
                    "Current booking status is invalid");
        }

        String currentStatus = booking.getStatus()
                .trim()
                .toUpperCase();

        String newStatus = status
                .trim()
                .toUpperCase();

        validateStatusTransition(currentStatus, newStatus);

        booking.setStatus(newStatus);

        return bookingRepository.save(booking);
    }

    // Validate complete booking data.
    private void validateBooking(Booking booking) {
        if (booking.getBookingReference() == null
                || booking.getBookingReference().isBlank()) {
            throw new BookingValidationException(
                    "Booking reference is required");
        }

        if (booking.getUserId() == null
                || booking.getUserId() <= 0) {
            throw new BookingValidationException(
                    "User ID must be a positive number");
        }

        if (booking.getServiceId() == null
                || booking.getServiceId() <= 0) {
            throw new BookingValidationException(
                    "Service ID must be a positive number");
        }

        validateBookingType(booking.getBookingType());

        if (booking.getTotalAmount() != null
                && booking.getTotalAmount()
                        .compareTo(BigDecimal.ZERO) < 0) {
            throw new BookingValidationException(
                    "Total amount cannot be negative");
        }

        if (booking.getCheckIn() != null
                && booking.getCheckOut() != null
                && booking.getCheckOut()
                        .isBefore(booking.getCheckIn())) {
            throw new BookingValidationException(
                    "Check-out cannot be before check-in");
        }

        if (CAB.equalsIgnoreCase(booking.getBookingType())) {
            validateCabBooking(booking);
        }

        if (booking.getStatus() != null
                && !booking.getStatus().isBlank()) {
            validateStatus(booking.getStatus());
        }
    }

    // Validate cab booking details.
    private void validateCabBooking(Booking booking) {
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

    // Validate booking type.
    private void validateBookingType(String bookingType) {
        if (bookingType == null || bookingType.isBlank()) {
            throw new BookingValidationException(
                    "Booking type is required");
        }

        String type = bookingType.trim().toUpperCase();

        if (!HOTEL.equals(type) && !CAB.equals(type)) {
            throw new BookingValidationException(
                    "Unsupported booking type: " + bookingType);
        }
    }

    // Validate allowed booking statuses.
    private void validateStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new BookingValidationException(
                    "Booking status is required");
        }

        String normalizedStatus = status.trim().toUpperCase();

        if (!PENDING.equals(normalizedStatus)
                && !CONFIRMED.equals(normalizedStatus)
                && !CANCELLED.equals(normalizedStatus)
                && !COMPLETED.equals(normalizedStatus)) {
            throw new BookingValidationException(
                    "Unsupported booking status: " + status);
        }
    }

    // Enforce legal booking status transitions.
    private void validateStatusTransition(
            String currentStatus,
            String newStatus) {
        if (currentStatus.equals(newStatus)) {
            return;
        }

        // Completed and cancelled bookings are final.
        if (CANCELLED.equals(currentStatus)
                || COMPLETED.equals(currentStatus)) {
            throw new BookingValidationException(
                    "Booking cannot be changed after it is "
                            + currentStatus.toLowerCase());
        }

        // PENDING -> CONFIRMED or CANCELLED.
        if (PENDING.equals(currentStatus)) {
            if (!CONFIRMED.equals(newStatus)
                    && !CANCELLED.equals(newStatus)) {
                throw new BookingValidationException(
                        "Pending booking can only become "
                                + "CONFIRMED or CANCELLED");
            }
            return;
        }

        // CONFIRMED -> COMPLETED or CANCELLED.
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
