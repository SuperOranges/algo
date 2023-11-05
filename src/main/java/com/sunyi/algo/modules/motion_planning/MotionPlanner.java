package com.sunyi.algo.modules.motion_planning;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.sunyi.algo.event.ClearingEventHandler;
import com.sunyi.algo.modules.behavioral_decision.event.handler.MotionSequenceUpdatedEventHandler;
import com.sunyi.algo.modules.framework.Closeable;
import com.sunyi.algo.modules.motion_planning.event.MotionSequenceUpdatedEvent;
import com.sunyi.algo.modules.motion_planning.event.producer.MotionSequenceUpdatedEventProducer;
import lombok.Setter;

import java.util.concurrent.ThreadFactory;

public class MotionPlanner implements Closeable {

    private final static int BUFFER_SIZE = 1024;

    private Disruptor<MotionSequenceUpdatedEvent> motionSequenceUpdatedEventDisruptor;
    private MotionSequenceUpdatedEventProducer motionSequenceUpdatedEventProducer;

    @Setter
    private MotionSequenceUpdatedEventHandler sequenceHandler;

    public MotionPlanner(MotionSequenceUpdatedEventHandler sequenceHandler) {
        this.sequenceHandler = sequenceHandler;

        String disruptorThreadName = "SequenceUpdated-%d";
        ThreadFactory disruptorThreadFactory = new ThreadFactoryBuilder().setNameFormat(disruptorThreadName).setDaemon(true).build();
        this.motionSequenceUpdatedEventDisruptor = new Disruptor<>(
                MotionSequenceUpdatedEvent::new, BUFFER_SIZE, disruptorThreadFactory);
        this.motionSequenceUpdatedEventDisruptor.handleEventsWith(this.sequenceHandler).then(new ClearingEventHandler());
        this.motionSequenceUpdatedEventDisruptor.start();
        RingBuffer<MotionSequenceUpdatedEvent> ringBuffer = motionSequenceUpdatedEventDisruptor.getRingBuffer();
        this.motionSequenceUpdatedEventProducer = new MotionSequenceUpdatedEventProducer(ringBuffer);
    }


    public void publishMotionSequenceUpdatedEvent() {
        motionSequenceUpdatedEventProducer.onData();
    }


    @Override
    public void close() {
        motionSequenceUpdatedEventDisruptor.shutdown();
    }
}
