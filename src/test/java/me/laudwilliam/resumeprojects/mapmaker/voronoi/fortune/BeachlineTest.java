package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune;

import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.geometry.Site;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class BeachlineTest {
    public static void main(String[] args) {
        Beachline beachline = new Beachline();
        Site site1 = new Site(4, 11);
        Site site2 = new Site(7, 9);
        Site site3 = new Site(2, 7);
        Site site4 = new Site(4, 7);
        Site site5 = new Site(-3, 7);
        beachline.insert(site1);
        beachline.insert(site2);
        beachline.insert(site3);
        beachline.insert(site4);
        beachline.insert(site5);
        StringBuilder results = beachline.display();
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println(results.toString());

    }
}