package com.sunyi.algo.modules.motion_planning.event;

import com.lmax.disruptor.EventFactory;

public class MotionSequenceUpdatedEventFactory implements EventFactory<MotionSequenceUpdatedEvent> {
    @Override
    public MotionSequenceUpdatedEvent newInstance() {
        return new MotionSequenceUpdatedEvent();
    }
}
