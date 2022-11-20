package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune;

import me.laudwilliam.resumeprojects.geometry2d.Point;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.events.CircleEvent;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.events.Event;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.events.SiteEvent;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.geometry.DCEL;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.geometry.Site;

public class Algorithm {
    private final DCEL dcel;
    private final EventQueue eventQueue;
    private final Beachline beachline;

    public Algorithm() {
        dcel = new DCEL();
        eventQueue = new EventQueue();
        beachline = new Beachline();
    }

    // Constructors


    // Factory method

    /**
     * Initializes Algorithm
     *
     * @param points a list of points that will act as the sites of the diagram
     * @return Algorithm
     */
    public static Algorithm make(double[][] points) {
        Algorithm algorithm = new Algorithm();
        for (double[] point : points) {
            double x = point[0];
            double y = point[1];
            algorithm.addPoint(x, y);
        }
        return algorithm;
    }


    // Public methods

    /**
     * Runs the algorithm, and creates the diagram
     */
    public void createVoronoi() {
        while (!eventQueue.isEmpty()) {
            Event event = eventQueue.pop();
            if (event instanceof SiteEvent) {
                processSiteEvent((SiteEvent) event);
            }
            if (event instanceof CircleEvent && ((CircleEvent) event).isValid()) {

            }
        }
    }

    // Private

    /**
     * Creates a site from a given point, and adds it to the event queue as a Site event
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     */
    private void addPoint(double x, double y) {
        Site site = dcel.addSite(x, y);
        SiteEvent event = (SiteEvent) eventQueue.addEvent(site);
    }

    /**
     * Handles the process of a new site event
     *
     * @param event Site event currently being handled
     */
    private void processSiteEvent(SiteEvent event) {
        Site site = event.getSite();
        // Insert site into the beachline
        Beachline.Breakpoint[] breakpoints = beachline.insert(site);
        if (breakpoints != null) {
            double[] circle = beachline.getCircleEvent(breakpoints[0], site.getY());
            if (circle != null)
                eventQueue.addEvent(new CircleEvent(new Point(circle[0], circle[1]), circle[2], breakpoints[0]));
            circle = beachline.getCircleEvent(breakpoints[1], site.getY());
            if (circle != null)
                eventQueue.addEvent(new CircleEvent(new Point(circle[0], circle[1]), circle[2], breakpoints[1]));
            dcel.insert(breakpoints[0]);
            dcel.insert(breakpoints[1]);
            dcel.link(breakpoints[0], breakpoints[1]);
            System.out.println("Test");
        }
    }
}
