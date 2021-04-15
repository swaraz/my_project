package com.example.motorbazar.model;

public class BookingsModel {
    int vehicleId;
    int bookingId;
    public BookingsModel(int vehicleId, int bookingId){
        this.vehicleId = vehicleId;
        this.bookingId =  bookingId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public int getBookingId() {
        return bookingId;
    }
}
