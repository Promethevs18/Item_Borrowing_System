package com.item.borrowing.client.Models;

public class borrowModel {
    private String borrower;
    private String date;
    private String email;
    private String tools;
    private String transactionCode;
    private String profileImage;

    public borrowModel() {
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public borrowModel(String borrower, String date, String email, String tools, String transactionCode, String profileImage) {
        this.borrower = borrower;
        this.date = date;
        this.email = email;
        this.tools = tools;
        this.transactionCode = transactionCode;
        this.profileImage = profileImage;
    }
}
