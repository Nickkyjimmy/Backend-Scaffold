# Kafka Usage Guide

This guide explains how to use Kafka for messaging between microservices in this project. The setup uses **KRaft mode (no Zookeeper)** with **Request-Reply** and **Fire-and-Forget** patterns.

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Topic Registry Pattern](#topic-registry-pattern)
3. [Creating a New Topic Registry](#creating-a-new-topic-registry)
4. [Implementing a Producer](#implementing-a-producer)
5. [Implementing a Consumer](#implementing-a-consumer)
6. [Request-Reply Pattern](#request-reply-pattern)
7. [Fire-and-Forget Pattern](#fire-and-forget-pattern)
8. [Complete Example](#complete-example)
9. [Testing Kafka Connectivity](#testing-kafka-connectivity)

---

## Architecture Overview

The Kafka setup includes:

- **Kafka Broker**: Running in KRaft mode on `kafka-1:9094` (internal) / `localhost:9092` (external)
- **Kafka UI**: Management interface at `http://localhost:8888`
- **Request-Reply Pattern**: Synchronous communication using `ReplyingKafkaTemplate`
- **Fire-and-Forget Pattern**: Asynchronous communication using `KafkaTemplate`
- **Generic Implementations**: Reusable `EventProducer` and `EventConsumer` interfaces

---

## Topic Registry Pattern

All topics are organized using a **Topic Registry** pattern with nested `Topic` classes for better organization:

```java
public class MyServiceTopicRegistry {
  static private final String prefix = "myservice.";
  
  public static class Topic {
    public static final String USER_CREATED = prefix + "user.created";
    public static final String ORDER_PLACED_REQ = prefix + "order.placed.req";
    public static final String ORDER_PLACED_RES = prefix + "order.placed.res";
  }
}
```

**Usage:**
```java
MyServiceTopicRegistry.Topic.USER_CREATED
MyServiceTopicRegistry.Topic.ORDER_PLACED_REQ
```

---

## Creating a New Topic Registry

### Step 1: Create the Topic Registry Class

Create a new file in your service's `external/dto` package:

**File:** `src/main/java/com/rmit/yourservice/yourservice/external/dto/YourServiceTopicRegistry.java`

```java
package com.rmit.yourservice.yourservice.external.dto;

public class YourServiceTopicRegistry {
  static private final String prefix = "yourservice.";
  
  public static class Topic {
    // Request-Reply topics
    public static final String GET_USER_REQ = prefix + "get_user.req";
    public static final String GET_USER_RES = prefix + "get_user.res";
    
    // Fire-and-forget topics
    public static final String USER_CREATED = prefix + "user.created";
    public static final String ORDER_PROCESSED = prefix + "order.processed";
  }
}
```

### Step 2: Update KafkaConsumerConfig

Add reply topics to the `replyListenerContainer` bean:

**File:** `src/main/java/com/rmit/yourservice/common/config/KafkaConsumerConfig.java`

```java
ContainerProperties containerProperties = new ContainerProperties(
    YourServiceTopicRegistry.Topic.GET_USER_RES,
    // ... other reply topics
);
```

---

## Implementing a Producer

### Option 1: Using EventProducer Interface (Recommended)

The `EventProducer` interface provides methods for both patterns:

**File:** `src/main/java/com/rmit/yourservice/yourservice/service/YourService.java`

```java
package com.rmit.yourservice.yourservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rmit.yourservice.yourservice.external.service.EventProducer;
import com.rmit.yourservice.yourservice.external.dto.YourServiceTopicRegistry;

@Service
public class YourService {
  
  @Autowired
  private EventProducer eventProducer;
  
  // Request-Reply example
  public void sendRequestReplyMessage() {
    MyRequestDto request = new MyRequestDto("data");
    
    MyResponseDto response = eventProducer.sendAndReceive(
        YourServiceTopicRegistry.Topic.GET_USER_REQ,
        YourServiceTopicRegistry.Topic.GET_USER_RES,
        request,
        MyResponseDto.class
    );
    
    // Use the response
    System.out.println("Received: " + response.getData());
  }
  
  // Fire-and-forget example
  public void sendNotification() {
    NotificationDto notification = new NotificationDto("message");
    
    eventProducer.send(
        YourServiceTopicRegistry.Topic.USER_CREATED,
        notification
    );
  }
  
  // Send with key example
  public void sendWithKey() {
    EventDto event = new EventDto("event-data");
    
    eventProducer.send(
        YourServiceTopicRegistry.Topic.ORDER_PROCESSED,
        "order-123", // message key
        event
    );
  }
}
```

### Option 2: Using KafkaGenericProducer

For simple fire-and-forget scenarios:

```java
@Autowired
private KafkaGenericProducer kafkaGenericProducer;

public void sendMessage() {
    kafkaGenericProducer.send(
        YourServiceTopicRegistry.Topic.USER_CREATED,
        new UserCreatedEvent("user-id", "user-name")
    );
}
```

---

## Implementing a Consumer

### Step 1: Create DTOs for Request/Response

**File:** `src/main/java/com/rmit/yourservice/yourservice/external/dto/GetUserRequest.java`

```java
package com.rmit.yourservice.yourservice.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserRequest {
  private String userId;
  private String email;
}
```

**File:** `src/main/java/com/rmit/yourservice/yourservice/external/dto/GetUserResponse.java`

```java
package com.rmit.yourservice.yourservice.external.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {
  private String userId;
  private String name;
  private String email;
  private Boolean success;
}
```

### Step 2: Add Consumer Methods to EventConsumerImpl

**File:** `src/main/java/com/rmit/yourservice/yourservice/kafka/EventConsumerImpl.java`

```java
package com.rmit.yourservice.yourservice.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmit.yourservice.yourservice.external.dto.YourServiceTopicRegistry;
import com.rmit.yourservice.yourservice.external.dto.GetUserRequest;
import com.rmit.yourservice.yourservice.external.dto.GetUserResponse;
import com.rmit.yourservice.yourservice.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventConsumerImpl {

  @Autowired
  private ObjectMapper objectMapper;
  
  @Autowired
  private UserService userService;

  /**
   * Request-Reply Pattern: Get User
   * Listens on GET_USER_REQ and replies on GET_USER_RES
   */
  @KafkaListener(topics = YourServiceTopicRegistry.Topic.GET_USER_REQ)
  @SendTo(YourServiceTopicRegistry.Topic.GET_USER_RES)
  public byte[] handleGetUserRequest(byte[] requestBytes) {
    try {
      GetUserRequest request = objectMapper.readValue(requestBytes, GetUserRequest.class);
      log.info("Received get user request: userId={}, email={}", 
               request.getUserId(), request.getEmail());
      
      // Process the request using your service
      User user = userService.getUser(request.getUserId(), request.getEmail());
      
      // Build response
      GetUserResponse response = GetUserResponse.builder()
          .userId(user.getId())
          .name(user.getName())
          .email(user.getEmail())
          .success(true)
          .build();
      
      // Serialize and return response
      return objectMapper.writeValueAsBytes(response);
    } catch (Exception e) {
      log.error("Error handling get user request", e);
      
      // Return error response
      GetUserResponse errorResponse = GetUserResponse.builder()
          .success(false)
          .build();
      
      try {
        return objectMapper.writeValueAsBytes(errorResponse);
      } catch (Exception ex) {
        log.error("Error serializing error response", ex);
        return new byte[0];
      }
    }
  }
  
  /**
   * Fire-and-Forget Pattern: User Created Notification
   * Just processes the message without replying
   */
  @KafkaListener(topics = YourServiceTopicRegistry.Topic.USER_CREATED)
  public void handleUserCreated(byte[] messageBytes) {
    try {
      UserCreatedEvent event = objectMapper.readValue(messageBytes, UserCreatedEvent.class);
      log.info("User created event received: {}", event);
      
      // Process the event (send email, update cache, etc.)
      notificationService.sendWelcomeEmail(event.getUserId());
      
    } catch (Exception e) {
      log.error("Error handling user created event", e);
    }
  }
}
```

---

## Request-Reply Pattern

The **Request-Reply** pattern is used when you need a synchronous response from another service.

### Flow

1. **Producer** sends a request to `TOPIC_REQ`
2. **Consumer** receives the request on `TOPIC_REQ`
3. **Consumer** processes and sends response to `TOPIC_RES`
4. **Producer** receives the response

### Producer Side

```java
@Autowired
private EventProducer eventProducer;

public void sendRequestReply() {
    MyRequest request = new MyRequest("data");
    
    MyResponse response = eventProducer.sendAndReceive(
        YourServiceTopicRegistry.Topic.GET_USER_REQ,  // Request topic
        YourServiceTopicRegistry.Topic.GET_USER_RES,  // Reply topic
        request,                                       // Request object
        MyResponse.class                               // Response type
    );
    
    // Use response (blocking call, waits for reply)
    processResponse(response);
}
```

### Consumer Side

```java
@KafkaListener(topics = YourServiceTopicRegistry.Topic.GET_USER_REQ)
@SendTo(YourServiceTopicRegistry.Topic.GET_USER_RES)
public byte[] handleRequest(byte[] requestBytes) {
    // 1. Deserialize request
    MyRequest request = objectMapper.readValue(requestBytes, MyRequest.class);
    
    // 2. Process request
    MyResponse response = processRequest(request);
    
    // 3. Serialize and return response
    return objectMapper.writeValueAsBytes(response);
}
```

---

## Fire-and-Forget Pattern

The **Fire-and-Forget** pattern is used for asynchronous notifications or events that don't require a response.

### Producer Side

```java
@Autowired
private EventProducer eventProducer;

public void sendNotification() {
    NotificationEvent event = new NotificationEvent("user-id", "message");
    
    // Send without waiting for response
    eventProducer.send(
        YourServiceTopicRegistry.Topic.USER_CREATED,
        event
    );
    
    // Execution continues immediately
    log.info("Notification sent");
}
```

### Consumer Side

```java
@KafkaListener(topics = YourServiceTopicRegistry.Topic.USER_CREATED)
public void handleNotification(byte[] messageBytes) {
    try {
        NotificationEvent event = objectMapper.readValue(
            messageBytes, 
            NotificationEvent.class
        );
        
        // Process the event
        processNotification(event);
        
    } catch (Exception e) {
        log.error("Error processing notification", e);
    }
}
```

---

## Complete Example

### Scenario: Order Service needs to verify user from Auth Service

### Step 1: Create Topic Registry in Auth Service

**File:** `auth-service/.../dto/AuthTopicRegistry.java`

```java
public class AuthTopicRegistry {
  static private final String prefix = "auth.";
  
  public static class Topic {
    public static final String VERIFY_USER_REQ = prefix + "verify_user.req";
    public static final String VERIFY_USER_RES = prefix + "verify_user.res";
  }
}
```

### Step 2: Create DTOs

**Request DTO:** `VerifyUserRequest.java`
```java
@Data
@Builder
public class VerifyUserRequest {
  private String userId;
  private String token;
}
```

**Response DTO:** `VerifyUserResponse.java`
```java
@Data
@Builder
public class VerifyUserResponse {
  private Boolean isValid;
  private String userId;
  private String username;
}
```

### Step 3: Implement Consumer in Auth Service

**File:** `auth-service/.../kafka/EventConsumerImpl.java`

```java
@KafkaListener(topics = AuthTopicRegistry.Topic.VERIFY_USER_REQ)
@SendTo(AuthTopicRegistry.Topic.VERIFY_USER_RES)
public byte[] handleVerifyUser(byte[] requestBytes) {
    try {
        VerifyUserRequest request = objectMapper.readValue(
            requestBytes, 
            VerifyUserRequest.class
        );
        
        // Verify user logic
        User user = authService.verifyUser(request.getUserId(), request.getToken());
        
        VerifyUserResponse response = VerifyUserResponse.builder()
            .isValid(user != null)
            .userId(user != null ? user.getId() : null)
            .username(user != null ? user.getUsername() : null)
            .build();
        
        return objectMapper.writeValueAsBytes(response);
    } catch (Exception e) {
        log.error("Error verifying user", e);
        return objectMapper.writeValueAsBytes(
            VerifyUserResponse.builder().isValid(false).build()
        );
    }
}
```

### Step 4: Use Producer in Order Service

**File:** `order-service/.../service/OrderService.java`

```java
@Service
public class OrderService {
    
    @Autowired
    private EventProducer eventProducer;
    
    public void createOrder(String userId, String token) {
        // Verify user via Kafka
        VerifyUserRequest request = VerifyUserRequest.builder()
            .userId(userId)
            .token(token)
            .build();
        
        VerifyUserResponse response = eventProducer.sendAndReceive(
            AuthTopicRegistry.Topic.VERIFY_USER_REQ,
            AuthTopicRegistry.Topic.VERIFY_USER_RES,
            request,
            VerifyUserResponse.class
        );
        
        if (response.getIsValid()) {
            // Create order
            Order order = new Order(userId);
            orderRepository.save(order);
            log.info("Order created for user: {}", response.getUsername());
        } else {
            throw new UnauthorizedException("Invalid user");
        }
    }
}
```

---

## Testing Kafka Connectivity

### Test Endpoints

The project includes test endpoints to verify Kafka connectivity:

**Route Service:**
```bash
# Test connection to Destination service
curl http://localhost:8082/kafka/test/destination?message=Hello

# Check Kafka status
curl http://localhost:8082/kafka/test/status
```

**Destination Service:**
```bash
# Test connection to Route service
curl http://localhost:8083/kafka/test/route?message=Hello

# Check Kafka status
curl http://localhost:8083/kafka/test/status
```

### Kafka UI

Access Kafka UI at `http://localhost:8888` to:
- View topics and messages
- Monitor consumer groups
- Inspect message payloads
- Check broker status

---

## Best Practices

### 1. Topic Naming Convention

Use a consistent naming pattern:
- Request-Reply: `{service}.{operation}.req` / `{service}.{operation}.res`
- Fire-and-Forget: `{service}.{event}`

Examples:
- `auth.verify_user.req` / `auth.verify_user.res`
- `order.order_placed`
- `user.user_created`

### 2. Error Handling

Always handle errors in consumers:

```java
@KafkaListener(topics = YourServiceTopicRegistry.Topic.SOME_TOPIC)
public void handleMessage(byte[] messageBytes) {
    try {
        // Process message
    } catch (Exception e) {
        log.error("Error processing message", e);
        // Optionally: send to dead letter queue
    }
}
```

### 3. DTO Design

- Use clear, descriptive names
- Include all necessary fields
- Use Lombok annotations for boilerplate
- Add validation annotations if needed

### 4. Logging

Always log important operations:

```java
log.info("Processing request: {}", request);
log.debug("Sending message to topic: {}", topic);
log.error("Error occurred", exception);
```

### 5. Timeout Configuration

For request-reply, the default timeout is 5 seconds. Adjust if needed in `KafkaProducerConfig`:

```java
replyingKafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
```

---

## Troubleshooting

### Service Not Receiving Messages

1. Check Kafka connection:
   ```bash
   docker-compose logs kafka-1
   ```

2. Verify topic exists in Kafka UI

3. Check consumer group:
   ```bash
   docker exec -it kafka-1 kafka-consumer-groups --bootstrap-server localhost:9092 --list
   ```

### Messages Not Being Serialized

Ensure your DTOs are serializable by Jackson:
- Use `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- Avoid circular references
- Use simple data types

### Timeout Errors

- Increase timeout in `KafkaProducerConfig`
- Check if consumer is running and processing messages
- Verify topic names match exactly

---

## Additional Resources

- [Spring Kafka Documentation](https://docs.spring.io/spring-kafka/reference/)
- [Apache Kafka Documentation](https://kafka.apache.org/documentation/)
- Kafka UI: `http://localhost:8888`

---

## Summary

The Kafka setup provides:

✅ **Generic Producer/Consumer** implementations  
✅ **Request-Reply** pattern support  
✅ **Fire-and-Forget** pattern support  
✅ **Topic Registry** pattern for organization  
✅ **Type-safe** message handling  
✅ **Error handling** built-in  
✅ **Easy testing** with test endpoints  

Use the patterns above to implement new Kafka-based communication between your microservices!

