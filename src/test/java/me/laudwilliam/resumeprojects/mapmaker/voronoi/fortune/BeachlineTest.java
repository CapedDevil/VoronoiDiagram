package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune;

import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.geometry.Site;

public class BeachlineTest {
    public static void main(String[] args) {
        test1();
    }


    public static void test1() {
        Beachline beachline = new Beachline();
        Site site1 = new Site(4.9, 11.44);
        Site site2 = new Site(12.7, 10.6);
        Site site3 = new Site(8.7, 7.7);

        beachline.insertTest(site1);
        beachline.insertTest(site2);
        beachline.insertTest(site3);
    }
}