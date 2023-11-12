/*
 * Copyright 2010, 2013, 2014 Luis Henrique Oliveira Rios
 *
 * This file is part of Search Algorithms Demonstrations.
 *
 * Search Algorithms Demonstrations is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Search Algorithms Demonstrations is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Search Algorithms Demonstrations.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sunyi.algo.simple;

public class AStar {
    /* Public: */
    public AStar(Maze maze, boolean mark_path, boolean step_by_step, TieBreakingStrategy tie_breaking_strategy, Heuristic heuristic, int neighborhood, int currentDirect) {

        this.h = maze.getH();
        this.w = maze.getW();
        this.open_list = new BinaryHeap(this.w * this.h);

        this.graph = new AStarNode[this.h][this.w];
        for (int y = 0; y < this.h; y++) {
            for (int x = 0; x < this.w; x++) {
                this.graph[y][x] = new AStarNode(maze.getMazeCell(x, y), tie_breaking_strategy);
                this.graph[y][x].h = heuristic.distanceToGoal(this.graph[y][x].getMazeCell(), maze.getGoal());
            }
        }

        this.has_solution = false;
        this.mark_path = mark_path;
        this.step_by_step = step_by_step;
        this.neighborhood = neighborhood;
        this.goal = this.graph[maze.getGoal().getY()][maze.getGoal().getX()];
        this.start = this.graph[maze.getStart().getY()][maze.getStart().getX()];

        this.start.parent = null;
        this.start.g = 0;
        this.start.f = this.start.g + this.start.h;
        this.open_list.insert(this.start);
        this.direct = currentDirect;
    }

    public String getOpenListText() {
        StringBuilder stringBuilder = new StringBuilder();

        for (BinaryHeapElement e : this.open_list.asSortedList()) {
            stringBuilder.append(e.toString()).append('\n');
        }

        return stringBuilder.toString();
    }

    public int getPathCost() {
        return this.path_cost;
    }

    public int getMazeCellG(MazeCell maze_cell) {
        return this.graph[maze_cell.getY()][maze_cell.getX()].g;
    }

    public int getMazeCellH(MazeCell maze_cell) {
        return this.graph[maze_cell.getY()][maze_cell.getX()].h;
    }

    public boolean hasExecutionFinished() {
        return this.open_list.size() == 0 || this.goal.closed;
    }

    public boolean hasSolution() {
        return this.has_solution;
    }

    public boolean isInOpenList(MazeCell maze_cell) {
        return this.open_list.has(this.graph[maze_cell.getY()][maze_cell.getX()]);
    }

    public boolean isInClosedList(MazeCell maze_cell) {
        return this.graph[maze_cell.getY()][maze_cell.getX()].closed;
    }

    public void setStepByStep(boolean step_by_step) {
        this.step_by_step = step_by_step;
    }

    public void solve() {
        AStarNode node;
        boolean changedFirstPriority = direct == 5;
        while (!this.hasExecutionFinished()) {
            node = (AStarNode) this.open_list.pop();
            node.closed = true;

            if (node == this.goal) {
                AStarNode node_child = null;
                this.path_cost = node.g;
                this.has_solution = true;

                if (this.mark_path) {
                    //node.getMazeCell().setPathFlag();
                    node_child = node;
                    node = node.parent;

                    do {
                        //node.getMazeCell().setNextMazeCell(node_child.getMazeCell());
                        //node.getMazeCell().setPathFlag();
                        node_child = node;
                        node = node.parent;
                    } while (node != null);
                }
                break;
            }

            for (int i = 0; i < this.neighborhood; i++) {
                int x, y;
                if (changedFirstPriority) {
                    x = node.getMazeCell().getX() + Maze.delta_x[i];
                    y = node.getMazeCell().getY() + Maze.delta_y[i];
                } else {
                    x = node.getMazeCell().getX() + Maze.delta_x[direct];
                    y = node.getMazeCell().getY() + Maze.delta_y[direct];
                    changedFirstPriority = true;
                }
                if (0 <= x && x < this.w && 0 <= y && y < this.h) {
                    AStarNode child = this.graph[y][x];
                    int cost = node.getMazeCell().getCost() + directionCost(node, child, this.goal);
                    if (child.getMazeCell().isBlocked() || child.closed)
                        continue;

                    if (this.open_list.has(child)) {
                        if (child.g > node.g + cost) {
                            child.parent = node;
                            child.g = node.g + cost;
                            child.f = child.g + child.h;
                            this.open_list.insert(child);
                        }
                    } else {
                        child.parent = node;
                        child.g = node.g + cost;
                        child.f = child.g + child.h;
                        this.open_list.insert(child);
                    }
                }
            }
        }
    }

    private int directionCost(AStarNode currentNode, AStarNode neighbor, AStarNode goal) {
        AStarNode parent = currentNode.parent;
        // 出发点
        if (parent == null) {
            return 0;
        }
        // 走直线
        if (neighbor.getMazeCell().x == parent.getMazeCell().x || neighbor.getMazeCell().y == parent.getMazeCell().y)
            return 0;
        // 普通拐点和终点拐点
        return 1;
    }

    /* Private: */
    private static class AStarNode extends BinaryHeapElement {
        /* Public: */
        public AStarNode parent;
        public int f, g, h;
        public boolean closed;

        public AStarNode(MazeCell maze_cell, TieBreakingStrategy tie_breaking_strategy) {
            this.closed = false;
            this.maze_cell = maze_cell;
            this.tie_breaking_strategy = tie_breaking_strategy;
        }

        @Override
        public boolean lessThanForHeap(BinaryHeapElement e) {
            if (this.f == ((AStarNode) e).f) {
                switch (this.tie_breaking_strategy) {
                    case NONE:
                        return false;
                    case HIGHEST_G_VALUES:
                        return this.g > ((AStarNode) e).g;
                    case SMALLEST_G_VALUES:
                        return this.g < ((AStarNode) e).g;
                }
            }
            return this.f < ((AStarNode) e).f;
        }

        public MazeCell getMazeCell() {
            return this.maze_cell;
        }

        @Override
        public String toString() {
            return String.format("%s [%2d %2d %2d]", this.maze_cell.toString(), this.f, this.g, this.h);
        }

        /* Private: */
        private MazeCell maze_cell;
        private TieBreakingStrategy tie_breaking_strategy;
    }

    private int w, h, path_cost, neighborhood;
    private boolean has_solution, mark_path, step_by_step;
    private AStarNode graph[][], goal, start;
    private BinaryHeap open_list;
    //0表示s方向，1表示E方向，2表示w方向，3表示N方向
    private int direct;
}
