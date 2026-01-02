package com.rmit.auth_service.auth.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmit.auth_service.auth.external.dto.BetaTopicRegistry;

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
   * Replace with your actual DTOs and business logic
   */
  @KafkaListener(topics = BetaTopicRegistry.Topic.TEST_REQUEST_REPLY_REQ)
  @SendTo(BetaTopicRegistry.Topic.TEST_REQUEST_REPLY_RES)
  public byte[] handleTestRequestReply(byte[] requestBytes) {
    try {
      log.info("Received test request-reply message");
      // Deserialize request
      // TestKafkaRequestReplyReq request = objectMapper.readValue(requestBytes, TestKafkaRequestReplyReq.class);
      
      // Process request
      // Your business logic here
      
      // Serialize response
      // TestKafkaRequestReplyRes response = processRequest(request);
      // return objectMapper.writeValueAsBytes(response);
      
      // Placeholder response
      return new byte[0];
    } catch (Exception e) {
      log.error("Error handling test request-reply", e);
      return new byte[0];
    }
  }

  /**
   * Example: Request-Reply pattern listener for list operation
   */
  @KafkaListener(topics = BetaTopicRegistry.Topic.LIST_REQ)
  @SendTo(BetaTopicRegistry.Topic.LIST_RES)
  public byte[] handleListRequest(byte[] requestBytes) {
    try {
      log.info("Received list request message");
      // Deserialize request
      // ListRequest request = objectMapper.readValue(requestBytes, ListRequest.class);
      
      // Process request
      // List<Item> items = yourService.list(request);
      
      // Serialize response
      // ListResponse response = new ListResponse(items);
      // return objectMapper.writeValueAsBytes(response);
      
      // Placeholder response
      return new byte[0];
    } catch (Exception e) {
      log.error("Error handling list request", e);
      return new byte[0];
    }
  }

  /**
   * Generic method to handle any Kafka message
   * This can be used as a template for adding more listeners
   * 
   * @param topic the topic name
   * @param requestBytes the raw message bytes
   * @param requestClass the class to deserialize the request to
   * @return the processed response bytes
   */
  @SuppressWarnings("unused")
  private <T> byte[] handleGenericMessage(String topic, byte[] requestBytes, Class<T> requestClass) {
    try {
      T request = objectMapper.readValue(requestBytes, requestClass);
      log.debug("Received message on topic: {} with request type: {}", topic, requestClass.getSimpleName());
      
      // Process request based on type
      // Add your business logic here
      // Example: YourService.process(request);
      
      // Return response
      // Example: return objectMapper.writeValueAsBytes(response);
      return new byte[0];
    } catch (Exception e) {
      log.error("Error handling message on topic: {}", topic, e);
      return new byte[0];
    }
  }
}

