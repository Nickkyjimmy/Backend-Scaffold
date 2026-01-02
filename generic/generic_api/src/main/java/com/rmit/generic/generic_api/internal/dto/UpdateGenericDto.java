package com.rmit.generic.generic_api.internal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating a Generic entity
 * All fields are optional - only provided fields will be updated
 * Replace fields with your specific entity fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGenericDto {
    
    private String name;
    
    private String value;
    
    // Add more fields as needed for your specific use case
}

