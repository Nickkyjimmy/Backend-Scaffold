package com.rmit.destination.destination.service;

import com.rmit.destination.destination.dto.DestinationRequest;
import com.rmit.destination.destination.dto.DestinationResponse;

import java.util.List;
import java.util.UUID;

public interface DestinationService {
    DestinationResponse createDestination(DestinationRequest request);
    DestinationResponse getDestinationById(UUID id);
    List<DestinationResponse> getAllDestinations();
    DestinationResponse updateDestination(UUID id, DestinationRequest request);
    void deleteDestination(UUID id);
}

