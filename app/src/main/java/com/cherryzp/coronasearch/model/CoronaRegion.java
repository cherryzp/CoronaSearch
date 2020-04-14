package com.cherryzp.coronasearch.model;

public class CoronaRegion {

    private String region;
    private String increaseAndDecrease;
    private String confirmedTotal;
    private String confirmedRecovered;
    private String confirmedDeath;
    private String rate;
    private double latitude;
    private double longitude;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getIncreaseAndDecrease() {
        return increaseAndDecrease;
    }

    public void setIncreaseAndDecrease(String increaseAndDecrease) {
        this.increaseAndDecrease = increaseAndDecrease;
    }

    public String getConfirmedTotal() {
        return confirmedTotal;
    }

    public void setConfirmedTotal(String confirmedTotal) {
        this.confirmedTotal = confirmedTotal;
    }

    public String getConfirmedRecovered() {
        return confirmedRecovered;
    }

    public void setConfirmedRecovered(String confirmedRecovered) {
        this.confirmedRecovered = confirmedRecovered;
    }

    public String getConfirmedDeath() {
        return confirmedDeath;
    }

    public void setConfirmedDeath(String confirmedDeath) {
        this.confirmedDeath = confirmedDeath;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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

    @Override
    public String toString() {
        return "CoronaRegion{" +
                "region='" + region + '\'' +
                ", increaseAndDecrease='" + increaseAndDecrease + '\'' +
                ", confirmedTotal='" + confirmedTotal + '\'' +
                ", confirmedRecovered='" + confirmedRecovered + '\'' +
                ", confirmedDeath='" + confirmedDeath + '\'' +
                ", rate='" + rate + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
