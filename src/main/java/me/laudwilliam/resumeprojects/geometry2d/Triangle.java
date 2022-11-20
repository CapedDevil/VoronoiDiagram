package me.laudwilliam.resumeprojects.geometry2d;

public class Triangle {
    public static double[] circumcenter(double x1, double y1, double x2, double y2, double x3, double y3) {
        double a = x2 - x1;
        double b = y2 - y1;
        double c = x3 - x1;
        double d = y3 - y1;
        double e = (x2 - x1) * (x1 + x2) + (y2 - y1) * (y1 + y2);
        double f = (x3 - x1) * (x1 + x3) + (y3 - y1) * (y1 + y3);
        double g = 2 * ((x2 - x1) * (y3 - y2) - (y2 - y1) * (x3 - x2));

        if (g == 0)
            return null;
        double x = (d * e - b * f) / g;
        double y = (a * f - c * e) / g;

        double radius = Math.sqrt(Math.pow(x1 - x, 2) + Math.pow((y1 - y), 2));

        return new double[]{x, y, radius};
    }

}
