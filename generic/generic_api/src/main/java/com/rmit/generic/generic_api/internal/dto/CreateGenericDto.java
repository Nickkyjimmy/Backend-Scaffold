package com.rmit.generic.generic_api.internal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a Generic entity
 * Replace fields with your specific entity fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateGenericDto {
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotNull(message = "Value is required")
    private String value;
    
    // Add more fields as needed for your specific use case
}

