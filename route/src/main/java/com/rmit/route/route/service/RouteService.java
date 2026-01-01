package com.rmit.route.route.service;

import com.rmit.route.route.dto.RouteRequest;
import com.rmit.route.route.dto.RouteResponse;

import java.util.List;
import java.util.UUID;

public interface RouteService {
    RouteResponse createRoute(RouteRequest request);
    RouteResponse getRouteById(UUID id);
    List<RouteResponse> getAllRoutes();
    RouteResponse updateRoute(UUID id, RouteRequest request);
    void deleteRoute(UUID id);
}

