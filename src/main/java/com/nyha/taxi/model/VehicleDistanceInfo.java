package com.nyha.taxi.model;

public class VehicleDistanceInfo {
    private Long id;
    private double distance;

    public VehicleDistanceInfo(Long id, double distance) {
        this.id = id;
        this.distance = distance;
    }

    public VehicleDistanceInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
