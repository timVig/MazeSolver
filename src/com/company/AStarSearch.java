package com.company;
import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * This class implements an algorithm for doing an AStar search with either 4 or 8 neighbors on the grid.
 * It overrides most of the methods in Search, seeing as a heuristic is needed.
 * Heuristic(s) used is/are the Chebyshev distance (8), and the Manhattan Distance(4)
 */
public class AStarSearch extends Search implements SearchAlgo{
    public AStarSearch(){}

    /**
     * Solves a 4 neighbor maze puzzle using AStarSearch
     * @param current -> start node
     * @param goal -> end node
     * @param maze -> maze to traverse
     * @param paths -> all traversed set
     * @param backtrack -> path traversal set
     * @return -> if it is solvable.
     */
    @Override
    public boolean solve4(Point current, Point goal, int[][] maze, HashSet<Point> paths, HashSet<Point> backtrack) {
        HashSet<Point> visited = new HashSet<>();
        HashSet<PointNode> closed = new HashSet<>();
        HashSet<PointNode> open = new HashSet<>(); open.add( new PointNode( current, null, 0 ) );
        PriorityQueue<PointNode> queue = new PriorityQueue<>((p1, p2) -> {
            if( p1.getHeuristic() == p2.getHeuristic() ) return 0;
            else if( p1.getHeuristic() < p2.getHeuristic() ) return -1;
            else return 1;
        });
        HashSet<int[][]> uniqueReplays = new HashSet<>();
        LinkedList<int[][]> replays = new LinkedList<>();
        queue.add( new PointNode( current, null, 0 ) );

        while( !open.isEmpty() ){
            PointNode n = queue.poll();
            open.remove(n); closed.add(n);
            if( isGoal(n.cur, goal ) ){
                backtrack( n, backtrack, visited, maze, uniqueReplays, replays );
                addFrame( maze, visited, uniqueReplays, replays );
                replay(replays);
                return true;
            }
            addFrame(maze, visited, uniqueReplays, replays );
            for( PointNode pnt: successors( n.cur, visited, n, maze ) ) {
                int heur = pnt.getHeuristic();
                paths.add( pnt.cur );
                if(closed.contains(pnt) ){ continue; }
                else if( open.contains(pnt) && heur < pnt.distance ) { open.remove(pnt); }
                else if( open.contains(pnt) && heur < pnt.distance ) { closed.remove(pnt); }
                else { open.add(pnt); queue.add(pnt); }
            }

            visited.add(n.cur);
        }
        return false;
    }

    /**
     * Solves an 8 neighbor maze puzzle using AStarSearch
     * @param current -> start node
     * @param goal -> end node
     * @param maze -> maze to traverse
     * @param paths -> all traversed set
     * @param backtrack -> path traversal set
     * @return -> if it is solvable.
     */
    @Override
    public boolean solve8( Point current, Point goal, int[][] maze, HashSet<Point> paths, HashSet<Point> backtrack ){
        HashSet<Point> visited = new HashSet<>();
        HashSet<PointNode> closed = new HashSet<>();
        HashSet<PointNode> open = new HashSet<>(); open.add( new PointNode( current, null, 0 ) );
        PriorityQueue<PointNode> queue = new PriorityQueue<>((p1, p2) -> {
            if( p1.getHeuristic() == p2.getHeuristic() ) return 0;
            else if( p1.getHeuristic() < p2.getHeuristic() ) return -1;
            else return 1;
        });
        HashSet<int[][]> uniqueReplays = new HashSet<>();
        LinkedList<int[][]> replays = new LinkedList<>();
        queue.add( new PointNode( current, null, 0 ) );

        while( !open.isEmpty() ){
            PointNode n = queue.poll();
            open.remove(n); closed.add(n);
            if( isGoal(n.cur, goal ) ){
                backtrack( n, backtrack, visited, maze, uniqueReplays, replays );
                addFrame( maze, visited, uniqueReplays, replays );
                replay(replays);
            }
            addFrame( maze, visited, uniqueReplays, replays );

            for( PointNode pnt: diagonalSuccessors( n.cur, visited, n, maze ) ) {
                int heur = pnt.getHeuristic8();
                paths.add( pnt.cur );
                if(closed.contains(pnt) ){ continue; }
                else if( open.contains(pnt) && heur < pnt.distance ) { open.remove(pnt); }
                else if( open.contains(pnt) && heur < pnt.distance ) { closed.remove(pnt); }
                else { open.add(pnt); queue.add(pnt); }
            }

            visited.add( n.cur );
        }
        return false;
    }

    /**
     * Gets successors for 4 neighbor Astar
     * @param p -> current point, P
     * @param visited -> visited set
     * @param previous -> previous point
     * @param testMaze -> the maze
     * @return -> LinkedList of new successors.
     */
    @Override
    public LinkedList<PointNode> successors(Point p, HashSet<Point> visited, PointNode previous, int[][] testMaze ){
        LinkedList<PointNode> list = new LinkedList<>();
        if( p.x - 1 >= 0 && testMaze[p.x - 1][p.y] == 0 ){
            Point p1 = new Point(p.x - 1, p.y );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous, previous.distance + 1 ) );
        }

        if( p.y - 1 >= 0 && testMaze[p.x][p.y - 1] == 0 ){
            Point p1 = new Point(p.x, p.y - 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous, previous.distance + 1 ) );
        }

        if( p.x + 1 < testMaze.length && testMaze[p.x + 1][p.y] == 0 ){
            Point p1 = new Point(p.x + 1, p.y );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous, previous.distance + 1 ) );
        }

        if( p.y + 1 < testMaze[0].length && testMaze[p.x][p.y + 1] == 0 ){
            Point p1 = new Point(p.x, p.y + 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous, previous.distance + 1 ) );
        }
        return list;
    }

    /**
     * Gets successors for 8 neighbor Astar
     * @param p -> current point, P
     * @param visited -> visited set
     * @param previous -> previous point
     * @param testMaze -> the maze
     * @return -> LinkedList of new successors.
     */
    @Override
    public LinkedList<PointNode> diagonalSuccessors( Point p, HashSet<Point> visited, PointNode previous, int[][] testMaze ){
        LinkedList<PointNode> list = successors( p, visited, previous, testMaze );
        if( p.x - 1 >= 0 && p.y - 1 >= 0 && testMaze[p.x - 1][p.y - 1] == 0 ){
            Point p1 = new Point(p.x - 1, p.y - 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous, previous.distance + 1 ) );
        }

        if( p.x + 1 < testMaze.length && p.y - 1 >= 0 && testMaze[p.x + 1][p.y - 1] == 0 ){
            Point p1 = new Point(p.x + 1, p.y - 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous, previous.distance + 1 ) );
        }

        if( p.x - 1 >= 0 && p.y + 1 < testMaze[0].length  && testMaze[p.x - 1][p.y + 1] == 0 ){
            Point p1 = new Point(p.x - 1, p.y + 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous, previous.distance + 1 ) );
        }

        if( p.x + 1 < testMaze.length && p.y + 1 < testMaze[0].length && testMaze[p.x + 1][p.y + 1] == 0 ){
            Point p1 = new Point(p.x + 1, p.y + 1 );
            if( !visited.contains(p1) ) list.add( new PointNode( p1, previous, previous.distance + 1 ) );
        }
        return list;
    }
}