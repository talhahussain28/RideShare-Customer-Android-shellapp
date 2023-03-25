package com.herride.customer.ui.home.order_place.model;

public class DriverLocationModel_ {
    private String driverId;
    private Double latitude;
    private Double longitude;
    private float bearing;
    private String status;

    public DriverLocationModel_(String driverId, Double latitude, Double longitude, float bearing, String status) {
        this.driverId = driverId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
        this.status = status;
    }

    public String getDelivery_boy_id() {
        return driverId;
    }

    public void setDelivery_boy_id(String delivery_boy_id) {
        this.driverId = delivery_boy_id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
