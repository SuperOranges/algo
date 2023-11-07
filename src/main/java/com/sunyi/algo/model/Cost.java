package com.sunyi.algo.model;

import com.sunyi.algo.base.MapLayerType;
import com.sunyi.algo.base.Position;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Slf4j
public class Cost implements Cloneable {
    private AtomicInteger power = new AtomicInteger(1);
    private AtomicInteger ticks = new AtomicInteger(1);
    private Position position;

    public Cost(Position position) {
        setPosition(position);
    }

    public void setPosition(Position position) {
        if (!position.equals(this.position)) {
            this.position = position;
            translateToCost();
        }
    }

    private void translateToCost() {
        String valuePair = position.getValue();
        String layerType = position.getLayerType();
        if (MapLayerType.FLAG.equals(layerType)) {
            this.power.set(0);
            this.ticks.set(0);
        }
        if (MapLayerType.BOX.equals(layerType) || MapLayerType.BATTERY.equals(layerType)) {
            this.power.set(1);
            this.ticks.set(1);
        }

        if (MapLayerType.LIGHT.equals(layerType)) {
            setLightCost();
        }
        if (!StringUtils.isEmpty(valuePair)) {
            String[] split = valuePair.split("/");
            this.power.set(1 + Integer.valueOf(split[0]));
            this.ticks.set(1 + (int) Math.ceil(Double.parseDouble(split[0]) / 2.0d));
        }
    }


    /**
     * 只在单线程里这个表
     * 计算路径时，如果计算出的路径点上有红绿灯，则需计算到红绿灯的时间花费是否小于该红绿灯的剩余时间。
     */
    public void setLightCost() {
        String value = position.getValue();
        if (0 == Integer.valueOf(value)) {
            this.ticks.set(1);
        } else {
            long timestamp = this.position.getTimestamp();
            long currentTimeMillis = System.currentTimeMillis();
            this.ticks.set(101 - (int) Math.ceil((currentTimeMillis - timestamp) / 100.0d));
        }
    }


    /**
     * 比例为当前可行动回合数/剩余回合时间
     * 返回一个1~100以内的数字,能量占据主导位置
     * <p>
     * 2.0 为场上单元格能耗之中位数
     *
     * @return
     */
    public int getMixedCost(int currentPower, int leftTicks, boolean sameDirection) {
        if (sameDirection) {
            double a = (double) this.power.get() / (double) currentPower;
            int b = currentPower * this.ticks.get();
            double c = 2.0d * Math.sqrt(leftTicks);
            double result = a + b / c;
            return intervalMappingFunction(result);
        } else {
            double a = (double) (this.power.get() + 1) / (double) currentPower;
            int b = currentPower * (this.ticks.get() + 1);
            double c = 2.0d * Math.sqrt(leftTicks);
            double result = a + b / c;
            return intervalMappingFunction(result);
        }
    }

    public int getTimeCost(boolean sameDirection) {
        if (sameDirection) {
            return intervalMappingFunction(this.ticks.get());
        } else {
            return intervalMappingFunction(this.ticks.get() + 1);
        }
    }

    public int getPowerCost(boolean sameDirection) {
        if (sameDirection) {
            return intervalMappingFunction(this.power.get());
        } else {
            return intervalMappingFunction(this.power.get() + 1);
        }
    }

    private int intervalMappingFunction(double source) {
        double atan = Math.atan(source);
        double v = (100.0d) * atan / Math.PI + 50;
        return Double.valueOf(v).intValue();
    }

    @Override
    public Object clone() {
        Cost cost = null;
        try {
            cost = (Cost) super.clone();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return cost;
    }

}
