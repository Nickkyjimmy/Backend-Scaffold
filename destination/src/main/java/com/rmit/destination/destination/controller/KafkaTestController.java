package com.rmit.destination.destination.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rmit.destination.destination.external.dto.RouteTopicRegistry;
import com.rmit.destination.destination.external.dto.TestKafkaRequest;
import com.rmit.destination.destination.external.dto.TestKafkaResponse;
import com.rmit.destination.destination.external.service.EventProducer;

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
   * Test Kafka connectivity to Route service
   * GET /kafka/test/route?message=Hello
   */
  @GetMapping("/route")
  public ResponseEntity<Map<String, Object>> testRouteKafka(
      @RequestParam(defaultValue = "Test message from destination service") String message) {
    Map<String, Object> result = new HashMap<>();
    
    try {
      log.info("Destination service: Sending test message to route service via Kafka");
      
      TestKafkaRequest request = TestKafkaRequest.builder()
          .message(message)
          .fromService("destination-service")
          .timestamp(System.currentTimeMillis())
          .build();

      TestKafkaResponse response = eventProducer.sendAndReceive(
          RouteTopicRegistry.Topic.TEST_REQUEST_REPLY_REQ,
          RouteTopicRegistry.Topic.TEST_REQUEST_REPLY_RES,
          request,
          TestKafkaResponse.class);

      result.put("success", true);
      result.put("message", "Kafka connection successful");
      result.put("request", request);
      result.put("response", response);
      result.put("status", "connected");
      
      log.info("Destination service: Successfully received response from route service: {}", response.getMessage());
      
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      log.error("Destination service: Error testing Kafka connection to route service", e);
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
    status.put("service", "destination-service");
    status.put("kafkaConfigured", true);
    status.put("producerAvailable", eventProducer != null);
    status.put("timestamp", System.currentTimeMillis());
    return ResponseEntity.ok(status);
  }
}

