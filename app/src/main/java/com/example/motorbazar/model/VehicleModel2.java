package com.example.motorbazar.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleModel2 {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("vehicleBrandId")
    @Expose
    private Integer vehicleBrandId;
    @SerializedName("vehicleBrand")
    @Expose
    private Object vehicleBrand;
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

    public Integer getVehicleBrandId() {
        return vehicleBrandId;
    }

    public void setVehicleBrandId(Integer vehicleBrandId) {
        this.vehicleBrandId = vehicleBrandId;
    }

    public Object getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(Object vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
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
