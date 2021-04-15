
package com.example.motorbazar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListedVehicle {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("applicationUserId")
    @Expose
    private String applicationUserId;
    @SerializedName("applicationUser")
    @Expose
    private ApplicationUser applicationUser;
    @SerializedName("vehicleCategoryId")
    @Expose
    private Integer vehicleCategoryId;
    @SerializedName("vehicleCategory")
    @Expose
    private VehicleCategory2 vehicleCategory;
    @SerializedName("vehicleBrandId")
    @Expose
    private Integer vehicleBrandId;
    @SerializedName("vehicleBrand")
    @Expose
    private VehicleBrand2 vehicleBrand;
    @SerializedName("vehicleModelId")
    @Expose
    private Integer vehicleModelId;
    @SerializedName("vehicleModel")
    @Expose
    private VehicleModel2 vehicleModel;
    @SerializedName("vehicleEngineId")
    @Expose
    private Integer vehicleEngineId;
    @SerializedName("vehicleEngine")
    @Expose
    private VehicleEngine2 vehicleEngine;
    @SerializedName("vehicleImages")
    @Expose
    private List<VehicleImage> vehicleImages = null;
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
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Object data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationUserId() {
        return applicationUserId;
    }

    public void setApplicationUserId(String applicationUserId) {
        this.applicationUserId = applicationUserId;
    }

    public ApplicationUser getApplicationUser() {
        return applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Integer getVehicleCategoryId() {
        return vehicleCategoryId;
    }

    public void setVehicleCategoryId(Integer vehicleCategoryId) {
        this.vehicleCategoryId = vehicleCategoryId;
    }

    public VehicleCategory2 getVehicleCategory() {
        return vehicleCategory;
    }

    public void setVehicleCategory(VehicleCategory2 vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public Integer getVehicleBrandId() {
        return vehicleBrandId;
    }

    public void setVehicleBrandId(Integer vehicleBrandId) {
        this.vehicleBrandId = vehicleBrandId;
    }

    public VehicleBrand2 getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(VehicleBrand2 vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public Integer getVehicleModelId() {
        return vehicleModelId;
    }

    public void setVehicleModelId(Integer vehicleModelId) {
        this.vehicleModelId = vehicleModelId;
    }

    public VehicleModel2 getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel2 vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public Integer getVehicleEngineId() {
        return vehicleEngineId;
    }

    public void setVehicleEngineId(Integer vehicleEngineId) {
        this.vehicleEngineId = vehicleEngineId;
    }

    public VehicleEngine2 getVehicleEngine() {
        return vehicleEngine;
    }

    public void setVehicleEngine(VehicleEngine2 vehicleEngine) {
        this.vehicleEngine = vehicleEngine;
    }

    public List<VehicleImage> getVehicleImages() {
        return vehicleImages;
    }

    public void setVehicleImages(List<VehicleImage> vehicleImages) {
        this.vehicleImages = vehicleImages;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
