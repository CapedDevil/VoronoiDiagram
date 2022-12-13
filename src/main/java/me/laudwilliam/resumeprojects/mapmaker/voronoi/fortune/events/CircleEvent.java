package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.events;

import me.laudwilliam.resumeprojects.geometry2d.Point;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.Beachline;

public class CircleEvent extends Event {
    private final Beachline.Breakpoint left;
    private final Beachline.Breakpoint right;
    private boolean isValid;

    public CircleEvent(Point center, double directix, Beachline.Breakpoint left, Beachline.Breakpoint right) {
        super(directix);
        isValid = true;
        this.left = left;
        this.right = right;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
