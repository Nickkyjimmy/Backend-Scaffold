package com.rmit.route.route.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Number is required")
    private String number;

    @NotNull(message = "List of destination IDs is required")
    private List<UUID> listOfDestinationIds;
}

