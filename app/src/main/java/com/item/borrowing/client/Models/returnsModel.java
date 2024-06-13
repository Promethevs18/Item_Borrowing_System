package com.item.borrowing.client.Models;

public class returnsModel {
    String ItemExistence, assetName, borrower, brandModel, date, itemImage;

    public returnsModel() {
    }

    public returnsModel(String itemExistence, String assetName, String borrower, String brandModel, String date, String itemImage) {
        ItemExistence = itemExistence;
        this.assetName = assetName;
        this.borrower = borrower;
        this.brandModel = brandModel;
        this.date = date;
        this.itemImage = itemImage;
    }

    public String getItemExistence() {
        return ItemExistence;
    }

    public void setItemExistence(String itemExistence) {
        ItemExistence = itemExistence;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
