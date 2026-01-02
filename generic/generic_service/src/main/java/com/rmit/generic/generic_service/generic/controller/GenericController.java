package com.rmit.generic.generic_service.generic.controller;

import com.rmit.generic.generic_api.internal.dto.CreateGenericDto;
import com.rmit.generic.generic_api.internal.dto.GetGenericDto;
import com.rmit.generic.generic_api.internal.dto.ListGenericDto;
import com.rmit.generic.generic_api.internal.dto.UpdateGenericDto;
import com.rmit.generic.generic_api.internal.service.GenericInternalService;
import com.rmit.generic.generic_service.common.http.GenericResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Generic entity
 * Provides full CRUD endpoints
 * Replace "Generic" with your specific entity name
 */
@RestController
@RequestMapping("/generic")
@RequiredArgsConstructor
@Slf4j
public class GenericController {
    
    private final GenericInternalService genericInternalService;
    
    /**
     * Create a new Generic entity
     * POST /generic
     */
    @PostMapping
    public ResponseEntity<GenericResponseDto<UUID>> createGeneric(
            @Valid @RequestBody CreateGenericDto req) {
        log.info("Received request to create Generic entity");
        UUID id = genericInternalService.createGeneric(req);
        return GenericResponseDto.getGenericResponseDto(id);
    }
    
    /**
     * Get a Generic entity by ID
     * GET /generic/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponseDto<GetGenericDto>> getGeneric(
            @PathVariable UUID id) {
        log.debug("Received request to get Generic entity with id: {}", id);
        GetGenericDto dto = genericInternalService.getGeneric(id);
        return GenericResponseDto.getGenericResponseDto(dto);
    }
    
    /**
     * List all Generic entities
     * GET /generic
     */
    @GetMapping
    public ResponseEntity<GenericResponseDto<List<ListGenericDto.ListGenericRes>>> listGenerics() {
        log.debug("Received request to list all Generic entities");
        List<ListGenericDto.ListGenericRes> result = genericInternalService.listGenerics();
        return GenericResponseDto.getGenericResponseDto(result);
    }
    
    /**
     * Update a Generic entity
     * PUT /generic/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<GenericResponseDto<String>> updateGeneric(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGenericDto req) {
        log.info("Received request to update Generic entity with id: {}", id);
        genericInternalService.updateGeneric(id, req);
        return GenericResponseDto.getGenericResponseDto("Generic entity updated successfully");
    }
    
    /**
     * Delete a Generic entity
     * DELETE /generic/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponseDto<String>> deleteGeneric(
            @PathVariable UUID id) {
        log.info("Received request to delete Generic entity with id: {}", id);
        genericInternalService.deleteGeneric(id);
        return GenericResponseDto.getGenericResponseDto("Generic entity deleted successfully");
    }
}

