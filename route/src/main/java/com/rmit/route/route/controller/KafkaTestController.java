package com.rmit.route.route.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rmit.route.route.external.dto.DestinationTopicRegistry;
import com.rmit.route.route.external.dto.TestKafkaRequest;
import com.rmit.route.route.external.dto.TestKafkaResponse;
import com.rmit.route.route.external.service.EventProducer;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/kafka/test")
@Slf4j
public class KafkaTestController {

  @Autowired
  private EventProducer eventProducer;

  /**
   * Test Kafka connectivity to Destination service
   * GET /kafka/test/destination?message=Hello
   */
  @GetMapping("/destination")
  public ResponseEntity<Map<String, Object>> testDestinationKafka(
      @RequestParam(defaultValue = "Test message from route service") String message) {
    Map<String, Object> result = new HashMap<>();
    
    try {
      log.info("Route service: Sending test message to destination service via Kafka");
      
      TestKafkaRequest request = TestKafkaRequest.builder()
          .message(message)
          .fromService("route-service")
          .timestamp(System.currentTimeMillis())
          .build();

      TestKafkaResponse response = eventProducer.sendAndReceive(
          DestinationTopicRegistry.Topic.TEST_REQUEST_REPLY_REQ,
          DestinationTopicRegistry.Topic.TEST_REQUEST_REPLY_RES,
          request,
          TestKafkaResponse.class);

      result.put("success", true);
      result.put("message", "Kafka connection successful");
      result.put("request", request);
      result.put("response", response);
      result.put("status", "connected");
      
      log.info("Route service: Successfully received response from destination service: {}", response.getMessage());
      
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.error("Route service: Error testing Kafka connection to destination service", e);
      result.put("success", false);
      result.put("message", "Kafka connection failed: " + e.getMessage());
      result.put("error", e.getClass().getSimpleName());
      result.put("status", "disconnected");
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(result);
    }
  }

  /**
   * Check Kafka connectivity status
   * GET /kafka/test/status
   */
  @GetMapping("/status")
  public ResponseEntity<Map<String, Object>> getKafkaStatus() {
    Map<String, Object> status = new HashMap<>();
    status.put("service", "route-service");
    status.put("kafkaConfigured", true);
    status.put("producerAvailable", eventProducer != null);
    status.put("timestamp", System.currentTimeMillis());
    return ResponseEntity.ok(status);
  }
}

