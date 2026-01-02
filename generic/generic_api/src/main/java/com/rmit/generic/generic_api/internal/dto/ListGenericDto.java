package com.rmit.generic.generic_api.internal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for listing Generic entities
 */
public class ListGenericDto {
    
    /**
     * Response DTO item for list operations
     * Replace fields with your specific entity fields
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ListGenericRes {
        private UUID id;
        private String name;
        private String value;
        private LocalDateTime createdAt;
        // Add more fields as needed for your specific use case
    }
}

