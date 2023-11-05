package com.sunyi.algo.modules.cost;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.sunyi.algo.modules.cost.event.CostRefreshedEvent;
import com.sunyi.algo.modules.cost.event.producer.CostRefreshedEventProducer;
import com.sunyi.algo.modules.framework.Closeable;
import com.sunyi.algo.modules.global_path_planning.event.handler.PathCostRefreshedEventHandler;
import com.sunyi.algo.modules.motion_planning.event.handler.MotionCostRefreshedEventHandler;
import lombok.Setter;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Calculator implements Closeable {

    private final static int BUFFER_SIZE = 1024;

    private Disruptor<CostRefreshedEvent> costRefreshedEventDisruptor;
    private CostRefreshedEventProducer costRefreshedEventProducer;

    @Setter
    private PathCostRefreshedEventHandler pathCostRefreshedEventHandler;
    @Setter
    private MotionCostRefreshedEventHandler motionCostRefreshedEventHandler;


    public Calculator(PathCostRefreshedEventHandler pathCostRefreshedEventHandler, MotionCostRefreshedEventHandler motionCostRefreshedEventHandler) {
        this.pathCostRefreshedEventHandler = pathCostRefreshedEventHandler;
        this.motionCostRefreshedEventHandler = motionCostRefreshedEventHandler;

        String disruptorThreadName = "CostRefreshed-%d";
        ThreadFactory disruptorThreadFactory = new ThreadFactoryBuilder().setNameFormat(disruptorThreadName).setDaemon(true).build();
        this.costRefreshedEventDisruptor = new Disruptor<>(
                CostRefreshedEvent::new, BUFFER_SIZE, disruptorThreadFactory);
        //放在一个handleEventsWith里是同时发 放在多个handleEventsWith则是按序发
        this.costRefreshedEventDisruptor.handleEventsWith(this.pathCostRefreshedEventHandler, this.motionCostRefreshedEventHandler);
        //.then(new ClearingEventHandler());
        this.costRefreshedEventDisruptor.start();
        RingBuffer<CostRefreshedEvent> ringBuffer = costRefreshedEventDisruptor.getRingBuffer();
        this.costRefreshedEventProducer = new CostRefreshedEventProducer(ringBuffer);
    }


    public void publishCostRefreshedEvent() {
        this.costRefreshedEventProducer.onData();
    }


    @Override
    public void close() {
        try {
            costRefreshedEventDisruptor.shutdown(1L, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            costRefreshedEventDisruptor = null;
        }
    }
}
