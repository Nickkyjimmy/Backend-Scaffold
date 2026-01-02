package com.rmit.route.route.service.impl;

import com.rmit.route.route.dto.RouteRequest;
import com.rmit.route.route.dto.RouteResponse;
import com.rmit.route.route.entity.Route;
import com.rmit.route.route.entity.RouteDestination;
import com.rmit.route.route.repo.RouteDestinationRepository;
import com.rmit.route.route.repo.RouteRepository;
import com.rmit.route.route.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

        private final RouteRepository routeRepository;
        private final RouteDestinationRepository routeDestinationRepository;

        @Override
        @Transactional
        public RouteResponse createRoute(RouteRequest request) {
                // Check if route number already exists
                if (routeRepository.existsByNumber(request.getNumber())) {
                        throw new RuntimeException("Route with this number already exists");
                }

                // Create new route
                Route route = routeRepository.save(Route.builder()
                                .name(request.getName())
                                .number(request.getNumber())
                                .build());

                // Create RouteDestination entities
                List<RouteDestination> routeDestinations = request.getListOfDestinationIds().stream()
                                .map(destinationId -> RouteDestination.builder()
                                                .routeId(route.getId())
                                                .destinationId(destinationId)
                                                .build())
                                .collect(Collectors.toList());

                routeDestinationRepository.saveAll(routeDestinations);

                return mapToResponse(route, "Route created successfully");
        }

        @Override
        public RouteResponse getRouteById(UUID id) {
                Route route = routeRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));

                return mapToResponse(route, null);
        }

        @Override
        public List<RouteResponse> getAllRoutes() {
                return routeRepository.findAll().stream()
                                .map(route -> mapToResponse(route, null))
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public RouteResponse updateRoute(UUID id, RouteRequest request) {
                Route route = routeRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Route not found with id: " + id));

                // Check if new number already exists (excluding current route)
                if (!route.getNumber().equals(request.getNumber()) &&
                                routeRepository.existsByNumber(request.getNumber())) {
                        throw new RuntimeException("Route with this number already exists");
                }

                // Update route
                route.setName(request.getName());
                route.setNumber(request.getNumber());
                route = routeRepository.save(route);

                // Delete existing destinations and create new ones
                routeDestinationRepository.deleteByRouteId(id);
                List<RouteDestination> routeDestinations = request.getListOfDestinationIds().stream()
                                .map(destinationId -> RouteDestination.builder()
                                                .routeId(id)
                                                .destinationId(destinationId)
                                                .build())
                                .collect(Collectors.toList());
                routeDestinationRepository.saveAll(routeDestinations);

                return mapToResponse(route, "Route updated successfully");
        }

        @Override
        @Transactional
        public void deleteRoute(UUID id) {
                if (!routeRepository.existsById(id)) {
                        throw new RuntimeException("Route not found with id: " + id);
                }
                // Delete associated route destinations first
                routeDestinationRepository.deleteByRouteId(id);
                routeRepository.deleteById(id);
        }

        private RouteResponse mapToResponse(Route route, String message) {
                // Fetch destination IDs from RouteDestination repository
                List<UUID> destinationIds = routeDestinationRepository.findByRouteId(route.getId()).stream()
                                .map(RouteDestination::getDestinationId)
                                .collect(Collectors.toList());

                return RouteResponse.builder()
                                .id(route.getId())
                                .name(route.getName())
                                .number(route.getNumber())
                                .listOfDestinationIds(destinationIds)
                                .createdAt(route.getCreatedAt())
                                .updatedAt(route.getUpdatedAt())
                                .message(message)
                                .build();
        }
}
