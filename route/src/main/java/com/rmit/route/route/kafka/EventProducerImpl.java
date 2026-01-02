package com.rmit.route.route.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmit.route.route.external.service.EventProducer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EventProducerImpl implements EventProducer {
  
  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  @Autowired
  private ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
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

  @Override
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

  @Override
  public <T> T sendAndReceive(String requestTopic, String replyTopic, Object request, Class<T> responseClass) {
    try {
      byte[] requestBytes = objectMapper.writeValueAsBytes(request);
      ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(requestTopic, requestBytes);
      producerRecord.headers().add(KafkaHeaders.REPLY_TOPIC, replyTopic.getBytes());

      RequestReplyFuture<String, Object, Object> replyFuture = replyingKafkaTemplate.sendAndReceive(producerRecord);
      ConsumerRecord<String, Object> response = replyFuture.get();
      
      byte[] responseBytes = (byte[]) response.value();
      T responseObject = objectMapper.readValue(responseBytes, responseClass);
      log.debug("Received response for request topic: {} on reply topic: {}", requestTopic, replyTopic);
      return responseObject;
    } catch (Exception e) {
      log.error("Error in sendAndReceive for topic: {} -> {}", requestTopic, replyTopic, e);
      throw new RuntimeException("Failed to sendAndReceive for topic: " + requestTopic, e);
    }
  }
}

