package com.viddoer.attendence.Models;

public class BranchModel {
    private String imageUrl;
    private String name;
    private String description;
    private String branch_code;

    public BranchModel(String imageUrl, String name, String description, String branch_code) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.branch_code = branch_code;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBranch_code() {
        return branch_code;
    }

    public void setBranch_code(String branch_code) {
        this.branch_code = branch_code;
    }
}
