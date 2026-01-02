package com.rmit.auth_service.common.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaGenericProducer {
  
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final ObjectMapper objectMapper;

  /**
   * Send a message to a topic (fire-and-forget)
   * 
   * @param topic the topic to send the message to
   * @param message the message object to send
   */
  public void send(String topic, Object message) {
    try {
      byte[] messageBytes = objectMapper.writeValueAsBytes(message);
      kafkaTemplate.send(topic, messageBytes);
      log.debug("Sent message to topic: {}", topic);
    } catch (Exception e) {
      log.error("Error sending message to topic: {}", topic, e);
      throw new RuntimeException("Failed to send message to topic: " + topic, e);
    }
  }

  /**
   * Send a message with a key to a topic
   * 
   * @param topic the topic to send the message to
   * @param key the message key
   * @param message the message object to send
   */
  public void send(String topic, String key, Object message) {
    try {
      byte[] messageBytes = objectMapper.writeValueAsBytes(message);
      kafkaTemplate.send(topic, key, messageBytes);
      log.debug("Sent message to topic: {} with key: {}", topic, key);
    } catch (Exception e) {
      log.error("Error sending message to topic: {} with key: {}", topic, key, e);
      throw new RuntimeException("Failed to send message to topic: " + topic, e);
    }
  }

  /**
   * Send a message asynchronously
   * 
   * @param topic the topic to send the message to
   * @param message the message object to send
   */
  public void sendAsync(String topic, Object message) {
    try {
      byte[] messageBytes = objectMapper.writeValueAsBytes(message);
      kafkaTemplate.send(topic, messageBytes).whenComplete((result, ex) -> {
        if (ex == null) {
          log.debug("Successfully sent message to topic: {}", topic);
        } else {
          log.error("Failed to send message to topic: {}", topic, ex);
        }
      });
    } catch (Exception e) {
      log.error("Error preparing async message for topic: {}", topic, e);
      throw new RuntimeException("Failed to prepare async message for topic: " + topic, e);
    }
  }
}

