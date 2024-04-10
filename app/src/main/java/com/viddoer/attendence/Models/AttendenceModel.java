package com.viddoer.attendence.Models;

public class AttendenceModel {

    String name;
    private String inputValue;

    String roll_no;
    String subject;
    String branch;
    String email, number, semester, subject_name, class_test_number, registration;

    public AttendenceModel(String name, String roll_no, String subject, String branch, String email, String number, String semester, String subject_name, String class_test_number, String registration) {
        this.name = name;
        this.roll_no = roll_no;
        this.subject = subject;
        this.branch = branch;
        this.email = email;
        this.number = number;
        this.semester = semester;
        this.subject_name = subject_name;
        this.class_test_number = class_test_number;
        this.registration = registration;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public String getClass_test_number() {
        return class_test_number;
    }


    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setClass_test_number(String class_test_number) {
        this.class_test_number = class_test_number;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }
}


