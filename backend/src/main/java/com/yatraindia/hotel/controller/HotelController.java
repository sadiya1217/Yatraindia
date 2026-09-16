package com.yatraindia.hotel.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yatraindia.hotel.entity.Hotel;
import com.yatraindia.hotel.service.HotelService;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {

        return ResponseEntity.ok(
                hotelService.getAllActiveHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                hotelService.getHotelById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Hotel> getHotelBySlug(
            @PathVariable String slug) {

        return ResponseEntity.ok(
                hotelService.getHotelBySlug(slug));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Hotel>> getHotelsByCity(
            @PathVariable String city) {

        return ResponseEntity.ok(
                hotelService.getHotelsByCity(city));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Hotel>> getHotelsByState(
            @PathVariable String state) {

        return ResponseEntity.ok(
                hotelService.getHotelsByState(state));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Hotel>> searchHotels(
            @RequestParam String name) {

        return ResponseEntity.ok(
                hotelService.searchHotels(name));
    }
}