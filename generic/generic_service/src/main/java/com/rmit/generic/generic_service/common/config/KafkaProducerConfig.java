package com.rmit.generic.generic_service.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.time.Duration;

/**
 * Kafka producer configuration
 * Configures KafkaTemplate and ReplyingKafkaTemplate for request-reply pattern
 */
@Configuration
public class KafkaProducerConfig {
    
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(
            ProducerFactory<String, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
    
    /**
     * Request-reply template for synchronous communication
     */
    @Bean
    ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate(
            ProducerFactory<String, Object> producerFactory,
            ConcurrentMessageListenerContainer<String, Object> replyListenerContainer) {
        var replyingKafkaTemplate = new ReplyingKafkaTemplate<>(producerFactory, replyListenerContainer);
        replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(5));
        return replyingKafkaTemplate;
    }
}

