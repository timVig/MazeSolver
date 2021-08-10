package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.concurrent.*;
enum Algorithm { DFS, BFS, ASTAR }
enum Mode { FOUR, EIGHT }

public class Main {
    static int[][] testMaze;
    public static Rectangle2D[][] drawingBoard;
    static HashSet<Point> path = new HashSet<>();
    static HashSet<Point> backtrack = new HashSet<>();
    static JFrame frame = new JFrame( "Maze" );
    static AStarSearch aStar = new AStarSearch();
    static BreadthFirstSearch bfs = new BreadthFirstSearch();
    static DepthFirstSearch dfs = new DepthFirstSearch();
    static int[][] paintMaze;
    static boolean processing = false; //should be made thread safe.
    static ExecutorService exec = Executors.newFixedThreadPool(1);
    static final String[] buttonNames = { "Gen", "BFS4", "BFS8", "DFS4", "DFS8", "AStar4", "AStar8" };

    public static void main(String[] args) {
        boolean solved = false;
        drawingBoard = new Rectangle2D[28][28];
        resetBoard( true );

        while( !solved ){
            testMaze = mazeGenerator( 28, 28 );
            solved = genDFS(new Point(0,0), new Point( testMaze.length-1,
                    testMaze[0].length-1 ), testMaze, path );
        }

        paintMaze = testMaze;
        frame.setVisible( true );
        frame.setLayout( null );
        startUI();
    }

    /**
     * This method does the same as a 4 neighbor DFS, in order to use a fast search to generate a possible maze
     * @return -> if the maze is possible
     */
    public static boolean genDFS( Point current, Point goal, int[][]maze, HashSet<Point> paths ){
        Stack<PointNode> stack = new Stack<>();
        HashSet<Point> visited = new HashSet<>();
        stack.push( new PointNode( current, null ) );
        while( !stack.isEmpty() ){
            PointNode p = stack.pop();
            paths.add( p.cur );
            if( dfs.isGoal(p.cur, goal) ) {
                return true;
            } else {
                visited.add( p.cur );
                for( PointNode pnt: dfs.successors(p.cur, visited, p, maze) ) stack.push(pnt);
            }
        }
        return false;
    }

    /**
     * This method solves a maze using a specific algorithm
     * @param a -> the algorithm to use
     * @param m -> the mode (4||8)
     * @param current -> start
     * @param goal -> end
     * @param maze -> the maze
     * @param paths -> visited paths
     * @return -> if the maze is possible
     */
    public static boolean solve( Algorithm a, Mode m, Point current, Point goal, int[][]maze, HashSet<Point> paths ){
        resetBoard( false );
        if( m == Mode.FOUR ){
            if( a == Algorithm.DFS ) return dfs.solve4( current, goal, maze, paths, backtrack );
            if( a == Algorithm.BFS ) return bfs.solve4( current, goal, maze, paths, backtrack );
            if( a == Algorithm.ASTAR ) return aStar.solve4( current, goal, maze, paths, backtrack  );
        } else if( m == Mode.EIGHT ){
            if( a == Algorithm.DFS ) return dfs.solve8( current, goal, maze, paths, backtrack );
            if( a == Algorithm.BFS ) return bfs.solve8( current, goal, maze, paths, backtrack );
            if( a == Algorithm.ASTAR ) return aStar.solve8( current, goal, maze, paths, backtrack );
        }
        return false;
    }

    /**
     * Resets the board, or sets it if this is the first time
     * @param isFirst -> is this the first time?
     */
    public static void resetBoard( boolean isFirst ) {
        for (int i = 1; i <= drawingBoard.length; i++)
            for (int j = 1; j <= drawingBoard[0].length; j++)
                drawingBoard[i-1][j-1] = new Rectangle2D.Double(i*20, j*20, 20, 20);
        frame.setSize( 60 + ( drawingBoard[0].length*20 ), ( 20 * ( drawingBoard.length + drawingBoard.length/2 )) );
        frame.getContentPane().add(new MyCanvas());
        if( !isFirst ) frame.setVisible( true );
        path.clear();
        backtrack.clear();
    }

    /**
     * Draws all the JButtons to scale and links up their action listeners.
     */
    public static void startUI(){
        int buttonSize = (( (drawingBoard.length * 20) ) / buttonNames.length) - 15;
        int distance = ((((drawingBoard.length * 20) ) - (buttonSize * buttonNames.length)) / buttonNames.length) + 3;
        int bounds = 20;
        int offset = 40;

        for( int i = 0; i < buttonNames.length; i++ ) {
            JButton button = new JButton( buttonNames[i] );
            button.setBounds( bounds + (i * (distance + buttonSize)), drawingBoard.length * 20 + offset,
                    buttonSize, buttonSize );
            button.setVisible( true );

            if( i == 0 ){ button.addActionListener( new GenerationAction() ); }
            if( i == 1 ){ button.addActionListener( new SolveAction( Algorithm.BFS, Mode.FOUR )); }
            if( i == 2 ){ button.addActionListener( new SolveAction( Algorithm.BFS, Mode.EIGHT )); }
            if( i == 3 ){ button.addActionListener( new SolveAction( Algorithm.DFS, Mode.FOUR )); }
            if( i == 4 ){ button.addActionListener( new SolveAction( Algorithm.DFS, Mode.EIGHT )); }
            if( i == 5 ){ button.addActionListener( new SolveAction( Algorithm.ASTAR, Mode.FOUR )); }
            if( i == 6 ){ button.addActionListener( new SolveAction( Algorithm.ASTAR, Mode.EIGHT )); }

            frame.add( button );
        } offset += buttonSize + distance;

        JButton exit = new JButton("Exit");
        exit.setBounds( bounds, drawingBoard.length * 20 + offset, (drawingBoard.length * 20), buttonSize );
        exit.setVisible(true);
        exit.addActionListener( new ExitListener() );
        frame.add(exit);
        frame.setVisible( true );
        frame.repaint();
    }

