package com.viddoer.attendence.Models;

public class AdvancedAttendanceModel {

    String name, roll, status, date;

    public AdvancedAttendanceModel(String name, String roll, String status, String date) {
        this.name = name;
        this.roll = roll;
        this.status = status;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
