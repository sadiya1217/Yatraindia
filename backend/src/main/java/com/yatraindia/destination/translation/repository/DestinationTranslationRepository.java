package com.yatraindia.destination.translation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yatraindia.destination.entity.Destination;
import com.yatraindia.destination.translation.entity.DestinationTranslation;

public interface DestinationTranslationRepository
        extends JpaRepository<DestinationTranslation, Long> {

    List<DestinationTranslation>
            findByDestination(Destination destination);

    Optional<DestinationTranslation>
            findByDestinationAndLanguageCode(
                    Destination destination,
                    String languageCode);

    boolean existsByDestinationAndLanguageCode(
            Destination destination,
            String languageCode);
}