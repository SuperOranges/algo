package com.sunyi.algo.modules.global_path_planning.event.handler;

import com.lmax.disruptor.EventHandler;
import com.sunyi.algo.modules.cost.event.CostRefreshedEvent;
import com.sunyi.algo.modules.global_path_planning.PathPlanner;
import lombok.Setter;


public class PathCostRefreshedEventHandler implements EventHandler<CostRefreshedEvent> {

    @Setter
    private PathPlanner pathPlanner;

    @Override
    public void onEvent(CostRefreshedEvent event, long sequence, boolean endOfBatch) throws Exception {
        //todo
        System.out.println(Thread.currentThread().getName() + "PathCostRefreshedEventHandler");
        Thread.sleep(5000);
        System.out.println(Thread.currentThread().getName() + "PathCostRefreshedEventHandler");
    }
}
