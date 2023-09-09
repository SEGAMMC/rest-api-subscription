package com.restapi.webhook.repository;

import com.restapi.webhook.entity.Subscription;
import com.restapi.webhook.entity.Transport;
import com.restapi.webhook.exception.NullObjException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
public class RepositoryImpl {
    private final SubscriptionRepository subscriptionRepository;
    private final TransportRepository transportRepository;

    public RepositoryImpl(SubscriptionRepository subscriptionRepository, TransportRepository transportRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.transportRepository = transportRepository;
    }


    public Subscription createSubscription(Transport transport) {
        Subscription subscription = new Subscription();

        subscription.setSubscriptionId(UUID.randomUUID().toString());
        long currentTime = System.currentTimeMillis();
        subscription.setCreated(new Date(currentTime));
        subscription.setExpires(new Date(currentTime + 3600000));//+60 min
        subscription.setStatus("ACTIVE");

        //проверка на наличие в бд
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("endpoint", ignoreCase());
        Example<Transport> exampleTrans = Example.of(transport, modelMatcher);
        if (!transportRepository.exists(exampleTrans)) transportRepository.save(transport);
        else {
            transport = transportRepository.findByEndpoint(transport.getEndpoint());
        }
        subscription.setTransport(transport);
        subscriptionRepository.save(subscription);
        return subscription;
    }

    public List<Transport> readTransport() {
        return transportRepository.findAll();
    }

    public Subscription readSubscriptionBySubId(String subId) {
        return subscriptionRepository.findBySubscriptionId(subId);
    }

    public List<Subscription> readAllSubscription(Transport transport) {
        Transport trans = transportRepository.findByEndpoint(transport.getEndpoint());

        return trans.getSubscription();
    }


    public List<Subscription> readSubscriptionByStatus(String statusFind,
                                                       Transport transport) {
        Transport trans = transportRepository.findByEndpoint(transport.getEndpoint());

        if (trans == null) throw new NullObjException("This endpoint"
                + transport.getEndpoint() + " not found");
        List<Subscription> allSubscription = trans.getSubscription();
        List<Subscription> statusSubscription = new ArrayList<>();
        for (Subscription sub : allSubscription) {
            if (sub.getStatus().equalsIgnoreCase(statusFind)) {
                statusSubscription.add(sub);
            }
        }
        return statusSubscription;
    }

    public Subscription updateSubscriptionBySubId(String subId) {
        Subscription subscription = subscriptionRepository
                .findBySubscriptionId(subId);

        long currentTime = System.currentTimeMillis();
        subscription.setCreated(new Date(currentTime));
        subscription.setExpires(new Date(currentTime + 3600000));//+60 min
        subscription.setStatus("ACTIVE");
        subscriptionRepository.save(subscription);
        return subscription;
    }

    public Subscription deleteSubscriptionBySubId(String subId) {
        Subscription subscription = subscriptionRepository
                .findBySubscriptionId(subId);
        subscriptionRepository.delete(subscription);
        return subscription;
    }

    public Subscription randomSubscription() {
        List<Subscription> subscriptionList = subscriptionRepository.findAll();
        int maxSize = subscriptionList.size() - 1;
        int rand = (int) (Math.random() * ++maxSize) + 0;
        return subscriptionList.get(rand);
    }

    public boolean subscriptionIsExist(String id) {
        return subscriptionRepository.existsById(id);
    }

    public List<Subscription> checkStatusInBase() {
        List<Subscription> allSubscriptionActive = subscriptionRepository.findByStatus("ACTIVE");
        List<Subscription> newSubscriptionActive = new ArrayList<>();
        for (Subscription s :  allSubscriptionActive) {
            if (s.getExpires().compareTo(new Date(System.currentTimeMillis()))<=0) {
                s.setStatus("INACTIVE");
                newSubscriptionActive.add(s);
                subscriptionRepository.save(s);
            }
        }
        return newSubscriptionActive;
    }
}
