package com.restapi.webhook.controller;

import com.restapi.webhook.entity.Notification;
import com.restapi.webhook.entity.Subscription;
import com.restapi.webhook.repository.RepositoryImpl;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@RestController
public class PostController {
    private final RepositoryImpl subscriptionRepository;

    public PostController(RepositoryImpl subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @PostMapping("/sendnotification")
    public ResponseEntity<Notification> createPostWithObject() {
        Subscription subscription = subscriptionRepository.randomSubscription();

        Notification notification = new Notification();
        notification.setEvent("Notification");
        notification.setUuid(subscription.getSubscriptionId());
        String url = subscription.getTransport().getEndpoint();
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(url,notification,Notification.class);
    }

}
