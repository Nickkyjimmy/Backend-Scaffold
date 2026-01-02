package com.rmit.generic.generic_api.internal.service;

import com.rmit.generic.generic_api.internal.dto.CreateGenericDto;
import com.rmit.generic.generic_api.internal.dto.GetGenericDto;
import com.rmit.generic.generic_api.internal.dto.ListGenericDto;
import com.rmit.generic.generic_api.internal.dto.UpdateGenericDto;

import java.util.List;
import java.util.UUID;

/**
 * Internal service interface for Generic entity operations
 * This interface defines the contract for CRUD operations
 */
public interface GenericInternalService {
    
    /**
     * Create a new Generic entity
     * @param req the create request DTO
     * @return the UUID of the created entity
     */
    UUID createGeneric(CreateGenericDto req);
    
    /**
     * Get a Generic entity by ID
     * @param id the UUID of the entity
     * @return the GetGenericDto
     */
    GetGenericDto getGeneric(UUID id);
    
    /**
     * List all Generic entities
     * @return a List of ListGenericRes
     */
    List<ListGenericDto.ListGenericRes> listGenerics();
    
    /**
     * Update a Generic entity
     * @param id the UUID of the entity to update
     * @param req the update request DTO
     */
    void updateGeneric(UUID id, UpdateGenericDto req);
    
    /**
     * Delete a Generic entity
     * @param id the UUID of the entity to delete
     */
    void deleteGeneric(UUID id);
}

