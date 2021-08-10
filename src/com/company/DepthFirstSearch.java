package com.company;
import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

/**
 * This class implements a Depth First Search using either a 4 or 8 neighbor pathfinding algorithm.
 */
public class DepthFirstSearch extends Search implements SearchAlgo {
    public DepthFirstSearch(){}

    /**
     * This method implements the solve for a 4 neighbor DFS.
     * @param current -> start node
     * @param goal -> end node
     * @param maze -> the maze
     * @param paths -> set of traversed nodes
     * @param backtrack -> set of path back
     * @return -> is it possible to solve this maze.
     */
    @Override
    public boolean solve4(Point current, Point goal, int[][] maze, HashSet<Point> paths, HashSet<Point> backtrack) {
        Stack<PointNode> stack = new Stack<>();
        HashSet<Point> visited = new HashSet<>();
        HashSet<int[][]> uniqueReplays = new HashSet<>();
        LinkedList<int[][]> replays = new LinkedList<>();

        stack.push( new PointNode( current, null ) );
        while( !stack.isEmpty() ){
            PointNode p = stack.pop();
            paths.add( p.cur );
            if( isGoal(p.cur, goal) ) {
                backtrack( p, backtrack, visited, maze, uniqueReplays, replays );
                addFrame( maze, visited, uniqueReplays, replays );
                replay(replays);
                return true;
            } else {
                visited.add( p.cur );
                addFrame( maze, visited, uniqueReplays, replays );
                for( PointNode pnt: successors(p.cur, visited, p, maze) ) stack.push(pnt);
            }
        }
        return false;
    }

    /**
     * This method implements the solve for an 8 neighbor DFS.
     * @param current -> start node
     * @param goal -> end node
     * @param maze -> the maze
     * @param paths -> set of traversed nodes
     * @param backtrack -> set of path back
     * @return -> is it possible to solve this maze.
     */
    @Override
    public boolean solve8(Point current, Point goal, int[][] maze, HashSet<Point> paths, HashSet<Point> backtrack) {
        Stack<PointNode> stack = new Stack<>();
        HashSet<Point> visited = new HashSet<>();
        HashSet<int[][]> uniqueReplays = new HashSet<>();
        LinkedList<int[][]> replays = new LinkedList<>();

        stack.push( new PointNode( current, null ) );
        while( !stack.isEmpty() ){
            PointNode p = stack.pop();
            paths.add( p.cur );
            if( isGoal(p.cur, goal ) ) {
                backtrack( p, backtrack, visited, maze, uniqueReplays, replays );
                addFrame( maze, visited, uniqueReplays, replays );
                replay(replays);
                return true;
            } else {
                visited.add( p.cur );
                addFrame( maze, visited, uniqueReplays, replays );
                for( PointNode pnt: diagonalSuccessors(p.cur, visited, p, maze) ) stack.push(pnt);
            }
        }
        return false;
    }
}