package com.example.motorbazar.model;

public class VehicleModel {
    int _id;
    String _name;
    Boolean _status;

    public VehicleModel(int id, String name, Boolean status){
        this._id = id;
        this._name=name;
        this._status = status;
    }

    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }
}
