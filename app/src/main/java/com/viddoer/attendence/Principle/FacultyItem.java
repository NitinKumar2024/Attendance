package com.viddoer.attendence.Principle;

public class FacultyItem {

    private String facultyName;
    private String email;
    private String number;

    public FacultyItem(String facultyName, String email, String number) {
        this.facultyName = facultyName;
        this.email = email;
        this.number = number;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
