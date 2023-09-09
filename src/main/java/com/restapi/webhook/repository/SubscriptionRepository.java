package com.restapi.webhook.repository;

import com.restapi.webhook.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository
        extends JpaRepository<Subscription, String> {
    Subscription findBySubscriptionId(String subscriptionId);

    List<Subscription> findByTransport_Id(int transport_id);

    List<Subscription> findByStatus(String status);
}
