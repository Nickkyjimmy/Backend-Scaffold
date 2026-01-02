package com.rmit.generic.generic_service.generic.controller;

import com.rmit.generic.generic_api.external.dto.GenericTopicRegistry;
import com.rmit.generic.generic_api.external.dto.TestKafkaRequest;
import com.rmit.generic.generic_api.external.dto.TestKafkaResponse;
import com.rmit.generic.generic_api.external.service.EventProducer;
import com.rmit.generic.generic_service.common.http.GenericResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Test controller for Kafka connectivity
 * Useful for testing Kafka integration
 */
@RestController
@RequestMapping("/kafka/test")
@RequiredArgsConstructor
@Slf4j
public class KafkaTestController {
    
    private final EventProducer eventProducer;
    
    /**
     * Test Kafka connectivity
     * GET /kafka/test?message=Hello
     */
    @GetMapping
    public ResponseEntity<GenericResponseDto<Map<String, Object>>> testKafka(
            @RequestParam(defaultValue = "Test message from generic service") String message) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            log.info("Generic service: Sending test message via Kafka");
            
            TestKafkaRequest request = TestKafkaRequest.builder()
                    .message(message)
                    .fromService("generic-service")
                    .timestamp(System.currentTimeMillis())
                    .build();
            
            // Example: Send and receive (request-reply pattern)
            TestKafkaResponse response = eventProducer.sendAndReceive(
                    GenericTopicRegistry.Topic.TEST_REQUEST_REPLY_REQ,
                    GenericTopicRegistry.Topic.TEST_REQUEST_REPLY_RES,
                    request,
                    TestKafkaResponse.class);
            
            result.put("success", true);
            result.put("message", "Kafka connection successful");
            result.put("request", request);
            result.put("response", response);
            result.put("status", "connected");
            
            log.info("Generic service: Successfully received response: {}", response.getMessage());
            
            return GenericResponseDto.getGenericResponseDto(result);
        } catch (Exception e) {
            log.error("Generic service: Error testing Kafka connection", e);
            result.put("success", false);
            result.put("message", "Kafka connection failed: " + e.getMessage());
            result.put("error", e.getClass().getSimpleName());
            result.put("status", "disconnected");
            GenericResponseDto<Map<String, Object>> errorResponse = new GenericResponseDto<>(result);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }
    }
    
    /**
     * Check Kafka connectivity status
     * GET /kafka/test/status
     */
    @GetMapping("/status")
    public ResponseEntity<GenericResponseDto<Map<String, Object>>> getKafkaStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("service", "generic-service");
        status.put("kafkaConfigured", true);
        status.put("producerAvailable", eventProducer != null);
        status.put("timestamp", System.currentTimeMillis());
        return GenericResponseDto.getGenericResponseDto(status);
    }
}

