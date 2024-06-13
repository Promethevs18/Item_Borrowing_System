package com.item.borrowing.client.Models;

public class calibrationModels {
    String  instrumentType, iic, dateForCalibration, status, model;

    public calibrationModels(String instrumentType, String iic, String dateForCalibration, String status, String model) {
        this.instrumentType = instrumentType;
        this.iic = iic;
        this.dateForCalibration = dateForCalibration;
        this.status = status;
        this.model = model;
    }

    public calibrationModels() {
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getIic() {
        return iic;
    }

    public void setIic(String iic) {
        this.iic = iic;
    }

    public String getDateForCalibration() {
        return dateForCalibration;
    }

    public void setDateForCalibration(String dateForCalibration) {
        this.dateForCalibration = dateForCalibration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
