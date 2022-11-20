package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.events;

import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.geometry.Site;

public class SiteEvent extends Event {
    private final Site site;

    public SiteEvent(Site site) {
        super(site.getY());
        this.site = site;
    }

    public Site getSite() {
        return site;
    }
}
