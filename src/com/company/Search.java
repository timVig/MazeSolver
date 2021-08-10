package com.company;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;

public class Search {
    public Search(){};

    /**
     * Determines if this point is the goal/
     * @param p -> current
     * @param e -> goal
     * @return -> is this the goal
     */
    public boolean isGoal(Point p, Point e ){ return p.equals( e ); }

    /**
     * Get all possible successors of a given point.
     * @param p -> current
     * @param visited -> visited set
     * @param previous -> previous node
     * @param testMaze -> the maze
     * @return -> List of successors.
     */
    public LinkedList<PointNode> successors(Point p, HashSet<Point> visited,
                                                   PointNode previous, int[][] testMaze ){
        LinkedList<PointNode> list = new LinkedList<>();
        if( p.x - 1 >= 0 && testMaze[p.x - 1][p.y] == 0 ){
            Point p1 = new Point(p.x - 1, p.y );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous ) );
        }

        if( p.y - 1 >= 0 && testMaze[p.x][p.y - 1] == 0 ){
            Point p1 = new Point(p.x, p.y - 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous ) );
        }

        if( p.x + 1 < testMaze.length && testMaze[p.x + 1][p.y] == 0 ){
            Point p1 = new Point(p.x + 1, p.y );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous ) );
        }

        if( p.y + 1 < testMaze[0].length && testMaze[p.x][p.y + 1] == 0 ){
            Point p1 = new Point(p.x, p.y + 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous ) );
        }
        return list;
    }

    /**
     * Get all possible successors of a given point.
     * @param p -> current
     * @param visited -> visited set
     * @param previous -> previous node
     * @param testMaze -> the maze
     * @return -> List of successors.
     */
    public LinkedList<PointNode> diagonalSuccessors( Point p, HashSet<Point> visited,
                                                            PointNode previous, int [][]testMaze ){
        LinkedList<PointNode> list = successors( p, visited, previous, testMaze );
        if( p.x - 1 >= 0 && p.y - 1 >= 0 && testMaze[p.x - 1][p.y - 1] == 0 ){
            Point p1 = new Point(p.x - 1, p.y - 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous ) );
        }

        if( p.x + 1 < testMaze.length && p.y - 1 >= 0 && testMaze[p.x + 1][p.y - 1] == 0 ){
            Point p1 = new Point(p.x + 1, p.y - 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous ) );
        }

        if( p.x - 1 >= 0 && p.y + 1 < testMaze[0].length  && testMaze[p.x - 1][p.y + 1] == 0 ){
            Point p1 = new Point(p.x - 1, p.y + 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous ) );
        }

        if( p.x + 1 < testMaze.length && p.y + 1 < testMaze[0].length && testMaze[p.x + 1][p.y + 1] == 0 ){
            Point p1 = new Point(p.x + 1, p.y + 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous ) );
        }
        return list;
    }

    /**
     * This creates a backtracking path from the end of the maze to the start of the maze.
     * @param end -> end node
     * @param backtrack -> set of backtracked nodes
     * @param visited -> set of visited nodes
     * @param maze -> the maze
     * @param uniqueReplays -> set of unique replay frames
     * @param replays -> set of replay frames.
     */
    public void backtrack( PointNode end, HashSet<Point> backtrack, HashSet<Point> visited, int[][] maze,
                           HashSet<int[][]> uniqueReplays, LinkedList<int[][]> replays ){
        while( end.prev != null ){
            backtrack.add( end.cur );
            addFrame( maze, visited, uniqueReplays, replays );
            end = end.prev;
        }
        backtrack.add( end.cur );
        addFrame( maze, visited, uniqueReplays, replays );
    }

    /**
     * Replays the maze traversal algorithm.
     * @param replays -> the set of replay frames.
     */
    public void replay( LinkedList<int[][]> replays ){
        for( int[][] replayFrame: replays ){
            Main.paintMaze = replayFrame;
            Main.frame.repaint();
            try{ Thread.sleep(40); } catch ( Exception e ){ e.printStackTrace(); }
        }
        Main.frame.repaint();
    }

    /**
     * Add a frame to the replay set.
     */
    public void addFrame( int[][] maze, HashSet<Point> visited, HashSet<int[][]> uniqueReplays,
                          LinkedList<int[][]> replays ){
        int[][] replayFrame = Main.genGrid( maze, visited );
        if( !uniqueReplays.contains(replayFrame) ) {
            uniqueReplays.add(replayFrame);
            replays.addLast(replayFrame);
        }
    }
}