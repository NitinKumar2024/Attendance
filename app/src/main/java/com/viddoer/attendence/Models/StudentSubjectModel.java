package com.viddoer.attendence.Models;

public class StudentSubjectModel {
    String subject_code, branch_code, roll_no, subject, role, registration;

    public StudentSubjectModel(String subject_code, String branch_code, String roll_no, String subject, String role, String registration) {
        this.subject_code = subject_code;
        this.branch_code = branch_code;
        this.roll_no = roll_no;
        this.subject = subject;
        this.role = role;
        this.registration = registration;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
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

    public String getRole() {
        return role;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
