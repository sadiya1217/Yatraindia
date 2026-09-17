package com.yatraindia.booking.service;

import com.yatraindia.booking.entity.Booking;
import com.yatraindia.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    public Booking getBookingByReference(String bookingReference) {
        return bookingRepository.findByBookingReference(bookingReference)
                .orElseThrow(() -> new RuntimeException(
                        "Booking not found with reference: " + bookingReference));
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingsByType(String bookingType) {
        return bookingRepository.findByBookingTypeIgnoreCase(bookingType);
    }

    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatusIgnoreCase(status);
    }

    public Booking createBooking(Booking booking) {
        if (bookingRepository.existsByBookingReference(booking.getBookingReference())) {
            throw new RuntimeException(
                    "Booking reference already exists: " + booking.getBookingReference());
        }

        return bookingRepository.save(booking);
    }

    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = getBookingById(id);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }
}