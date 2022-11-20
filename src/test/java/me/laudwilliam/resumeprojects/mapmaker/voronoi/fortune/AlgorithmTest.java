package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune;

public class AlgorithmTest {

    public static void main(String[] args) {
        double[][] points = {{4.6, 11.4}, {12.7, 10.6}, {8.7, 7.7}, {13.9, 6.76}, {7.1, 4.24}};
        Algorithm algorithm = Algorithm.make(points);
        algorithm.createVoronoi();
    }
}
