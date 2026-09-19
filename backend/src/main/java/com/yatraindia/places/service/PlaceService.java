package com.yatraindia.places.service;

import com.yatraindia.places.entity.Place;
import com.yatraindia.places.repository.PlaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Place> getAllPlaces() {
        return placeRepository.findAll();
    }

    public Optional<Place> getPlaceById(Long id) {
        return placeRepository.findById(id);
    }

    public List<Place> getPlacesByDestination(Long destinationId) {
        return placeRepository.findByDestinationId(destinationId);
    }

    public List<Place> getPlacesByCategory(Long destinationId, String category) {
        return placeRepository.findByDestinationIdAndCategoryIgnoreCase(
                destinationId,
                category
        );
    }

    public List<Place> getPlacesByCity(String city) {
        return placeRepository.findByCityIgnoreCase(city);
    }

    public List<Place> getPlacesByState(String state) {
        return placeRepository.findByStateIgnoreCase(state);
    }
}
