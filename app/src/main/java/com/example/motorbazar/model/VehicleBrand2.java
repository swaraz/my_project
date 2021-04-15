package com.example.motorbazar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleBrand2 {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("vehicleCategoryId")
    @Expose
    private Integer vehicleCategoryId;
    @SerializedName("vehcileCategory")
    @Expose
    private Object vehcileCategory;
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

    public Integer getVehicleCategoryId() {
        return vehicleCategoryId;
    }

    public void setVehicleCategoryId(Integer vehicleCategoryId) {
        this.vehicleCategoryId = vehicleCategoryId;
    }

    public Object getVehcileCategory() {
        return vehcileCategory;
    }

    public void setVehcileCategory(Object vehcileCategory) {
        this.vehcileCategory = vehcileCategory;
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
