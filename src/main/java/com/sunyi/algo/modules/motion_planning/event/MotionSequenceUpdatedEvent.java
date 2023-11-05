package com.sunyi.algo.modules.motion_planning.event;

import com.sunyi.algo.event.ObjectEvent;
import com.sunyi.algo.model.Sequence;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MotionSequenceUpdatedEvent extends ObjectEvent<Sequence> {

    Sequence sequence;

}
