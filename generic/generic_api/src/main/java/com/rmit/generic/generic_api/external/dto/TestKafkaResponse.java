package com.rmit.generic.generic_api.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test Kafka response DTO for request-reply pattern
 * Replace with your specific Kafka message DTOs
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestKafkaResponse {
    
    private String message;
    
    private String fromService;
    
    private String receivedFrom;
    
    private Long timestamp;
    
    private Boolean success;
}

