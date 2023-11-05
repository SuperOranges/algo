package com.sunyi.algo.modules.cost.event.producer;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.RingBuffer;
import com.sunyi.algo.modules.cost.event.CostRefreshedEvent;

public class CostRefreshedEventProducer {

    private final RingBuffer<CostRefreshedEvent> ringBuffer;

    public CostRefreshedEventProducer(RingBuffer<CostRefreshedEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslator<CostRefreshedEvent> TRANSLATOR =
            (event, sequence) -> {
                //todo 此处设置事件内容

            };

    public void onData() {
        ringBuffer.publishEvent(TRANSLATOR);
    }
}
