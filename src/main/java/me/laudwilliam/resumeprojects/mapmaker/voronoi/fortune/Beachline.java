package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune;

import me.laudwilliam.resumeprojects.datastructures.Tree;
import me.laudwilliam.resumeprojects.geometry2d.Parabola;
import me.laudwilliam.resumeprojects.geometry2d.Point;
import me.laudwilliam.resumeprojects.geometry2d.Triangle;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.geometry.Site;

@SuppressWarnings({"rawtypes", "DuplicatedCode"})
public class Beachline {

    // Private
    private Tree.Node root;

    public static Breakpoint findBreakpointToTheLeftOf(Tree.Node root, Breakpoint breakpoint, double directix) {
        Tree.Node rightTurn = null;
        Tree.Node current = root;
        while (current.getData() != breakpoint && !Tree.isLeaf(current)) {
            boolean breakpointBeforeCurrent = isPointBeforeBreakpoint(breakpoint.getPosition(directix), (Breakpoint) current.getData(), directix);
            if (!isPointBeforeBreakpoint(breakpoint.getPosition(directix), (Breakpoint) current.getData(), directix)) {
                rightTurn = current;
                current = current.getRight();
                continue;
            }
            current = current.getLeft();
        }
        // Case 1:
        if (rightTurn == null && current == root) {
            if (Tree.isLeaf(root.getLeft())) return null;
            return (Breakpoint) Tree.getParentOfRightMostLeaf(root.getLeft()).getData();
        }
        // Case 2:
        if (rightTurn == null && !Tree.isLeaf(current.getLeft())) {
            return (Breakpoint) Tree.getParentOfRightMostLeaf(current.getLeft()).getData();
        }
        // Case 3:
        if (rightTurn == null && Tree.isLeaf(current.getLeft()))
            return null;

        assert rightTurn != null;
        // Case 4:
        if (Tree.isLeaf(current.getLeft()) && Tree.isLeaf(rightTurn.getLeft()))
            return (Breakpoint) rightTurn.getData();
        // Case 5:
        if (Tree.isLeaf(current.getLeft()) && !Tree.isLeaf(rightTurn.getLeft()))
            return (Breakpoint) Tree.getParentOfRightMostLeaf(rightTurn.getLeft()).getData();
        // Case 6:
        return (Breakpoint) Tree.getParentOfRightMostLeaf(current.getLeft()).getData();
    }

    public static Breakpoint findBreakpointToTheRightOf(Tree.Node root, Breakpoint breakpoint, double directix) {
        Tree.Node leftTurn = null;
        Tree.Node current = root;
        while (current.getData() != breakpoint) {
            if (isPointBeforeBreakpoint(breakpoint.getPosition(directix), (Breakpoint) current.getData(), directix)) {
                leftTurn = current;
                current = current.getLeft();
                continue;
            }
            current = current.getRight();
        }
        // Case 1:
        //          (breakpoint) breakpoint == root, leftTurn == null
        //             /   \
        //         (node)  (node)
        if (leftTurn == null && current == root) {
            if (Tree.isLeaf(root.getRight())) return null;
            return (Breakpoint) Tree.getParentOfLeftMostLeaf(root.getRight()).getData();
        }
        // Case 2:
        if (leftTurn == null && !Tree.isLeaf(current.getRight()))
            return (Breakpoint) Tree.getParentOfLeftMostLeaf(current.getRight()).getData();
        // Case 3:
        if (leftTurn == null && Tree.isLeaf(current.getRight()))
            return null;
        assert leftTurn != null;
        // Case 4:
        if (Tree.isLeaf(current.getRight()) && Tree.isLeaf(leftTurn.getRight()))
            return (Breakpoint) leftTurn.getData();
        // Case 5:
        if (Tree.isLeaf(current.getRight()) && !Tree.isLeaf(leftTurn.getRight()))
            return (Breakpoint) Tree.getParentOfLeftMostLeaf(leftTurn.getRight()).getData();
        // Case 6:
        return (Breakpoint) Tree.getParentOfLeftMostLeaf(current.getRight()).getData();
    }

