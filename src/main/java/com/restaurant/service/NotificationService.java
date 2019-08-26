package com.restaurant.service;

import com.restaurant.dto.SseMessage;

public interface NotificationService<T> {

    void addEmitter(final T emitter);
    void removeEmitter(final T emitter);
    void doNotify();
    SseMessage getElement(long restaurantId);
    void addElement(long restaurantId, SseMessage element);
}
