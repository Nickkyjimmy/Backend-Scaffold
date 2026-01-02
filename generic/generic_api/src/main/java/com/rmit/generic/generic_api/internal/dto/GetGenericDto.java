package com.rmit.generic.generic_api.internal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for retrieving a Generic entity
 * Replace fields with your specific entity fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetGenericDto {
    
    private UUID id;
    
    private String name;
    
    private String value;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    // Add more fields as needed for your specific use case
}

