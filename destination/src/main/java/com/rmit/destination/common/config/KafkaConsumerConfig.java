package com.rmit.destination.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import com.rmit.destination.destination.external.dto.DestinationTopicRegistry;
import com.rmit.destination.destination.external.dto.RouteTopicRegistry;

@Configuration
public class KafkaConsumerConfig {
  @Bean
  ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
      KafkaTemplate<String, Object> kafkaTemplate,
      ConsumerFactory<String, Object> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    factory.setReplyTemplate(kafkaTemplate);
    return factory;
  }

  // request-reply listener container
  @Bean
  public ConcurrentMessageListenerContainer<String, Object> replyListenerContainer(
      ConsumerFactory<String, Object> consumerFactory) {

    ContainerProperties containerProperties = new ContainerProperties(
        // reply topics will be added dynamically based on request-reply pattern
        DestinationTopicRegistry.Topic.TEST_REQUEST_REPLY_RES,
        DestinationTopicRegistry.Topic.LIST_RES,
        RouteTopicRegistry.Topic.TEST_REQUEST_REPLY_RES,
        RouteTopicRegistry.Topic.LIST_RES);

    return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
  }
}

