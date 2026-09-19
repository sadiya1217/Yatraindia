package com.yatraindia.places.controller;

import com.yatraindia.places.entity.Place;
import com.yatraindia.places.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public List<Place> getAllPlaces() {
        return placeService.getAllPlaces();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        return placeService.getPlaceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/destination/{destinationId}")
    public List<Place> getPlacesByDestination(
            @PathVariable Long destinationId) {
        return placeService.getPlacesByDestination(destinationId);
    }

    @GetMapping("/destination/{destinationId}/category/{category}")
    public List<Place> getPlacesByCategory(
            @PathVariable Long destinationId,
            @PathVariable String category) {
        return placeService.getPlacesByCategory(destinationId, category);
    }

    @GetMapping("/city/{city}")
    public List<Place> getPlacesByCity(@PathVariable String city) {
        return placeService.getPlacesByCity(city);
    }

    @GetMapping("/state/{state}")
    public List<Place> getPlacesByState(@PathVariable String state) {
        return placeService.getPlacesByState(state);
    }
}