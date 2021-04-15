package com.example.motorbazar.model;

public class WishlistModel {
    int vehicleId;
    int wishlistId;
    public WishlistModel(int vehicleId, int wishlistId){
        this.vehicleId = vehicleId;
        this.wishlistId =  wishlistId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public int getWishlistId() {
        return wishlistId;
    }
}
