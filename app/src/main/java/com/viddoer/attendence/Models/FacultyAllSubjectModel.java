package com.viddoer.attendence.Models;

public class FacultyAllSubjectModel {

    String branch, semester, subject_name, subject_code;

    public FacultyAllSubjectModel(String branch, String semester, String subject_name, String subject_code) {
        this.branch = branch;
        this.semester = semester;
        this.subject_name = subject_name;
        this.subject_code = subject_code;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }
}
