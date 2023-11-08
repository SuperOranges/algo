package com.sunyi.algo.modules.global_path_planning.algorithm;

import org.junit.Test;

import java.util.ArrayList;

public class GWOTest {

    int populationSize = 20;
    int MAX_ITERATIONS = 10;
    int runs = 31;

    ArrayList<Seq> values = new ArrayList<>();
    GWO algorithm = null;


    @Test
    public void execute() {
        double time = 0;
        for (int i = 0; i < runs; i++) {
            algorithm = new GWO(populationSize, MAX_ITERATIONS);
            algorithm.execute();
            time += algorithm.getComputeTime();
            values.add(algorithm.getSolutions().get(0));
        }
        System.out.println("Compute time : " + ((time / runs) / 1000.0));
    }
}