    /**
     * Computes the manhattan distance from a certain point to the end
     * @param p -> current point
     * @return -> the distance
     */
    public static int manhattan( Point p ){
        return Math.abs(p.x - testMaze.length) + Math.abs(p.y - testMaze[0].length);
    }

    /**
     * Computes the chebyshev distance from a certain point to the end
     * @param p -> current point
     * @return -> the distance
     */
    public static int chebyshev( Point p ){
        return Math.max( Math.abs( p.x - testMaze.length ), Math.abs( p.y - testMaze[0].length ) );
    }

    /**
     * Generates a random maze of h x w which may not be solvable, needs confirmation before using
     * @param w -> width
     * @param h -> height
     * @return -> a randomly developed maze
     */
    public static int[][] mazeGenerator( int w, int h ){
        int rate = 70;
        int[][] maze = new int[w][h];
        Random rand = new Random();
        for( int i = 0; i < w; i++ ){
            for( int j = 0; j < h; j++ ){
                if( rand.nextInt(100) < rate ) {
                    maze[i][j] = 1;
                    rate = rate - 3;
                } else {
                    maze[i][j] = 0;
                    rate = rate + 2;
                }
            }
        }
        maze[w-2][h-1] = 0; maze[w-1][h-1] = 0; maze[w-1][h-2] = 0;
        maze[0][0] = 0; maze[0][1] = 0; maze[1][0] = 0;
        return maze;
    }

    /**
     * creates a grid with different squares represented as numbers for later painting
     * @param maze -> the maze grid
     * @param visited -> visited points
     * @return -> painting grid
     */
    public static int[][] genGrid( int[][] maze, HashSet<Point> visited ){
        int[][] returnGrid = new int[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                Point p = new Point(i, j);
                if(backtrack.contains( p )){ returnGrid[i][j] = 9; }
                else if ( visited.contains(p) ){ returnGrid[i][j] = 8; }
                else { if( maze[i][j] == 1 ) returnGrid[i][j] = 1; else returnGrid[i][j] = 0; }
            }
        }
        return returnGrid;
    }

    /**
     * Paints the board
     */
    static class MyCanvas extends JComponent {
        public void paint( Graphics g2 ){
            Graphics2D g = (Graphics2D) g2;
            for (int i = 0; i < paintMaze.length; i++) {
                for (int j = 0; j < paintMaze[0].length; j++) {
                    if( paintMaze[i][j] == 9 ){
                        g.setColor( new Color(0x5DD9B5) );
                        g.fill(drawingBoard[j][i]);
                    } else if( paintMaze[i][j] == 8 ){
                        g.setColor( new Color(0xC90D0D) );
                        g.fill(drawingBoard[j][i]);
                    } else {
                        if(paintMaze[i][j] == 1 ){
                            g.setColor( new Color( 0x000000 ) );
                            g.fill(drawingBoard[j][i]);
                        } else {
                            g.setColor( new Color(0xEEEAEA) );
                            g.fill(drawingBoard[j][i]);
                        }
                    }
                    g.setColor( new Color(000));
                    g.draw(drawingBoard[j][i]);
                    g.drawRect( (i+1)*20, (j+1)*20, 20, 20 );
                }
            }
        }
    }

    /**
     * This class is used to perform maze generation for the "Gen" button
     */
    static class GenerationAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            boolean solved = false;
            processing = true;
            while( !solved ){
                testMaze = mazeGenerator( 28, 28 );
                solved = genDFS(new Point(0,0), new Point( testMaze.length-1,
                        testMaze[0].length-1 ), testMaze, path );
            }
            paintMaze = testMaze;
            exec.execute( () -> frame.repaint() );
            processing = false;
        }
    }

    /**
     * This class is the action listener for all of the searches that can be used.
     */
    static class SolveAction implements ActionListener {
        Algorithm algo;
        Mode mode;
        public SolveAction( Algorithm algo, Mode mode ){ this.algo = algo; this.mode = mode; }
        @Override
        public void actionPerformed(ActionEvent e) {
            processing = true;
            exec.execute(() -> {
                solve(this.algo, this.mode, new Point(0, 0),
                        new Point(testMaze.length - 1, testMaze[0].length - 1), testMaze, path);
                processing = false;
            });

        }
    }

    /**
     * This is the action listener for exiting the program.
     */
    static class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(1);
        }
    }
}