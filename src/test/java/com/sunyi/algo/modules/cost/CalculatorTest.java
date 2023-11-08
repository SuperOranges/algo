package com.sunyi.algo.modules.cost;

import com.sunyi.algo.modules.global_path_planning.event.handler.PathCostRefreshedEventHandler;
import com.sunyi.algo.modules.motion_planning.event.handler.MotionCostRefreshedEventHandler;
import org.junit.jupiter.api.Test;


class CalculatorTest {

    PathCostRefreshedEventHandler pathCostRefreshedEventHandler = new PathCostRefreshedEventHandler();
    MotionCostRefreshedEventHandler motionCostRefreshedEventHandler = new MotionCostRefreshedEventHandler();
    //Calculator calculator = new Calculator(pathCostRefreshedEventHandler, motionCostRefreshedEventHandler);

    @Test
    void publishCostRefreshedEvent() throws InterruptedException {
        //calculator.publishCostRefreshedEvent();
        Thread.sleep(6000);
    }
}