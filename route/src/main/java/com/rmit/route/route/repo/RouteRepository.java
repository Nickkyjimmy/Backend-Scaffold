package com.rmit.route.route.repo;

import com.rmit.route.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID> {
    boolean existsByNumber(String number);
}

