package com.example.menu.models;

import java.util.Random;

public class Reservation{
    private String id;
    private String date;
    private String time;
    private String number;
    private String name;
    private String phone;

    public Reservation() {
    }

    public Reservation(String id, String date, String time, String number, String name, String phone) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.number = number;
        this.name = name;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
