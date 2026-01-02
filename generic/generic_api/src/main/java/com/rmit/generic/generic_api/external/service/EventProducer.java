package com.rmit.generic.generic_api.external.service;

/**
 * Generic interface for Kafka event producers
 * Provides methods for sending messages and request-reply patterns
 * Implement this interface in the service module
 */
public interface EventProducer {
    
    /**
     * Send a message to a topic (fire-and-forget)
     * 
     * @param topic the topic to send the message to
     * @param message the message object to send
     */
    void send(String topic, Object message);

    /**
     * Send a message with a key to a topic
     * 
     * @param topic the topic to send the message to
     * @param key the message key
     * @param message the message object to send
     */
    void send(String topic, String key, Object message);

    /**
     * Send a request and wait for a reply (request-reply pattern)
     * 
     * @param requestTopic the topic to send the request to
     * @param replyTopic the topic to receive the reply from
     * @param request the request object
     * @param responseClass the class of the response object
     * @return the response object
     * @param <T> the type of the response
     */
    <T> T sendAndReceive(String requestTopic, String replyTopic, Object request, Class<T> responseClass);
}

