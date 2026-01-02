package com.rmit.generic.generic_api.external.dto;

/**
 * Topic registry for Generic service Kafka topics
 * Replace "generic" prefix with your service name
 */
public class GenericTopicRegistry {
    
    private static final String PREFIX = "generic.";
    
    /**
     * Kafka topic names for Generic service
     * Add more topics as needed for your specific use case
     */
    public static class Topic {
        // Request-Reply pattern topics
        public static final String TEST_REQUEST_REPLY_REQ = PREFIX + "test_request_reply_req";
        public static final String TEST_REQUEST_REPLY_RES = PREFIX + "test_request_reply_res";
        
        // List operation topics (if needed)
        public static final String LIST_REQ = PREFIX + "list_req";
        public static final String LIST_RES = PREFIX + "list_res";
        
        // Add more topic names as needed
    }
}

