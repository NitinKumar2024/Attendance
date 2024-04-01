package com.viddoer.attendence.Models;

public class StudentListRankModel {
    private String rollNumber;
    private String name;
    private String rank;
    private int attendancePercentage;

    public StudentListRankModel(String rollNumber, String name, String rank, int attendancePercentage) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.rank = rank;
        this.attendancePercentage = attendancePercentage;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    public int getAttendancePercentage() {
        return attendancePercentage;
    }
}
