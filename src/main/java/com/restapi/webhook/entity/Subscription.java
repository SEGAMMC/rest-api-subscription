package com.restapi.webhook.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @Column(name = "subscription_id")
    private String subscriptionId;

    @Column(name = "created")
    private Date created;

    @Column(name = "expires")
    private Date expires;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH,
    CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "transport_id")
    private Transport transport;
}
