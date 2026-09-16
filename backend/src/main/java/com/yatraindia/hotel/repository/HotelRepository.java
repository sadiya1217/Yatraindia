package com.yatraindia.hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yatraindia.hotel.entity.Hotel;

public interface HotelRepository
        extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findBySlug(String slug);

    List<Hotel> findByCityIgnoreCase(String city);

    List<Hotel> findByStateIgnoreCase(String state);

    List<Hotel> findByStatus(String status);

    List<Hotel> findByNameContainingIgnoreCase(String name);

    boolean existsBySlug(String slug);
}