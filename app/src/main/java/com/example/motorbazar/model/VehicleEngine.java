package com.example.motorbazar.model;

public class VehicleEngine {
    int _id;
    String _engineCC;
    Boolean _status;
    public VehicleEngine(int id, String engineCC, Boolean status){
        this._id = id;
        this._engineCC = engineCC;
        this._status = status;
    }

    public int get_id() {
        return _id;
    }

    public String get_engineCC() {
        return _engineCC;
    }
}
