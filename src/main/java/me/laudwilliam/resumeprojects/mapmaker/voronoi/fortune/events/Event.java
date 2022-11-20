package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.events;

public abstract class Event implements Comparable<Event> {
    private final double y;

    // Constructor
    public Event(double y) {
        this.y = y;
    }

    @Override
    public int compareTo(Event event) {
        return Double.compare(this.y, event.y);
    }

    // Getters and Setters
    public double getY() {
        return y;
    }
}
