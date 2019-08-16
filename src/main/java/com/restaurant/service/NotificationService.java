package com.restaurant.service;

import com.restaurant.utils.CustomSseEmitter;

public interface NotificationService {

    void addEmitter(final CustomSseEmitter emitter);
    void removeEmitter(final CustomSseEmitter emitter);
    void doNotify();
}
