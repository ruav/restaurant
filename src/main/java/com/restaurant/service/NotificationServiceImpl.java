package com.restaurant.service;

import com.restaurant.dto.SseMessage;
import com.restaurant.utils.CustomSseEmitter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@EnableScheduling
public class NotificationServiceImpl implements NotificationService<CustomSseEmitter>{

    public final Map<Long, List<CustomSseEmitter>> emmiters = new ConcurrentHashMap<>();
    public final Map<Long, Queue<SseMessage>> notifications = new ConcurrentHashMap<>();

    @Override
    public void addEmitter(final CustomSseEmitter emitter) {
        if (emmiters.get(emitter.getRestaurant()) == null) {
            emmiters.put(emitter.getRestaurant(), new CopyOnWriteArrayList());
        }
        emmiters.get(emitter.getRestaurant()).add(emitter);
    }

    @Override
    public void removeEmitter(final CustomSseEmitter emitter) {
        emmiters.get(emitter.getRestaurant()).remove(emitter);
        if (emmiters.get(emitter.getRestaurant()).isEmpty()) emmiters.remove(emitter.getRestaurant());
    }

    @Async
    @Scheduled(fixedRate = 5_000)
    @Override
    public void doNotify() {
        List<CustomSseEmitter> deadEmitters = new ArrayList<>();
        for (Long restaurant : emmiters.keySet()) {

            SseMessage message = getElement(restaurant);
            if (!isEmptyMessage(message)) {
                final SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .data(message.getData())
                        .id(String.valueOf(System.currentTimeMillis()))
                        .reconnectTime(3 * 1_000)
                        .name(message.getName());
                emmiters.get(restaurant).forEach(emitter -> {
                    try {
                        emitter.send(event);
                    } catch (Exception e) {
                        deadEmitters.add(emitter);
                    }
                });
            }
        }
        deadEmitters.forEach(this::removeEmitter);
    }

    @Override
    public SseMessage getElement(long restaurantId) {
        if (notifications.get(restaurantId) != null) {
            return notifications.get(restaurantId).poll();
        }
        return null;
    }

    @Override
    public void addElement(long restaurantId, SseMessage element) {
        if (emmiters.get(restaurantId) != null) {
            if (notifications.get(restaurantId) == null) {
                notifications.put(restaurantId, new LinkedList<>());
            }
            notifications.get(restaurantId).add(element);
        }
    }

    private boolean isEmptyMessage(SseMessage event) {
        if (event == null) return true;
        if (event.getName() == null || event.getName().isEmpty()) return true;
        if (event.getData() == null || event.getData().isEmpty()) return true;
        return false;
    }

}
