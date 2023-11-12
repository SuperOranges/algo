package com.sunyi.algo;

import com.sunyi.algo.base.Position;
import com.sunyi.algo.simple.AStar;
import com.sunyi.algo.simple.Maze;
import lombok.Data;

import java.util.concurrent.locks.ReadWriteLock;

@Data
public class SimpleAlgo {

    //ticks maze
    Maze maze;
    AStar aStar;

    int[][] lightLeftTicksMap;

    Position start;
    Position goal;

    //用于在落后时在下一轮抢得先机
    int[][] possibleBoxPoints_NW;
    int[][] tipsPoints_NW;

    int[][] possibleBoxPoints_WS;
    int[][] tipsPoints_WS;

    int[][] possibleBoxPoints_SE;
    int[][] tipsPoints_SE;

    int[][] possibleBoxPoints_EN;
    int[][] tipsPoints_EN;

    //0,1表示的障碍物图层，包含敌方小车， 红灯，其他障碍物
    int[][] obstacle;


    //1.夺旗模式  2.捣蛋模式  启动一个额外的线程来检查是否落后以及该使用什么模式
    int currentMode;

    boolean behind;

    //地图宽和高
    int h, w;

    ReadWriteLock readWriteLock;


    private int captureTheFlag() {
        if (currentMode == 1) {
            return captureTheFlag();
        } else {
            return trick();
        }
    }

    /**
     * 夺旗模式的目标是在第一名的情况下夺得最多的棋子
     * 第二名的情况下获得最高的能量
     * 取坐标时先 >>右移8位获取x值 再右移8位获取y
     * //做两版本一版本有序列 一版本没序列
     *
     * @return
     */
    private int getNextPosition() {
        maze.setGoal((int) goal.getX(), (int) goal.getY());
        int sx = (int) start.getX();
        int sy = (int) start.getY();
        //确定下一格的位置
        int min = 4;
        int minCost = Integer.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            int x, y;
            x = sx + Maze.delta_x[i];
            y = sy + Maze.delta_y[i];
            maze.setStart(x, y);
            aStar.solve();
            int pathCost = aStar.getPathCost();
            min = minCost < pathCost ? min : i;
            minCost = Math.min(minCost, pathCost);
        }
        int px = sx + Maze.delta_x[min];
        int py = sy + Maze.delta_y[min];
        return px | (py << 8);
    }

    /**
     * 全力捣乱第一名
     *
     * @return
     */
    private int trick() {

        return 0;
    }

}
