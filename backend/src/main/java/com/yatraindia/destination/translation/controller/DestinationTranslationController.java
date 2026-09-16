package com.yatraindia.destination.translation.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yatraindia.destination.translation.entity.DestinationTranslation;
import com.yatraindia.destination.translation.service.DestinationTranslationService;

@RestController
@RequestMapping("/api/destinations")
public class DestinationTranslationController {

    private final DestinationTranslationService translationService;

    public DestinationTranslationController(
            DestinationTranslationService translationService) {

        this.translationService = translationService;
    }

    @GetMapping("/{destinationId}/translations")
    public ResponseEntity<List<DestinationTranslation>>
            getTranslations(
                    @PathVariable Long destinationId) {

        return ResponseEntity.ok(
                translationService
                        .getTranslations(destinationId));
    }

    @GetMapping("/{destinationId}/translations/{languageCode}")
    public ResponseEntity<DestinationTranslation>
            getTranslation(
                    @PathVariable Long destinationId,
                    @PathVariable String languageCode) {

        return ResponseEntity.ok(
                translationService.getTranslation(
                        destinationId,
                        languageCode));
    }
}