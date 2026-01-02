package com.rmit.destination.destination.service.impl;

import com.rmit.destination.destination.dto.DestinationRequest;
import com.rmit.destination.destination.dto.DestinationResponse;
import com.rmit.destination.destination.entity.Destination;
import com.rmit.destination.destination.repo.DestinationRepository;
import com.rmit.destination.destination.service.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;

    @Override
    public DestinationResponse createDestination(DestinationRequest request) {
        // Check if destination name already exists
        if (destinationRepository.existsByName(request.getName())) {
            throw new RuntimeException("Destination with this name already exists");
        }

        // Create new destination
        Destination destination = Destination.builder()
                .name(request.getName())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        destination = destinationRepository.save(destination);

        return mapToResponse(destination, "Destination created successfully");
    }

    @Override
    public DestinationResponse getDestinationById(UUID id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found with id: " + id));

        return mapToResponse(destination, null);
    }

    @Override
    public List<DestinationResponse> getAllDestinations() {
        return destinationRepository.findAll().stream()
                .map(destination -> mapToResponse(destination, null))
                .collect(Collectors.toList());
    }

    @Override
    public DestinationResponse updateDestination(UUID id, DestinationRequest request) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destination not found with id: " + id));

        // Check if new name already exists (excluding current destination)
        if (!destination.getName().equals(request.getName()) &&
                destinationRepository.existsByName(request.getName())) {
            throw new RuntimeException("Destination with this name already exists");
        }

        // Update destination
        destination.setName(request.getName());
        destination.setLatitude(request.getLatitude());
        destination.setLongitude(request.getLongitude());

        destination = destinationRepository.save(destination);

        return mapToResponse(destination, "Destination updated successfully");
    }

    @Override
    public void deleteDestination(UUID id) {
        if (!destinationRepository.existsById(id)) {
            throw new RuntimeException("Destination not found with id: " + id);
        }
        destinationRepository.deleteById(id);
    }

    private DestinationResponse mapToResponse(Destination destination, String message) {
        return DestinationResponse.builder()
                .id(destination.getId())
                .name(destination.getName())
                .latitude(destination.getLatitude())
                .longitude(destination.getLongitude())
                .createdAt(destination.getCreatedAt())
                .updatedAt(destination.getUpdatedAt())
                .message(message)
                .build();
    }
}

