package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.geometry;

import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.Beachline;

import java.util.HashMap;
import java.util.HashSet;

public class DCEL {
    private final HashSet<Vertex> vertices;
    private final HashSet<Edge> edges;
    private final HashSet<Site> sites;
    private final HashMap<Beachline.Breakpoint, Edge> uncompletedEdges;
    private final HashMap<Beachline.Breakpoint, Beachline.Breakpoint> edgeTwins;
    // A neat way of doing breakpoint twins
    private Beachline.Breakpoint previousBreakpoint;

    // Constructor
    public DCEL() {
        vertices = new HashSet<>();
        edges = new HashSet<>();
        sites = new HashSet<>();
        uncompletedEdges = new HashMap<>();
        edgeTwins = new HashMap<>();
    }

    // Public methods
    public Site addSite(double x, double y) {
        Site site = new Site(x, y);
        sites.add(site);
        return site;
    }

    public void insert(Beachline.Breakpoint breakpoint) {
        // If previousBreakpoint is null, then there is no twin edge, if it is not null, then twin the breakpoints
        Edge edge = new Edge();
        if (previousBreakpoint == null)
            previousBreakpoint = breakpoint;
        else {
            edgeTwins.put(previousBreakpoint, breakpoint);
            edgeTwins.put(breakpoint, previousBreakpoint);
            previousBreakpoint = null;
        }
        uncompletedEdges.put(breakpoint, edge);
    }
}
