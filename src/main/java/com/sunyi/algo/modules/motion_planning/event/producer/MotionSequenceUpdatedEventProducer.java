package com.sunyi.algo.modules.motion_planning.event.producer;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.sunyi.algo.model.Sequence;
import com.sunyi.algo.modules.motion_planning.event.MotionSequenceUpdatedEvent;


public class MotionSequenceUpdatedEventProducer {
    private final RingBuffer<MotionSequenceUpdatedEvent> ringBuffer;

    public MotionSequenceUpdatedEventProducer(RingBuffer<MotionSequenceUpdatedEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslator<MotionSequenceUpdatedEvent> TRANSLATOR =
            (EventTranslator<MotionSequenceUpdatedEvent>) (event, sequence) -> {
                //此处设置事件内容
                Sequence sequence1 = new Sequence();
                sequence1.setSequence();
                event.setSequence(sequence1);
            };

    public void onData() {
        ringBuffer.publishEvent(TRANSLATOR);
    }
}
