package com.sunyi.algo.model;

import com.sunyi.algo.base.MapLayerType;
import com.sunyi.algo.base.Position;
import org.junit.Test;

public class CostTest {

    Position positionA = new Position(1, 1, "2/2", MapLayerType.TIRE, 1L);
    Position positionB = new Position(1, 1, "4/4", MapLayerType.TIRE, 1L);
    Position positionC = new Position(1, 1, "6/6", MapLayerType.TIRE, 1L);
    Position positionD = new Position(1, 1, "8/8", MapLayerType.TIRE, 1L);
    Position positionE = new Position(1, 1, "80/80", MapLayerType.TIRE, 1L);
    Position positionF = new Position(1, 1, "80/80", MapLayerType.TIRE, 1L);
    Position positionG = new Position(1, 1, "2/2", MapLayerType.TIRE, 1L);

    int highCurrentPower = 900;


    @Test
    public void getMixedCostSameDirection() {

        //1. hight current

    }

    @Test
    public void getMixedCostDiffDirection() {
    }

    @Test
    public void getTimeCost() {
    }

    @Test
    public void getPowerCost() {
    }

    @Test
    public void getMixedCost() {
    }
}