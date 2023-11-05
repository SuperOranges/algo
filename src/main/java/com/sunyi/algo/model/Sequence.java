package com.sunyi.algo.model;

import lombok.Data;

@Data
public class Sequence {
    int[][] positions = new int[5][2];

    public void setSequence() {
        positions[0] = new int[]{1, 1};
    }

}
