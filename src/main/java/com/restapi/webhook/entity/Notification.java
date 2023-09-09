package com.restapi.webhook.entity;

import lombok.Data;

@Data
public class Notification {
    private String event;
    private String uuid;
}
