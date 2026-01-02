package com.rmit.generic.generic_service.common.config;

import com.rmit.generic.generic_api.external.dto.GenericTopicRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

/**
 * Kafka consumer configuration
 * Configures listener factory and reply listener container for request-reply pattern
 */
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
    
    /**
     * Request-reply listener container
     * Add all reply topics here
     */
    @Bean
    public ConcurrentMessageListenerContainer<String, Object> replyListenerContainer(
            ConsumerFactory<String, Object> consumerFactory) {
        
        ContainerProperties containerProperties = new ContainerProperties(
                GenericTopicRegistry.Topic.TEST_REQUEST_REPLY_RES,
                GenericTopicRegistry.Topic.LIST_RES
                // Add more reply topics as needed
        );
        
        return new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
    }
}

