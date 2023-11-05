package com.sunyi.algo.modules.behavioral_decision.event;

import com.lmax.disruptor.EventFactory;

public class DirectionChangedEventFactory implements EventFactory<DirectionChangedEvent> {

    @Override
    public DirectionChangedEvent newInstance() {
        return new DirectionChangedEvent();
    }

}


