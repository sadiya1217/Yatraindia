package com.yatraindia.places.repository;

import com.yatraindia.places.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    List<Place> findByDestinationId(Long destinationId);

    List<Place> findByDestinationIdAndCategoryIgnoreCase(
            Long destinationId,
            String category
    );

    List<Place> findByCityIgnoreCase(String city);

    List<Place> findByStateIgnoreCase(String state);
}