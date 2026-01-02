package com.rmit.destination.common.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

@Configuration
public class KafkaProducerConfig {
  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate(
      ProducerFactory<String, Object> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  // request-reply template
  @Bean
  ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate(
      ProducerFactory<String, Object> producerFactory,
      ConcurrentMessageListenerContainer<String, Object> replyListenerContainer) {
    var replyingKafkaTemplate = new ReplyingKafkaTemplate<>(producerFactory, replyListenerContainer);
    replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(5));
    return replyingKafkaTemplate;
  }
}

