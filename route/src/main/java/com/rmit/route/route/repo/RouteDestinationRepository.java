package com.rmit.route.route.repo;

import com.rmit.route.route.entity.RouteDestination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RouteDestinationRepository extends JpaRepository<RouteDestination, UUID> {
    List<RouteDestination> findByRouteId(UUID routeId);
    void deleteByRouteId(UUID routeId);
}

