package com.viddoer.attendence.Models;

public class PrincipleUploadSubjectModel {

    String subject, subject_code, branch, branch_code, semester;

    public PrincipleUploadSubjectModel(String subject, String subject_code, String branch, String branch_code, String semester) {
        this.subject = subject;
        this.subject_code = subject_code;
        this.branch = branch;
        this.branch_code = branch_code;
        this.semester = semester;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject_code() {
        return subject_code;
    }

    public void setSubject_code(String subject_code) {
        this.subject_code = subject_code;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
