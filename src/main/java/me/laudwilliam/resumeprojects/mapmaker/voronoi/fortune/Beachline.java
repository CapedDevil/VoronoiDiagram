package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune;

import me.laudwilliam.resumeprojects.geometry2d.Parabola;
import me.laudwilliam.resumeprojects.geometry2d.Point;
import me.laudwilliam.resumeprojects.geometry2d.Triangle;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.geometry.Site;

public class Beachline {

    // Private
    private Node root;

    private boolean isLeaf(Node node) {
        return node.left == null && node.right == null;
    }

    private Breakpoint findBreakpointToTheLeftOf(Breakpoint breakpoint, double directix) {
        Node current = root;
        // Variable to keep track of where you took first right
        Node firstRight = root;
        // Find breakpoint in the tree
        while (!isLeaf(current)) {
            if (current.data == breakpoint)
                break;
            if (isPointBeforeBreakpoint(breakpoint.getPosition(directix), (Breakpoint) current.data))
                current = current.left;
            else {
                firstRight = current;
                current = current.right;
            }
        }
        // If firstRight has no breakpoints to the left, firstRight is the left most breakpoint
        // Assuming first right isn't the breakpoint we are looking for of course
        if (isLeaf(firstRight.left) && firstRight.data != breakpoint) return (Breakpoint) firstRight.data;
        // Catch a case in which firstRight == breakpoint and there are no more breakpoints to the left of it
        //          (firstRight == breakpoint)
        //                   /   \
        // (Not a breakpoint)   (breakpoint[middle,right, isMax=True])
        //                          /      \
        //                      (middle)   (right)
        if (firstRight.data == breakpoint && isLeaf(firstRight.left)) return null;
        // Find the left most left with firstRight.right as the root node
        // This works because the right most leaf, will always lie at root->right-right->...->right
        // Added a catch to the case where the first right.left == breakpoint, this would mean that first right has to be the breakpoint to the left
        Node root = firstRight.left.data == breakpoint ? firstRight : firstRight.left;
        while (!isLeaf(root.right)) root = root.right;
        return (Breakpoint) root.data;
    }

    private Breakpoint findBreakpointToTheRightOf(Breakpoint breakpoint, double directix) {
        Node current = root;
        // Variable to keep track of where you took first left
        Node firstLeft = root;
        // Find breakpoint in the tree
        while (!isLeaf(current)) {
            if (current.data == breakpoint)
                break;
            if (isPointBeforeBreakpoint(breakpoint.getPosition(directix), (Breakpoint) current.data)) {
                firstLeft = current;
                current = current.left;
            } else current = current.right;
        }
        // If first left has no breakpoints to the right, first left is the right most breakpoint
        // Assuming first left isn't the breakpoint we are looking for of course
        if (isLeaf(firstLeft.right) && firstLeft.data != breakpoint) return (Breakpoint) firstLeft.data;
        // Catch a case in which firstRight == breakpoint and there are no more breakpoints to the left of it
        //          (firstLeft == breakpoint)
        //             /   \
        //  (breakpoint)   (not a breakpoint)
        //    /      \
        // (middle)   (right)
        if (firstLeft.data == breakpoint && isLeaf(firstLeft.right)) return null;
        // Find the left most left with firstLeft.right as the root node
        // This works because the left most leaf, will always lie at root->left-left->...->left
        // Added a catch to the case where the first left.right == breakpoint, this would mean that first left has to be the breakpoint to the right
        Node root = firstLeft.right.data == breakpoint ? firstLeft : firstLeft.right;
        while (!isLeaf(root.left)) root = root.left;
        return (Breakpoint) root.data;
    }

    private boolean isPointBeforeBreakpoint(Point point, Breakpoint breakpoint) {
        double x1 = breakpoint.arc1.site.getX();
        double y1 = breakpoint.arc1.site.getY();
        double x2 = breakpoint.arc2.site.getX();
        double y2 = breakpoint.arc2.site.getY();
        double directix = point.getY();
        // Get intersection of the two arcs of the breakpoint
        double[] intersection = Parabola.findIntersection(x1, y1, x2, y2, directix);
        assert intersection != null;
        // If there is only one intersection, make return whether if site comes before that intersection
        if (intersection.length == 1) return point.getX() <= intersection[0];
        // If there are 2 intersections, and the breakpoint is a min breakpoint, just check if site comes before min(intersection)
        if (!breakpoint.isMax) return point.getX() <= Math.min(intersection[0], intersection[1]);
        // Make sure site comes before max(intersection) but before min(intersection)
        return point.getX() <= Math.max(intersection[0], intersection[1]) && point.getX() <= Math.min(intersection[0], intersection[1]);
    }

