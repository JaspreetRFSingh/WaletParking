package com.jstech.mywalet.model;

/**
 * Created by samsung on 05-01-2018.
 */

public class Car {

    String number;
    String phone;
    String passC;

    public Car(String number, String phone, String passC) {
        this.number = number;
        this.phone = phone;
        this.passC = passC;
    }
    public Car(){

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassC(String passC) { this.passC = passC; }

    public String getPassC() { return passC; }
}
