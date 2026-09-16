package com.yatraindia.hotel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yatraindia.common.exception.ResourceNotFoundException;
import com.yatraindia.hotel.entity.Hotel;
import com.yatraindia.hotel.repository.HotelRepository;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<Hotel> getAllActiveHotels() {
        return hotelRepository.findByStatus("ACTIVE");
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Hotel not found with id: " + id));
    }

    public Hotel getHotelBySlug(String slug) {
        return hotelRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Hotel not found with slug: " + slug));
    }

    public List<Hotel> getHotelsByCity(String city) {
        return hotelRepository.findByCityIgnoreCase(city);
    }

    public List<Hotel> getHotelsByState(String state) {
        return hotelRepository.findByStateIgnoreCase(state);
    }

    public List<Hotel> searchHotels(String name) {
        return hotelRepository.findByNameContainingIgnoreCase(name);
    }
}