package com.cherryzp.coronasearch.model;

public class ClinicTest {

    private String ClinicName;
    private double lattitude;
    private double longitude;
    private String phone;
    private String roadaddr;

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoadaddr() {
        return roadaddr;
    }

    public void setRoadaddr(String roadaddr) {
        this.roadaddr = roadaddr;
    }

    @Override
    public String toString() {
        return "Clinic{" +
                "ClinicName='" + ClinicName + '\'' +
                ", lattitude='" + lattitude + '\'' +
                ", longtitude='" + longitude + '\'' +
                ", phone='" + phone + '\'' +
                ", roadaddr='" + roadaddr + '\'' +
                '}';
    }

}
