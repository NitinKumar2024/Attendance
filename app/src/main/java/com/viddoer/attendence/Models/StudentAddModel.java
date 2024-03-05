package com.viddoer.attendence.Models;

public class StudentAddModel {
    private String regNo;
    private String name;
    private String phone;
    private String email;

    public StudentAddModel(String regNo, String name, String phone, String email) {
        this.regNo = regNo;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public String getRegNo() {
        return regNo;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}
