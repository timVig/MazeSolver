package com.company;
import java.awt.*;

/**
 * This class defines a pointnode, which contains a point, its distance from the start, and its previous node
 */
public class PointNode {
    Point cur;
    PointNode prev;
    int distance;

    /**
     * Constructor for non-heuristic related algorithms.
     * @param cur -> the current node
     * @param prev -> the starting node
     */
    public PointNode(Point cur, PointNode prev) {
        this.cur = cur;
        this.prev = prev;
        this.distance = 0;
    }

    /**
     * Constructor for heuristic related algorithms (AStar)
     * @param distance -> distance from the start
     */
    public PointNode(Point cur, PointNode prev, int distance) {
        this.cur = cur;
        this.prev = prev;
        this.distance = distance;
    }

    /*** These methods are used to get the heuristics for AstarSearching */
    public int getHeuristic() { return this.distance + Main.manhattan(cur); }
    public int getHeuristic8() { return this.distance + Main.chebyshev(cur); }

    /*** Overridden for equality defined my way. */
    @Override public int hashCode() {
        return (cur.x * 31) + (31 * cur.y);
    }
    @Override public boolean equals(Object o) {
        PointNode p = (PointNode) o;
        return (p.cur.x == this.cur.x && this.cur.y == p.cur.y);
    }
}