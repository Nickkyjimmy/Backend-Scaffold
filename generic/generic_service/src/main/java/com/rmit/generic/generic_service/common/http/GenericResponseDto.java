package com.rmit.generic.generic_service.common.http;

import org.springframework.http.ResponseEntity;

/**
 * Generic response DTO wrapper for consistent API responses
 */
public record   GenericResponseDto<T>(T data) {
    
    public static <T> ResponseEntity<GenericResponseDto<T>> getGenericResponseDto(T result) {
        return ResponseEntity.ok(new GenericResponseDto<T>(result));
    }
}

