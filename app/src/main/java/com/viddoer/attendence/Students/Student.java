package com.viddoer.attendence.Students;

public class Student {

    String name, roll_no, status;

    public Student(String name, String roll_no, String status) {
        this.name = name;
        this.roll_no = roll_no;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
