package com.example.motorbazar.model;

public class VehicleBrand {
    int _categotyId;
    int _id;
    String _name;
    Boolean _status;

    public VehicleBrand(int id, String name, Boolean status){

        this._id = id;
        this._name = name;
        this._status = status;
    }
    public String get_name() {
        return _name;
    }

    public int get_id() {
        return _id;
    }

    public int get_categotyId(){
        return _categotyId;
    }
}
