package com.restaurant.service;

import com.restaurant.utils.CustomSseEmitter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.restaurant.rest.MobileEndpoint.getElement;

@Service
@EnableScheduling
public class NotificationServiceImpl implements NotificationService{

    final List<CustomSseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Override
    public void addEmitter(final CustomSseEmitter emitter) {
        emitters.add(emitter);
    }

    @Override
    public void removeEmitter(final CustomSseEmitter emitter) {
        emitters.remove(emitter);
    }

    @Async
    @Scheduled(fixedRate = 5_000)
    @Override
    public void doNotify() {
        List deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
                String data = getElement(emitter.getRestaurant());
                if (data != null && !data.isEmpty()) {
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(data)
                            .id(String.valueOf(System.currentTimeMillis()))
                            .reconnectTime(3 * 1_000)
                            .name("event");
                    try{
                        emitter.send(event);
                    } catch(Exception e) {
                        deadEmitters.add(emitter);
                    }
                }
            });
        emitters.removeAll(deadEmitters);

    }

}
