package com.yatraindia.cab.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yatraindia.cab.entity.Cab;

public interface CabRepository extends JpaRepository<Cab, Long> {

    Optional<Cab> findBySlug(String slug);

    List<Cab> findByCityIgnoreCase(String city);

    List<Cab> findByStateIgnoreCase(String state);

    List<Cab> findByCabTypeIgnoreCase(String cabType);

    List<Cab> findByStatus(String status);

    List<Cab> findByNameContainingIgnoreCase(String name);

    boolean existsBySlug(String slug);
}