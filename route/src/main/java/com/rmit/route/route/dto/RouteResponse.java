package com.rmit.route.route.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteResponse {
    private UUID id;
    private String name;
    private String number;
    private List<UUID> listOfDestinationIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;
}

