package mobilecomputing.delifast.entities;

import com.mapbox.geojson.Point;

public class LocationHelper {

    private Point location;
    private double radiusInM;

    public LocationHelper() {
    }

    public Point getLocation() {
        return this.location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public double getRadiusInM() {
        return radiusInM;
    }

    public void setRadiusInM(double radiusInM) {
        this.radiusInM = radiusInM;
    }
}
