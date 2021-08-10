package com.company;
import java.awt.*;
import java.util.HashSet;

/**
 * This interface defines that anything marked as SearchAlgo must have a 4 and 8 neighbor variant implemented
 * Requires a start, end, maze, set of visited, and set of backtracked nodes.
 */
public interface SearchAlgo {
    boolean solve4(Point current, Point goal, int[][] maze, HashSet<Point> paths, HashSet<Point> backtrack);
    boolean solve8(Point current, Point goal, int[][] maze, HashSet<Point> paths, HashSet<Point> backtrack);
}