package com.yatraindia.destination.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yatraindia.destination.entity.Destination;

public interface DestinationRepository
        extends JpaRepository<Destination, Long> {

    Optional<Destination> findBySlug(String slug);

    List<Destination> findByStateIgnoreCase(String state);

    List<Destination> findByStatus(String status);

    List<Destination> findByNameContainingIgnoreCase(String name);

    boolean existsBySlug(String slug);
}