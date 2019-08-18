package com.restaurant.utils;

import com.restaurant.service.NotificationService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class CustomSseEmitter {

    private SseEmitter emitter;
    private long restaurant;

    public CustomSseEmitter(SseEmitter emitter, long restaurant, NotificationService service) {
        this.emitter = emitter;
        this.emitter.onError(e -> {
            service.removeEmitter(this);
        });
        this.emitter.onCompletion(() -> service.removeEmitter(this));
        this.emitter.onTimeout(() -> service.removeEmitter(this));
        this.restaurant = restaurant;
    }

    public void send(SseEmitter.SseEventBuilder builder) throws IOException {
        emitter.send(builder);
    }

    public long getRestaurant() {
        return restaurant;
    }

    public SseEmitter getEmitter() {
        return emitter;
    }

}
