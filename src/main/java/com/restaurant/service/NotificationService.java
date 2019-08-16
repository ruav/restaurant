package com.restaurant.service;

public interface NotificationService<T, N> {

    void addEmitter(final T emitter);
    void removeEmitter(final T emitter);
    void doNotify();
    String getElement(long restaurantId);
    void addElement(long restaurantId, N element);
}
