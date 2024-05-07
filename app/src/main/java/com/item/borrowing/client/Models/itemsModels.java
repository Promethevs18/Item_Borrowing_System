package com.item.borrowing.client.Models;

public class itemsModels {
    String assetName, brandModel, genSpecs, iic, itemImage, location;

    public itemsModels() {
    }

    public itemsModels(String assetName, String brandModel, String genSpecs, String iic, String itemImage, String location) {
        this.assetName = assetName;
        this.brandModel = brandModel;
        this.genSpecs = genSpecs;
        this.iic = iic;
        this.itemImage = itemImage;
        this.location = location;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getBrandModel() {
        return brandModel;
    }

    public void setBrandModel(String brandModel) {
        this.brandModel = brandModel;
    }

    public String getGenSpecs() {
        return genSpecs;
    }

    public void setGenSpecs(String genSpecs) {
        this.genSpecs = genSpecs;
    }

    public String getIic() {
        return iic;
    }

    public void setIic(String iic) {
        this.iic = iic;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
