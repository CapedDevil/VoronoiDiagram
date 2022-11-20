package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.events;

import me.laudwilliam.resumeprojects.geometry2d.Point;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.Beachline;

public class CircleEvent extends Event {
    private final Beachline.Breakpoint breakpoint;
    private boolean isValid;

    public CircleEvent(Point center, double directix, Beachline.Breakpoint breakpoint) {
        super(directix);
        isValid = true;
        this.breakpoint = breakpoint;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
