package com.rmit.destination.destination.controller;

import com.rmit.destination.destination.dto.DestinationRequest;
import com.rmit.destination.destination.dto.DestinationResponse;
import com.rmit.destination.destination.service.DestinationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/destinations")
@RequiredArgsConstructor
public class DestinationController {

    private final DestinationService destinationService;

    @PostMapping
    public ResponseEntity<DestinationResponse> createDestination(@Valid @RequestBody DestinationRequest request) {
        try {
            DestinationResponse response = destinationService.createDestination(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            DestinationResponse errorResponse = DestinationResponse.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DestinationResponse> getDestinationById(@PathVariable UUID id) {
        try {
            DestinationResponse response = destinationService.getDestinationById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            DestinationResponse errorResponse = DestinationResponse.builder()
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<DestinationResponse>> getAllDestinations() {
        List<DestinationResponse> response = destinationService.getAllDestinations();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DestinationResponse> updateDestination(
            @PathVariable UUID id,
            @Valid @RequestBody DestinationRequest request) {
        try {
            DestinationResponse response = destinationService.updateDestination(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            DestinationResponse errorResponse = DestinationResponse.builder()
                    .message(e.getMessage())
                    .build();
            HttpStatus status = e.getMessage().contains("not found") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDestination(@PathVariable UUID id) {
        try {
            destinationService.deleteDestination(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