    private Node findLeafAboveSite(Site site) {
        Node current = root;
        while (!isLeaf(current)) {
            // if current is not a leaf, it must be a breakpoint
            Breakpoint breakpoint = (Breakpoint) current.data;
            if (isPointBeforeBreakpoint(site, breakpoint)) current = current.left;
            else current = current.right;
        }
        return current;
    }

    double[] getCircleEvent(Breakpoint breakpoint, double directix) {
        Breakpoint left = findBreakpointToTheLeftOf(breakpoint, directix);
        Breakpoint right = findBreakpointToTheRightOf(breakpoint, directix);
        if (left == null || right == null)
            return null;
        Breakpoint convergingBreakpoint = isConverging(breakpoint, left) ? left : right;

        Site site1 = breakpoint.arc1.site;
        Site site2 = breakpoint.arc2.site;
        Site site3 = convergingBreakpoint.arc1.site == site1 || convergingBreakpoint.arc1.site == site2 ? convergingBreakpoint.arc2.site : convergingBreakpoint.arc1.site;

        if (!isClockwise(site1, site2, site3))
            return null;

        double[] result = Triangle.circumcenter(site1.getX(), site1.getY(), site2.getX(), site2.getY(), site3.getX(), site3.getY());
        if (result != null && result[1] - result[2] <= directix)
            return result;

        return null;
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
        if (breakpoint2.arc2.site == breakpoint1.arc1.site && breakpoint2.arc1.site == breakpoint1.arc2.site)
            return false;
        else return breakpoint2.arc2.site != breakpoint1.arc2.site || breakpoint2.arc1.site != breakpoint1.arc1.site;
    }

    Breakpoint[] insert(Site site) {
        // If there are no arcs in the beachline, insert site as root Arc
        if (root == null) {
            root = new Node(new Arc(site));
            return null;
        }
        Node current = findLeafAboveSite(site);
        Arc old = (Arc) current.data;

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
            current.data = new Breakpoint(left, right);
            // Add new nodes to the tree
            Node leftNode = new Node(left);
            Node rightNode = new Node(right);
            current.left = leftNode;
            current.right = rightNode;
            return new Breakpoint[]{(Breakpoint) current.data};
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
        current.data = new Breakpoint(left, middle, false);
        current.left = new Node(left);
        current.right = new Node(new Breakpoint(middle, right, true));
        current.right.left = new Node(middle);
        current.right.right = new Node(right);
        return new Breakpoint[]{(Breakpoint) current.data, (Breakpoint) current.right.data};
    }

    /**
     * <h1> Display Beachline in Console</h1>
     * The display function takes the nodes of the beachline
     * and formats them into a string structure, that makes
     * visualizing the tree much easier
     *
     * @return a StringBuilder, that contains the tree structure in a string format
     */
    StringBuilder display() {
        StringBuilder results = new StringBuilder();
        traversePreOrder(results, "", "", root);
        return results;
    }

    /**
     * @param sb      the string builder to append out put to
     * @param padding padding for tree
     * @param pointer shape of the pointer
     * @param node    the root node of tree to traverse
     * @author Baeldung.com
     * @see <a href="https://www.baeldung.com/java-print-binary-tree-diagram">
     * How to print a binary tree Java</a>
     */
    private void traversePreOrder(StringBuilder sb, String padding, String pointer, Node node) {
        if (node != null) {
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.data.toString());
            sb.append("\n");

            String paddingForBoth = padding + "|  ";
            String pointerForRight = "└──";
            String pointerForLeft = (node.right != null) ? "├──" : "└──";

            traversePreOrder(sb, paddingForBoth, pointerForLeft, node.left);
            traversePreOrder(sb, paddingForBoth, pointerForRight, node.right);
        }
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

    private class Node {
        Object data;
        Node left;
        Node right;

        Node(Arc arc) {
            this.data = arc;
        }

        Node(Breakpoint breakpoint) {
            this.data = breakpoint;
        }
    }


}
