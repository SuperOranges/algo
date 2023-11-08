package com.sunyi.algo.modules.global_path_planning.algorithm;

import com.sunyi.algo.base.Position;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Setter
public class Seq {
    Queue<Position> positions;

    int[] keyNode = new int[4];

    boolean goalAdded = false;

    public Seq() {
        this.positions = new LinkedList<>();
    }

    public Seq(Seq seq) {
        this();
        this.positions = seq.positions;
        this.keyNode = seq.keyNode;
        this.goalAdded = seq.goalAdded;
    }

    public void add(Position po) {
        int x = (int) po.getX();
        if (x < 4) {
            keyNode[x] = 1;
        }
        if (!goalAdded) {
            positions.add(po);
            if (po.getX() == 6) {
                goalAdded = true;
            }
        }
    }

    public boolean canAddGoal() {
        for (int i = 0; i < keyNode.length; i++) {
            if (keyNode[i] == 0) {
                return false;
            }
        }
        return true;
    }

    public int getCost() {
        final int[] x = {0};
        this.positions.forEach(position -> {
            x[0] = x[0] + Integer.parseInt(position.getValue());
        });
        return x[0];
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        this.positions.forEach(position -> {
            builder.append("[").append(position.getX()).append(",").append(position.getY()).append("],");
        });
        builder.append("cost:" + getCost() + "]");
        return builder.toString();
    }

    @Override
    public Object clone() {
        Seq seq = null;
        try {
            seq = (Seq) super.clone();
        } catch (Exception e) {
            e.getMessage();
        }
        return seq;
    }

    public Seq copy() {
        return new Seq(this);
    }


}
