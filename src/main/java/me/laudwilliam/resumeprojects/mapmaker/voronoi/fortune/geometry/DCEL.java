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
        Edge edge = new Edge();
        uncompletedEdges.put(breakpoint, edge);
    }

    public void link(Beachline.Breakpoint breakpoint1, Beachline.Breakpoint breakpoint2) {
        edgeTwins.put(breakpoint1, breakpoint2);
        edgeTwins.put(breakpoint2, breakpoint1);
    }
}
