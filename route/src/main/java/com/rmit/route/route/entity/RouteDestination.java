package com.rmit.route.route.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "route_destinations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(name = "route_id", nullable = false)
    private UUID routeId;

    @Column(name = "destination_id", nullable = false)
    private UUID destinationId;
}

