package com.restapi.webhook.controller;

import com.restapi.webhook.entity.Subscription;
import com.restapi.webhook.entity.Transport;
import com.restapi.webhook.exception.InfoObj;
import com.restapi.webhook.repository.RepositoryImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {
    private final RepositoryImpl subscriptionRepository;

    public Controller(RepositoryImpl subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @PostMapping("/createsubscriptions")
    public ResponseEntity<?> createSubscription(@RequestBody Transport transport) {
        Subscription subscription = null;
        InfoObj infoByException = null;
        if (transport.getType().equalsIgnoreCase("webhook")
                && transport.getEndpoint().length()!=0)
            subscription = subscriptionRepository.createSubscription(transport);
        else {
            infoByException = checkTransport(transport);
        }
        return (subscription != null)
                ? new ResponseEntity<>(subscription, HttpStatus.CREATED)
                : new ResponseEntity<>(infoByException, HttpStatus.BAD_REQUEST);
    }

    private InfoObj checkTransport(Transport transport) {
        InfoObj checkParamert = new InfoObj();
        StringBuilder info = new StringBuilder();
        if (!transport.getType().equalsIgnoreCase("webhook"))
            info.append("WEBHOOK type invalid");
        if (transport.getEndpoint().length()==0) {
            if (info.length() > 0) info.append(", ");
            info.append("Endpoint is null");
        }
        checkParamert.setInfo(info.toString());
        return checkParamert;
    }


    @GetMapping("/transports")
    public ResponseEntity<?> readTransport() {
        final List<Transport> transportList = subscriptionRepository.readTransport();
        return (transportList != null)
                ? new ResponseEntity<>(transportList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PostMapping("/subscriptions/{id}")
    public ResponseEntity<?> readsubscriptionById(@PathVariable(name = "id") String id) {
        boolean subscriptionIsExist = subscriptionRepository.subscriptionIsExist(id);
        Subscription subscription=null;
        InfoObj infoException = null;
        if (subscriptionIsExist)
            subscription = subscriptionRepository.readSubscriptionBySubId(id);
        else {
            infoException = new InfoObj();
            infoException.setInfo("Subscription by id: " + id+" not found");
        }
        return (subscription != null)
                ? new ResponseEntity<>(subscription, HttpStatus.OK)
                : new ResponseEntity<>(infoException, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/subscriptions")
    public ResponseEntity<?> readAllSubscription(@RequestBody Transport transport) {
        final List<Subscription> subscriptionList = subscriptionRepository.readAllSubscription(transport);
        return (subscriptionList != null)
                ? new ResponseEntity<>(subscriptionList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/subscriptionsactive")
    public ResponseEntity<?> readSubscriptionActive(@RequestBody Transport transport) {
        final List<Subscription> subscriptionList = subscriptionRepository.readSubscriptionByStatus("active", transport);
        return (subscriptionList != null)
                ? new ResponseEntity<>(subscriptionList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/subscriptionsinactive")
    public ResponseEntity<?> readSubscriptionInActive(@RequestBody Transport transport) {

        final List<Subscription> subscriptionList = subscriptionRepository.readSubscriptionByStatus("inactive", transport);
        return (subscriptionList != null)
                ? new ResponseEntity<>(subscriptionList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/subscriptions/{id}")
    public ResponseEntity<?> updatesubscriptionById(@PathVariable(name = "id") String id) {
        final Subscription subscription = subscriptionRepository.updateSubscriptionBySubId(id);
        return (subscription != null)
                ? new ResponseEntity<>(subscription, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/subscriptions/{id}")
    public ResponseEntity<?> deleteSubscriptionById(@PathVariable(name = "id") String id) {
        final Subscription subscription = subscriptionRepository.deleteSubscriptionBySubId(id);
        return (subscription != null)
                ? new ResponseEntity<>(subscription, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/checkstatusactive")
    public List<Subscription> checkstatusactive(){
        return subscriptionRepository.checkStatusInBase();
    }



}
