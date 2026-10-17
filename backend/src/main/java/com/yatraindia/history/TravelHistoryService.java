package com.yatraindia.history;

import com.yatraindia.booking.entity.Booking;
import com.yatraindia.booking.repository.BookingRepository;
import com.yatraindia.cab.entity.Cab;
import com.yatraindia.cab.repository.CabRepository;
import com.yatraindia.hotel.entity.Hotel;
import com.yatraindia.hotel.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class TravelHistoryService {

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final CabRepository cabRepository;

    public TravelHistoryService(
            BookingRepository bookingRepository,
            HotelRepository hotelRepository,
            CabRepository cabRepository) {

        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.cabRepository = cabRepository;
    }

    public List<String> getVisitedStates(Long userId) {

        List<Booking> completedBookings =
                bookingRepository.findByUserId(userId)
                        .stream()
                        .filter(booking ->
                                "COMPLETED".equalsIgnoreCase(
                                        booking.getStatus()))
                        .toList();

        Set<String> states = new LinkedHashSet<>();

        for (Booking booking : completedBookings) {

            if ("HOTEL".equalsIgnoreCase(booking.getBookingType())) {

                hotelRepository.findById(booking.getServiceId())
                        .map(Hotel::getState)
                        .ifPresent(states::add);

            } else if ("CAB".equalsIgnoreCase(booking.getBookingType())) {

                cabRepository.findById(booking.getServiceId())
                        .map(Cab::getState)
                        .ifPresent(states::add);
            }
        }

        return states.stream().toList();
    }
}