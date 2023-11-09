package com.sunyi.algo;

import com.sunyi.algo.base.Position;
import com.sunyi.algo.simple.AStar;
import com.sunyi.algo.simple.DStarLite;
import com.sunyi.algo.simple.ManhattanDistanceHeuristic;
import com.sunyi.algo.simple.Maze;
import com.sunyi.algo.simple.MazeCell;
import com.sunyi.algo.simple.TieBreakingStrategy;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AlgoRunner {

    private final MazeCell[][] powerMazeCells;
    private final DStarLite powerDStarLite;
    private final Maze powerMaze;


    private final MazeCell[][] timeMazeCells;
    private final DStarLite timeDStarLite;
    private final Maze timeMaze;

    private final AStar star;


    Random random = new Random(184049348294100L);

    public AlgoRunner() {


        this.powerMazeCells = null;
        this.timeMazeCells = null;

        long seed = 184049348294100L;
        int w = 50;
        int h = 50;

        this.powerMaze = new Maze(seed, w, h, 0.000001f, 80, false);
        this.powerDStarLite = new DStarLite(powerMaze, false, false, ManhattanDistanceHeuristic.getInstance(), 4);

        this.timeMaze = new Maze(seed, w, h, 0.0000000f, 100, false);
        this.timeDStarLite = new DStarLite(timeMaze, false, false, ManhattanDistanceHeuristic.getInstance(), 4);

        this.star = new AStar(powerMaze, false, false, TieBreakingStrategy.SMALLEST_G_VALUES, ManhattanDistanceHeuristic.getInstance(), 4);
        //设置要发生变化的位置
    }


    public Position getSolution() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            int x = random.nextInt(50);
            int y = random.nextInt(50);
            this.powerMaze.setStart(x, y);

            int m = random.nextInt(50);
            int n = random.nextInt(50);
            this.powerMaze.setGoal(m, n);
            this.powerDStarLite.solve();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");

        return null;
    }

    public Position getSolutionAStar() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            int x = random.nextInt(50);
            int y = random.nextInt(50);
            this.powerMaze.setStart(x, y);

            int m = random.nextInt(50);
            int n = random.nextInt(50);
            this.powerMaze.setGoal(m, n);
            this.star.solve();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
        return null;
    }


    public static void main(String[] args) {
        AlgoRunner algoRunner = new AlgoRunner();
        algoRunner.getSolution();
        algoRunner.getSolutionAStar();
    }


    private void blockSomeCells() {

        Set<MazeCell> blocked_cells = new HashSet<MazeCell>(), unblocked_cells = new HashSet<MazeCell>();

        for (int i = 0; i < 4; i++) {
            /* Block some cells. */
            MazeCell blocked_maze_cell;
            int x, y;
            x = random.nextInt(this.powerMaze.getW());
            y = random.nextInt(this.powerMaze.getH());

            blocked_maze_cell = this.powerMaze.getMazeCell(x, y);
            if (blocked_maze_cell != this.powerMaze.getStart() && blocked_maze_cell != this.powerMaze.getGoal() && !blocked_maze_cell.isBlocked()
                    && !blocked_cells.contains(blocked_maze_cell)) {
                blocked_maze_cell.block();
                blocked_cells.add(blocked_maze_cell);
            }

        }
        for (int i = 0; i < 4; i++) {
            MazeCell unblocked_maze_cell;
            int x, y;
            x = random.nextInt(this.powerMaze.getW());
            y = random.nextInt(this.powerMaze.getH());
            unblocked_maze_cell = this.powerMaze.getMazeCell(x, y);

            if (!blocked_cells.contains(unblocked_maze_cell) && !unblocked_cells.contains(unblocked_maze_cell) && unblocked_maze_cell != this.powerMaze.getGoal()
                    && unblocked_maze_cell != powerMaze.getStart()) {
                int new_cost = random.nextInt(80) + 1;

                if (unblocked_maze_cell.isBlocked() || unblocked_maze_cell.getCost() > new_cost) {
                    unblocked_cells.add(unblocked_maze_cell);
                    unblocked_maze_cell.setCost(new_cost);
                }
            }
        }
        if (unblocked_cells.size() > 0 || blocked_cells.size() > 0) {
            this.powerDStarLite.informBlockedCells(blocked_cells);
            this.powerDStarLite.informUnblockedCells(unblocked_cells);
        }
    }


}
