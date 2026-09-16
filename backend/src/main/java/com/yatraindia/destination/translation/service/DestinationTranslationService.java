package com.yatraindia.destination.translation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.yatraindia.common.exception.ResourceNotFoundException;
import com.yatraindia.destination.entity.Destination;
import com.yatraindia.destination.repository.DestinationRepository;
import com.yatraindia.destination.translation.entity.DestinationTranslation;
import com.yatraindia.destination.translation.repository.DestinationTranslationRepository;

@Service
public class DestinationTranslationService {

    private final DestinationRepository destinationRepository;
    private final DestinationTranslationRepository translationRepository;

    public DestinationTranslationService(
            DestinationRepository destinationRepository,
            DestinationTranslationRepository translationRepository) {

        this.destinationRepository = destinationRepository;
        this.translationRepository = translationRepository;
    }

    public List<DestinationTranslation>
            getTranslations(Long destinationId) {

        Destination destination = destinationRepository
                .findById(destinationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Destination not found with id: "
                                        + destinationId));

        return translationRepository
                .findByDestination(destination);
    }

    public DestinationTranslation
            getTranslation(
                    Long destinationId,
                    String languageCode) {

        Destination destination = destinationRepository
                .findById(destinationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Destination not found with id: "
                                        + destinationId));

        return translationRepository
                .findByDestinationAndLanguageCode(
                        destination,
                        languageCode.toLowerCase())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Translation not found for language: "
                                        + languageCode));
    }
}