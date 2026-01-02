package com.rmit.generic.generic_service.generic.repo;

import com.rmit.generic.generic_service.generic.model.GenericModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for Generic entity
 * Add custom query methods as needed
 */
@Repository
public interface GenericRepo extends JpaRepository<GenericModel, UUID> {
    
    // Add custom query methods here
    // Example:
    // boolean existsByName(String name);
    // List<GenericModel> findByValue(String value);
}

