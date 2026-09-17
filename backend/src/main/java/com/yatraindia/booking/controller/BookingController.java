package com.yatraindia.booking.controller;

import com.yatraindia.booking.entity.Booking;
import com.yatraindia.booking.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/reference/{bookingReference}")
    public ResponseEntity<Booking> getBookingByReference(
            @PathVariable String bookingReference) {
        return ResponseEntity.ok(
                bookingService.getBookingByReference(bookingReference));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    @GetMapping("/type/{bookingType}")
    public ResponseEntity<List<Booking>> getBookingsByType(
            @PathVariable String bookingType) {
        return ResponseEntity.ok(
                bookingService.getBookingsByType(bookingType));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(
            @PathVariable String status) {
        return ResponseEntity.ok(
                bookingService.getBookingsByStatus(status));
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody Booking booking) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookingService.createBooking(booking));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Booking> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return ResponseEntity.ok(
                bookingService.updateBookingStatus(id, status));
    }
}