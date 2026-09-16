package com.yatraindia.destination.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yatraindia.destination.entity.Destination;
import com.yatraindia.destination.service.DestinationService;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    public DestinationController(
            DestinationService destinationService) {

        this.destinationService = destinationService;
    }

    @GetMapping
    public ResponseEntity<List<Destination>>
            getAllDestinations() {

        return ResponseEntity.ok(
                destinationService
                        .getAllActiveDestinations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destination>
            getDestinationById(
                    @PathVariable Long id) {

        return ResponseEntity.ok(
                destinationService
                        .getDestinationById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Destination>
            getDestinationBySlug(
                    @PathVariable String slug) {

        return ResponseEntity.ok(
                destinationService
                        .getDestinationBySlug(slug));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Destination>>
            getDestinationsByState(
                    @PathVariable String state) {

        return ResponseEntity.ok(
                destinationService
                        .getDestinationsByState(state));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Destination>>
            searchDestinations(
                    @RequestParam String name) {

        return ResponseEntity.ok(
                destinationService
                        .searchDestinations(name));
    }
}