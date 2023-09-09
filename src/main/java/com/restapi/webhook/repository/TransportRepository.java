package com.restapi.webhook.repository;

import com.restapi.webhook.entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransportRepository
        extends JpaRepository<Transport, Integer> {
    Transport findByEndpoint(String endpoint);

}
