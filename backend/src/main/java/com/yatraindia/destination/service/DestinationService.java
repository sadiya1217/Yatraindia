package com.yatraindia.destination.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yatraindia.destination.entity.Destination;
import com.yatraindia.destination.repository.DestinationRepository;
import com.yatraindia.common.exception.ResourceNotFoundException;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationService(
            DestinationRepository destinationRepository) {

        this.destinationRepository = destinationRepository;
    }

    public List<Destination> getAllActiveDestinations() {

        return destinationRepository.findByStatus("ACTIVE");
    }

    public Destination getDestinationById(Long id) {

        return destinationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Destination not found with id: " + id));
    }

    public Destination getDestinationBySlug(String slug) {

        return destinationRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Destination not found with slug: " + slug));
    }

    public List<Destination> getDestinationsByState(
            String state) {

        return destinationRepository
                .findByStateIgnoreCase(state);
    }

    public List<Destination> searchDestinations(
            String name) {

        return destinationRepository
                .findByNameContainingIgnoreCase(name);
    }
}