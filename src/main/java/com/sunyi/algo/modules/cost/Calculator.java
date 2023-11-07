package com.sunyi.algo.modules.cost;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import com.sunyi.algo.base.MapLayerType;
import com.sunyi.algo.base.Position;
import com.sunyi.algo.model.Cost;
import com.sunyi.algo.modules.cost.event.CostRefreshedEvent;
import com.sunyi.algo.modules.cost.event.producer.CostRefreshedEventProducer;
import com.sunyi.algo.modules.framework.Closeable;
import com.sunyi.algo.modules.framework.MedianCalculator;
import com.sunyi.algo.modules.global_path_planning.event.handler.PathCostRefreshedEventHandler;
import com.sunyi.algo.modules.motion_planning.event.handler.MotionCostRefreshedEventHandler;
import lombok.Setter;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Calculator implements Closeable {

    private final static int BUFFER_SIZE = 1024;

    private final Cost[][] costs;

    private int powerCostMedian;

    MedianCalculator medianCalculator = new MedianCalculator();

    private Disruptor<CostRefreshedEvent> costRefreshedEventDisruptor;
    private CostRefreshedEventProducer costRefreshedEventProducer;

    @Setter
    private PathCostRefreshedEventHandler pathCostRefreshedEventHandler;
    @Setter
    private MotionCostRefreshedEventHandler motionCostRefreshedEventHandler;


    public Calculator(PathCostRefreshedEventHandler pathCostRefreshedEventHandler, MotionCostRefreshedEventHandler motionCostRefreshedEventHandler, int w, int h) {
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

        this.costs = new Cost[h][w];

    }


    public void publishCostRefreshedEvent() {
        this.costRefreshedEventProducer.onData();
    }


    private void initCost(int h, int w) {
        int i = 0;
        int[] powerCost = new int[h * w];

        //todo 先根据 ContextDto初始化
        //然后根据引用为null的进行赋值
        Position basePosition = new Position(1, 1, "1/1", MapLayerType.EMPTY, 1L);
        Cost baseCost = new Cost(basePosition);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (this.costs[y][x] == null) {
                    Position position = (Position) basePosition.clone();
                    position.setX(x);
                    position.setY(y);
                    Cost cost = (Cost) baseCost.clone();
                    cost.setPosition(position);
                    this.costs[y][x] = cost;
                    powerCost[i] = 1;
                    i++;
                }
            }
        }
        this.powerCostMedian = medianCalculator.findKthLargest(powerCost, h * w / 2);
    }

    //todo 需要计算一个成本 转换成一个整数


    @Override
    public void close() {
        try {
            costRefreshedEventDisruptor.shutdown(1L, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            costRefreshedEventDisruptor = null;
        }
    }

    public int getMedian(Cost[][] costs, int h, int w) {

        int i = 0;
        int[] cost = new int[h * w];

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                cost[i] = costs[y][x].getPower().get();
                i++;
            }
        }
        return medianCalculator.findKthLargest(cost, h * w / 2);
    }






}
