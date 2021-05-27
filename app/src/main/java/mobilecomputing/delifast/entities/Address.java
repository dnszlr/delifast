package mobilecomputing.delifast.entities;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;

public class Address {

    private double latitude;
    private double longitude;
    private String addressString;
    private String geoHash;

    public Address() {

    }

    public Address(double latitude, double longitude, String addressString) {
        this.latitude = latitude;
        this.longitude = longitude;
        geoHash = GeoFireUtils.getGeoHashForLocation(new GeoLocation(latitude, longitude));
        this.addressString = addressString;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public String getGeoHash() {
        return geoHash;
    }

    public void setGeoHash(String geoHash) {
        this.geoHash = geoHash;
    }
}
