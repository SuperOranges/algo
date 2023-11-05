package com.sunyi.algo.modules.cost.event;

import com.lmax.disruptor.EventFactory;

public class CostRefreshedEventFactory implements EventFactory<CostRefreshedEvent> {
    @Override
    public CostRefreshedEvent newInstance() {
        return new CostRefreshedEvent();
    }
}
