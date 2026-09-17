package com.yatraindia.booking.repository;

import com.yatraindia.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingReference(String bookingReference);

    List<Booking> findByUserId(Long userId);

    List<Booking> findByBookingTypeIgnoreCase(String bookingType);

    List<Booking> findByStatusIgnoreCase(String status);

    List<Booking> findByServiceIdAndBookingType(Long serviceId, String bookingType);

    boolean existsByBookingReference(String bookingReference);
}