package com.viddoer.attendence.Principle;

public class FacultyItem {

    private String facultyName;
    private String subject;
    private String number;
    private String password;
    private String id;

    public FacultyItem(String facultyName, String subject, String number, String password, String id) {
        this.facultyName = facultyName;
        this.subject = subject;
        this.number = number;
        this.password = password;
        this.id = id;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