    public static boolean isPointBeforeBreakpoint(Point point, Breakpoint breakpoint, double directix) {
        double x1 = breakpoint.arc1.site.getX();
        double y1 = breakpoint.arc1.site.getY();
        double x2 = breakpoint.arc2.site.getX();
        double y2 = breakpoint.arc2.site.getY();
        // Get intersection of the two arcs of the breakpoint
        double[] intersection = Parabola.findIntersection(x1, y1, x2, y2, directix);
        assert intersection != null;
        // If there is only one intersection, make return whether if site comes before that intersection
        if (intersection.length == 1) return point.getX() < intersection[0];
        // If there are 2 intersections, and the breakpoint is a min breakpoint, just check if site comes before min(intersection)
        if (!breakpoint.isMax) return point.getX() < Math.min(intersection[0], intersection[1]);
        // Make sure site comes before max(intersection) but before min(intersection)
        return point.getX() < Math.max(intersection[0], intersection[1]) && point.getX() >= Math.min(intersection[0], intersection[1]);
    }

    private Tree.Node findArcAboveSite(Site site) {
        Tree.Node current = root;
        while (!Tree.isLeaf(current)) {
            // if current is not a leaf, it must be a breakpoint
            Breakpoint breakpoint = (Breakpoint) current.getData();
            if (isPointBeforeBreakpoint(site, breakpoint, site.getY())) current = current.getLeft();
            else current = current.getRight();
        }
        return current;
    }

    private Tree.Node findArcAboveSite(Site site, Tree.Node[] parent) {
        Tree.Node current = root;
        while (!Tree.isLeaf(current)) {
            // if current is not a leaf, it must be a breakpoint
            Breakpoint breakpoint = (Breakpoint) current.getData();

            parent[0] = current;
            if (isPointBeforeBreakpoint(site, breakpoint, site.getY())) current = current.getLeft();
            else current = current.getRight();
        }
        return current;
    }


    double[] getCircleEvent(Breakpoint breakpoint1, Breakpoint breakpoint2, double directix) {
        if (breakpoint1 == null || breakpoint2 == null)
            return null;
        if (!isConverging(breakpoint1, breakpoint2))
            return null;
        Site site1 = breakpoint1.arc1.site;
        Site site2 = breakpoint1.arc2.site;
        Site site3 = breakpoint2.arc1.site;
        if (!isClockwise(site1, site2, site3))
            return null;
        double[] circumcenter = Triangle.circumcenter(site1.getX(), site1.getY(), site2.getX(), site2.getY(), site3.getX(), site3.getY());

        return circumcenter != null && circumcenter[1] - circumcenter[2] <= directix ? circumcenter : null;
    }

    public Tree.Node getRoot() {
        return root;
    }

    private boolean isClockwise(Site site1, Site site2, Site site3) {
        double ax = site2.getX() - site1.getX();
        double ay = site2.getY() - site1.getY();
        double bx = site3.getX() - site2.getX();
        double by = site3.getY() - site2.getY();
        double r = (ax * by) - (ay * bx);
        return r < 0;

    }

    private boolean isConverging(Breakpoint breakpoint1, Breakpoint breakpoint2) {
        return !(breakpoint1.arc1.site == breakpoint2.arc2.site && breakpoint1.arc2.site == breakpoint2.arc1.site);
    }

