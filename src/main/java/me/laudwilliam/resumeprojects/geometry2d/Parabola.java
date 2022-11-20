package me.laudwilliam.resumeprojects.geometry2d;

public class Parabola {
    public static double[] findIntersection(double x1, double y1, double x2, double y2, double directix) {
        if (y1 == directix)
            return new double[]{x1};
        if (y2 == directix)
            return new double[]{x2};
        double[] abc1 = getABC(x1, y1, directix);
        double[] abc2 = getABC(x2, y2, directix);
        double a = abc1[0] - abc2[0];
        double b = abc1[1] - abc2[1];
        double c = abc1[2] - abc2[2];
        if (a == 0 & b == 0 & c == 0)
            return new double[]{};
        if (Math.pow(b, 2) - (4 * a * c) < 0)
            return null;
        return getRoots(abc1[0] - abc2[0], abc1[1] - abc2[1], abc1[2] - abc2[2]);
    }

    public static double[] getABC(double x, double y, double directix) {
        double a = 1 / (2 * (y - directix));
        double b = -2 * x * a;
        double c = (Math.pow(x, 2) * a) + ((y + directix) / 2);
        return new double[]{a, b, c};
    }

    public static double[] getRoots(double a, double b, double c) {
        if (a == 0)
            return new double[]{-c / b};
        double[] results = new double[2];
        double p = Math.pow(b, 2) - (4 * a * c);
        results[0] = (-b + Math.sqrt(p)) / (2 * a);
        results[1] = (-b - Math.sqrt(p)) / (2 * a);
        return results;
    }

    public static double findY(double x, double y, double directix, double x1) {
        double[] abc = getABC(x, y, directix);
        return (abc[0] * Math.pow(x1, 2)) + (abc[1] * x1) + abc[2];
    }

}
