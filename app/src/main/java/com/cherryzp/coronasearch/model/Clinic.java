package com.cherryzp.coronasearch.model;

public class Clinic {

    private String city;
    private String address;
    private String clinic;
    private String tel;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "Clinic{" +
                "city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", clinic='" + clinic + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