    Breakpoint[] insert(Site site) {
        // If there are no arcs in the beachline, insert site as root Arc
        if (root == null) {
            root = new Tree.Node(new Arc(site));
            return null;
        }
        Tree.Node[] parent = new Tree.Node[1];
        Tree.Node current = findArcAboveSite(site, parent);
        Arc old = (Arc) current.getData();

        // If arc is on the same y as site, create only one breakpoint
        if (old.site.getY() == site.getY()) {
            Arc left = null;
            Arc right = null;
            // Find which site is on the left and which is on the right. Then set right and left to the corresponding arc
            if (old.site.getX() < site.getX()) {
                left = new Arc(old.site);
                right = new Arc(site);
            }
            if (old.site.getX() > site.getX()) {
                left = new Arc(site);
                right = new Arc(old.site);
            }
            // Create new breakpoint between left and right arcs and replace the current node with it
            current.setData(new Breakpoint(left, right));
            // Add new nodes to the tree
            Tree.Node leftNode = new Tree.Node(left);
            Tree.Node rightNode = new Tree.Node(right);
            current.setLeft(leftNode);
            current.setRight(rightNode);
            return new Breakpoint[]{(Breakpoint) current.getData()};
        }
        // Replace old arc with 3 new arcs
        Arc left = new Arc(old.site);
        Arc right = new Arc(old.site);
        Arc middle = new Arc(site);
        // Create new nodes and replace tree with them
        //  (old ) -> (breakpoint[left, middle, isMax=false])
        //                  /   \
        //             (left)   (breakpoint[middle,right, isMax=True])
        //                        /      \
        //                   (middle)   (right)
        current.setData(new Breakpoint(left, middle, false));
        current.setLeft(new Tree.Node(left));
        current.setRight(new Tree.Node(new Breakpoint(middle, right, true)));
        current.getRight().setLeft(new Tree.Node(middle));
        current.getRight().setRight(new Tree.Node(right));
        return new Breakpoint[]{(Breakpoint) current.getData(), (Breakpoint) current.getRight().getData()};
    }

    private class Arc {
        Site site;

        public Arc(Site site) {
            this.site = site;
        }

        @Override
        public String toString() {
            return site.toString();
        }
    }

    public class Breakpoint {
        final Arc arc1;
        final Arc arc2;
        final boolean isMax;

        Breakpoint(Arc arc1, Arc arc2) {
            this.arc1 = arc1;
            this.arc2 = arc2;
            this.isMax = false;
        }

        Breakpoint(Arc arc1, Arc arc2, boolean isMax) {
            this.arc1 = arc1;
            this.arc2 = arc2;
            this.isMax = isMax;
        }


        public Point getPosition(double directix) {
            Site site1 = arc1.site;
            Site site2 = arc2.site;
            double[] intersection = Parabola.findIntersection(site1.getX(), site1.getY(), site2.getX(), site2.getY(), directix);
            assert intersection != null;
            Point position;
            if (intersection.length == 1 && !isMax) {
                position = new Point(intersection[0], Parabola.findY(site1.getX(), site1.getY(), directix, intersection[0]));
                return position;
            }
            if (intersection.length == 1) {
                position = new Point(intersection[0], Parabola.findY(site2.getX(), site2.getY(), directix, intersection[0]));
                return position;
            }
            if (!isMax) {
                double min = Math.min(intersection[0], intersection[1]);
                position = new Point(min, Parabola.findY(site1.getX(), site1.getY(), directix, min));
                return position;
            }
            double max = Math.max(intersection[0], intersection[1]);
            position = new Point(max, Parabola.findY(site1.getX(), site1.getY(), directix, max));
            return position;
        }

        @Override
        public String toString() {
            return "{" + arc1.toString() + arc2.toString() + ", " + isMax + '}';
        }
    }

    public void insertTest(Site site) {
        Breakpoint[] breakpoints = insert(site);
        if (breakpoints == null) return;
        Breakpoint left1 = findBreakpointToTheLeftOf(root, breakpoints[0], site.getY());
        Breakpoint left2 = findBreakpointToTheLeftOf(root, breakpoints[1], site.getY());
        Breakpoint right1 = findBreakpointToTheRightOf(root, breakpoints[0], site.getY());
        Breakpoint right2 = findBreakpointToTheRightOf(root, breakpoints[1], site.getY());
        System.out.println();
    }
}
