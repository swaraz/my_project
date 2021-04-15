package com.example.motorbazar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyVehicles {

    @SerializedName("vehicleCategoryId")
    @Expose
    private Integer vehicleCategoryId;
    @SerializedName("vehicleBrandId")
    @Expose
    private Integer vehicleBrandId;
    @SerializedName("vehicleModelId")
    @Expose
    private Integer vehicleModelId;
    @SerializedName("vehicleEngineId")
    @Expose
    private Integer vehicleEngineId;
    @SerializedName("mileage")
    @Expose
    private Double mileage;
    @SerializedName("registrationNumber")
    @Expose
    private String registrationNumber;
    @SerializedName("odometerReading")
    @Expose
    private Double odometerReading;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("condition")
    @Expose
    private Integer condition;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("isSold")
    @Expose
    private Boolean isSold;
    @SerializedName("vehicleImages")
    @Expose
    private Object vehicleImages;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("status")
    @Expose
    private Boolean status;

    public Integer getVehicleCategoryId() {
        return vehicleCategoryId;
    }

    public void setVehicleCategoryId(Integer vehicleCategoryId) {
        this.vehicleCategoryId = vehicleCategoryId;
    }

    public Integer getVehicleBrandId() {
        return vehicleBrandId;
    }

    public void setVehicleBrandId(Integer vehicleBrandId) {
        this.vehicleBrandId = vehicleBrandId;
    }

    public Integer getVehicleModelId() {
        return vehicleModelId;
    }

    public void setVehicleModelId(Integer vehicleModelId) {
        this.vehicleModelId = vehicleModelId;
    }

    public Integer getVehicleEngineId() {
        return vehicleEngineId;
    }

    public void setVehicleEngineId(Integer vehicleEngineId) {
        this.vehicleEngineId = vehicleEngineId;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Double getOdometerReading() {
        return odometerReading;
    }

    public void setOdometerReading(Double odometerReading) {
        this.odometerReading = odometerReading;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsSold() {
        return isSold;
    }

    public void setIsSold(Boolean isSold) {
        this.isSold = isSold;
    }

    public Object getVehicleImages() {
        return vehicleImages;
    }

    public void setVehicleImages(Object vehicleImages) {
        this.vehicleImages = vehicleImages;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
