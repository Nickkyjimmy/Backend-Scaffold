package com.rmit.generic.generic_service.common.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmit.generic.generic_api.external.dto.GenericTopicRegistry;
import com.rmit.generic.generic_api.external.dto.TestKafkaRequest;
import com.rmit.generic.generic_api.external.dto.TestKafkaResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * Kafka event consumer implementation
 * Handles incoming Kafka messages (both request-reply and fire-and-forget patterns)
 * Add more listeners as needed for your specific use case
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EventConsumerImpl {
    
    private final ObjectMapper objectMapper;
    
    /**
     * Example: Request-Reply pattern listener for test messages
     */
    @KafkaListener(topics = GenericTopicRegistry.Topic.TEST_REQUEST_REPLY_REQ)
    @SendTo(GenericTopicRegistry.Topic.TEST_REQUEST_REPLY_RES)
    public byte[] handleTestRequestReply(byte[] requestBytes) {
        try {
            TestKafkaRequest request = objectMapper.readValue(requestBytes, TestKafkaRequest.class);
            log.info("Generic service: Received test request-reply message from: {}", request.getFromService());
            
            TestKafkaResponse response = TestKafkaResponse.builder()
                    .message("Generic service received: " + request.getMessage())
                    .fromService("generic-service")
                    .receivedFrom(request.getFromService())
                    .timestamp(System.currentTimeMillis())
                    .success(true)
                    .build();
            
            return objectMapper.writeValueAsBytes(response);
        } catch (Exception e) {
            log.error("Error handling test request-reply", e);
            return new byte[0];
        }
    }
    
    /**
     * Example: Request-Reply pattern listener for list operation
     */
    @KafkaListener(topics = GenericTopicRegistry.Topic.LIST_REQ)
    @SendTo(GenericTopicRegistry.Topic.LIST_RES)
    public byte[] handleListRequest(byte[] requestBytes) {
        try {
            log.info("Generic service: Received list request message");
            // Implement your list logic here
            // Placeholder response
            return new byte[0];
        } catch (Exception e) {
            log.error("Error handling list request", e);
            return new byte[0];
        }
    }
    
    // Add more Kafka listeners as needed for your specific use case
}

