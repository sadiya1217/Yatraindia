package com.yatraindia.cab.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yatraindia.cab.entity.Cab;
import com.yatraindia.cab.service.CabService;

@RestController
@RequestMapping("/api/cabs")
public class CabController {

    private final CabService cabService;

    public CabController(CabService cabService) {
        this.cabService = cabService;
    }

    @GetMapping
    public ResponseEntity<List<Cab>> getAllCabs() {
        return ResponseEntity.ok(
                cabService.getAllActiveCabs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cab> getCabById(
            @PathVariable Long id) {
        return ResponseEntity.ok(
                cabService.getCabById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<Cab> getCabBySlug(
            @PathVariable String slug) {
        return ResponseEntity.ok(
                cabService.getCabBySlug(slug));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Cab>> getCabsByCity(
            @PathVariable String city) {
        return ResponseEntity.ok(
                cabService.getCabsByCity(city));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Cab>> getCabsByState(
            @PathVariable String state) {
        return ResponseEntity.ok(
                cabService.getCabsByState(state));
    }

    @GetMapping("/type/{cabType}")
    public ResponseEntity<List<Cab>> getCabsByType(
            @PathVariable String cabType) {
        return ResponseEntity.ok(
                cabService.getCabsByType(cabType));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Cab>> searchCabs(
            @RequestParam String name) {
        return ResponseEntity.ok(
                cabService.searchCabs(name));
    }
}