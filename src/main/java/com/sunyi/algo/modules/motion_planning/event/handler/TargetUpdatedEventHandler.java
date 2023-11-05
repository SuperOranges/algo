package com.sunyi.algo.modules.motion_planning.event.handler;

import com.lmax.disruptor.EventHandler;
import com.sunyi.algo.modules.global_path_planning.event.TargetUpdatedEvent;
import com.sunyi.algo.modules.motion_planning.MotionPlanner;
import lombok.Setter;


public class TargetUpdatedEventHandler implements EventHandler<TargetUpdatedEvent> {

    @Setter
    private MotionPlanner motionPlanner;

    @Override
    public void onEvent(TargetUpdatedEvent event, long sequence, boolean endOfBatch) throws Exception {

    }
}
