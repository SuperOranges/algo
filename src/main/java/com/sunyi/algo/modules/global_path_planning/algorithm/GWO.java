package com.sunyi.algo.modules.global_path_planning.algorithm;

import com.sunyi.algo.base.Position;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
public class GWO {

    protected Seq alphaWolf;
    protected Seq betaWolf;
    protected Seq deltaWolf;
    protected int currentIteration;
    protected final int MAX_ITERATIONS;
    protected int populationSize;
    List<Seq> solutions = new ArrayList<>();
    protected List<Seq> wolves;

    protected long init_time;
    protected long computeTime;


    //问题
    Position[] cityPosition = new Position[4];
    Position start = new Position(5L, 5L, "0");
    Position goal = new Position(6, 6, "0");


    public GWO(int populationSize, int MAX_ITERATIONS) {
        this.MAX_ITERATIONS = MAX_ITERATIONS;
        this.populationSize = populationSize;
        for (int i = 0; i < 4; i++) {
            cityPosition[i] = new Position(i, i, i + 1 + "");
        }
    }

    public void execute() {
        this.init_time = System.currentTimeMillis();
        wolves = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            Seq wolf = new Seq();
            //首先都添加起点
            wolf.add(start);
            wolves.add(wolf);
        }
        while (!isStoppingCriteriaReached()) {
            updatePopulation();
            // a decreases linearly front 2 to 0
            for (int i = 0; i < populationSize; i++) {
                Seq currentWolf = wolves.get(i);
                //做一些操作 比如再放入一个联通的节点
                if (currentWolf.canAddGoal()) {
                    currentWolf.add(goal);
                    continue;
                } else {
                    Random random = new Random();
                    int t1 = random.nextInt(100);
                    int index = t1 % 4;
                    //随机再添加一个城市
                    currentWolf.add(cityPosition[index]);
                }
            }
            updateProgress();
        }
        updatePopulation();
        //返回最佳的三个解集
        solutions.add(alphaWolf);
        solutions.add(betaWolf);
        solutions.add(deltaWolf);
        computeTime = System.currentTimeMillis() - init_time;
    }

    private void updatePopulation() {
        for (Seq wolf : wolves) {
            // 对超出搜索空间边界的进行修正
            //repairOperator.execute(wolf);
            // Calculate objective function for each search agent
            // 计算目标函数 比如计算距离、能量、时间
            //problem.evaluate(wolf);
            //针对约束条件对结果集进行一些处理
            //problem.evaluateConstraint(wolf);
            // Update Alpha, Beta, and Delta
            if (alphaWolf == null) {
                alphaWolf = (Seq) wolf.copy();
            }
            if (betaWolf == null) {
                betaWolf = (Seq) wolf.copy();
            }
            if (deltaWolf == null) {
                deltaWolf = (Seq) wolf.copy();
            }
            //在多目标角度对两个解决方案进行比较
            if (alphaWolf.getCost() > wolf.getCost()) {
                alphaWolf = (Seq) wolf.copy();
            }
            if (betaWolf.getCost() > wolf.getCost() && alphaWolf.getCost() < wolf.getCost()) {
                betaWolf = (Seq) wolf.copy();
            }
            if (deltaWolf.getCost() > wolf.getCost() && betaWolf.getCost() < wolf.getCost()) {
                deltaWolf = (Seq) wolf.copy();
            }

        }
        /*
         * problem.evaluate(alphaWolf); problem.evaluate(betaWolf);
         * problem.evaluate(deltaWolf); problem.evaluateConstraint(alphaWolf);
         * problem.evaluateConstraint(betaWolf); problem.evaluateConstraint(deltaWolf);
         */
    }

    protected void updateProgress() {
        this.currentIteration += 1;
    }


    protected boolean isStoppingCriteriaReached() {
        return currentIteration >= MAX_ITERATIONS;
    }
}

