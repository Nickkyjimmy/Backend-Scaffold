package com.rmit.route.route.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmit.route.route.external.dto.RouteTopicRegistry;
import com.rmit.route.route.external.dto.TestKafkaRequest;
import com.rmit.route.route.external.dto.TestKafkaResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Generic Kafka event consumer implementation
 * Handles both request-reply and fire-and-forget message patterns
 */
@Component
@Slf4j
public class EventConsumerImpl {

  @Autowired
  private ObjectMapper objectMapper;

  /**
   * Example: Request-Reply pattern listener
   */
  @KafkaListener(topics = RouteTopicRegistry.Topic.TEST_REQUEST_REPLY_REQ)
  @SendTo(RouteTopicRegistry.Topic.TEST_REQUEST_REPLY_RES)
  public byte[] handleTestRequestReply(byte[] requestBytes) {
    try {
      TestKafkaRequest request = objectMapper.readValue(requestBytes, TestKafkaRequest.class);
      log.info("Route service: Received test request-reply message from: {}", request.getFromService());
      
      TestKafkaResponse response = TestKafkaResponse.builder()
          .message("Route service received: " + request.getMessage())
          .fromService("route-service")
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
  @KafkaListener(topics = RouteTopicRegistry.Topic.LIST_REQ)
  @SendTo(RouteTopicRegistry.Topic.LIST_RES)
  public byte[] handleListRequest(byte[] requestBytes) {
    try {
      log.info("Route service: Received list request message");
      // Placeholder response
      return new byte[0];
    } catch (Exception e) {
      log.error("Error handling list request", e);
      return new byte[0];
    }
  }
}

