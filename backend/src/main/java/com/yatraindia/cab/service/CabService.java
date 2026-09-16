package com.yatraindia.cab.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yatraindia.cab.entity.Cab;
import com.yatraindia.cab.repository.CabRepository;
import com.yatraindia.common.exception.ResourceNotFoundException;

@Service
public class CabService {

    private final CabRepository cabRepository;

    public CabService(CabRepository cabRepository) {
        this.cabRepository = cabRepository;
    }

    public List<Cab> getAllActiveCabs() {
        return cabRepository.findByStatus("ACTIVE");
    }

    public Cab getCabById(Long id) {
        return cabRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cab not found with id: " + id));
    }

    public Cab getCabBySlug(String slug) {
        return cabRepository.findBySlug(slug)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cab not found with slug: " + slug));
    }

    public List<Cab> getCabsByCity(String city) {
        return cabRepository.findByCityIgnoreCase(city);
    }

    public List<Cab> getCabsByState(String state) {
        return cabRepository.findByStateIgnoreCase(state);
    }

    public List<Cab> getCabsByType(String cabType) {
        return cabRepository.findByCabTypeIgnoreCase(cabType);
    }

    public List<Cab> searchCabs(String name) {
        return cabRepository.findByNameContainingIgnoreCase(name);
    }
}