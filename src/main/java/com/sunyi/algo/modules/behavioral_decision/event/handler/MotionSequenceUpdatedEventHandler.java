package com.sunyi.algo.modules.behavioral_decision.event.handler;

import com.lmax.disruptor.EventHandler;
import com.sunyi.algo.model.Sequence;
import com.sunyi.algo.modules.behavioral_decision.Controller;
import com.sunyi.algo.modules.motion_planning.event.MotionSequenceUpdatedEvent;
import lombok.Setter;


/**
 * EventHandler和WorkHandler都用于处理Disruptor中的事件，但它们的使用场景和功能略有不同。EventHandler用于并行处理事件，每个EventHandler实例可以处理相同类型的事件，从而提高处理效率。而WorkHandler则适用于多线程消费事件的场景，每个工作线程只处理特定的事件序列，保证有序处理。
 * <p>
 * 在Disruptor中，EventHandler和WorkHandler是用于不同场景的事件处理器：
 * <p>
 * EventHandler：EventHandler通常用于并行处理事件的场景。它是最常见的事件处理器类型。每个EventHandler都独立地处理Ring Buffer中的事件，每个事件只会被一个EventHandler处理。多个EventHandler可以同时处理不同的事件，实现事件的并行处理。
 * <p>
 * WorkHandler：WorkHandler通常用于负载均衡的场景。它可以用于将事件分发给一组消费者进行处理。当有多个WorkHandler时，每个事件只会被一个WorkHandler处理，不同的事件可能会被分发到不同的WorkHandler进行处理。这样可以实现事件的负载均衡，提高处理吞吐量。
 * <p>
 * 需要注意的是，EventHandler和WorkHandler是Disruptor库中的接口，并且是用来处理相同类型的事件对象。它们都定义了onEvent方法，用于处理事件。但在使用时的场景略有不同。
 * <p>
 * 总结一下，EventHandler主要用于并行处理事件的场景，而WorkHandler主要用于负载均衡的场景。你可以根据具体的需求选择适合的事件处理器类型来实现高效的事件处理。
 * <p>
 * <p>
 * 如果handler消费不够快怎么办 是有序消费
 */
public class MotionSequenceUpdatedEventHandler implements EventHandler<MotionSequenceUpdatedEvent> {

    @Setter
    private Controller controller;

    @Override
    public void onEvent(MotionSequenceUpdatedEvent motionSequenceUpdatedEvent, long sequence, boolean endOfBatch) throws Exception {
        Sequence sequence1 = motionSequenceUpdatedEvent.getSequence();
        System.out.println(sequence1);
    }


}
