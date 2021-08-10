package com.company;
import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * This class implements a BreadthFirstSearch, with either 4 or 8 neighbor variations to choose from.
 */
public class BreadthFirstSearch extends Search implements SearchAlgo{
    public BreadthFirstSearch(){}

    /**
     * This method implements the solve for a 4 neighbor BFS.
     * @param current -> start node
     * @param goal -> end node
     * @param maze -> the maze
     * @param paths -> set of traversed nodes
     * @param backtrack -> set of path back
     * @return -> is it possible to solve this maze.
     */
    @Override
    public boolean solve4(Point current, Point goal, int[][] maze, HashSet<Point> paths, HashSet<Point> backtrack) {
        LinkedList<PointNode> queue = new LinkedList<>();
        HashSet<Point> visited = new HashSet<>();
        HashSet<int[][]> uniqueReplays = new HashSet<>();
        LinkedList<int[][]> replays = new LinkedList<>();

        queue.addFirst( new PointNode( current, null ) );
        while( !queue.isEmpty() ){
            PointNode p = queue.removeLast();
            paths.add( p.cur );
            if( isGoal(p.cur, goal) ) {
                backtrack( p, backtrack, visited, maze, uniqueReplays, replays );
                replay(replays);
                return true;
            } else {
                visited.add( p.cur );
                for( PointNode pnt: successors(p.cur, visited, p, maze) ) queue.addFirst(pnt);
            }
        }
        return false;
    }

    /**
     * This method implements the solve for an 8 neighbor BFS.
     * @param current -> start node
     * @param goal -> end node
     * @param maze -> the maze
     * @param paths -> set of traversed nodes
     * @param backtrack -> set of path back
     * @return -> is it possible to solve this maze.
     */
    @Override
    public boolean solve8(Point current, Point goal, int[][] maze, HashSet<Point> paths, HashSet<Point> backtrack) {
        LinkedList<PointNode> queue = new LinkedList<>();
        HashSet<Point> visited = new HashSet<>();
        HashSet<int[][]> uniqueReplays = new HashSet<>();
        LinkedList<int[][]> replays = new LinkedList<>();

        queue.addFirst( new PointNode( current, null ) );
        while( !queue.isEmpty() ){
            PointNode p = queue.removeLast();
            paths.add( p.cur );
            if( isGoal(p.cur, goal) ) {
                backtrack( p, backtrack, visited, maze, uniqueReplays, replays );
                replay(replays);
                return true;
            } else {
                visited.add( p.cur );
                for( PointNode pnt: diagonalSuccessors(p.cur, visited, p, maze) ) queue.addFirst(pnt);
            }
        }
        return false;
    }
}