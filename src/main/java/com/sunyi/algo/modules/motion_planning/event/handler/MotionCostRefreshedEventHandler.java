package com.sunyi.algo.modules.motion_planning.event.handler;

import com.lmax.disruptor.EventHandler;
import com.sunyi.algo.modules.cost.event.CostRefreshedEvent;
import com.sunyi.algo.modules.motion_planning.MotionPlanner;
import lombok.Setter;

public class MotionCostRefreshedEventHandler implements EventHandler<CostRefreshedEvent> {

    @Setter
    private MotionPlanner motionPlanner;

    @Override
    public void onEvent(CostRefreshedEvent event, long sequence, boolean endOfBatch) throws Exception {
        //todo
        System.out.println(Thread.currentThread().getName() + "MotionCostRefreshedEventHandler");
        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getName() + "MotionCostRefreshedEventHandler");
    }
}
