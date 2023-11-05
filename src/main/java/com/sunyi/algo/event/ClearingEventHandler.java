package com.sunyi.algo.event;

import com.lmax.disruptor.EventHandler;

public class ClearingEventHandler<T> implements EventHandler<ObjectEvent<T>> {
    @Override
    public void onEvent(ObjectEvent<T> event, long sequence, boolean endOfBatch) {
        event.clear();
    }
}
