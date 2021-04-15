package com.example.motorbazar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorbazar.AddingItemsPackage.AddingVehicle;
import com.example.motorbazar.Fragments.HomePackage.HomeTabPackage.AllVehicles;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.model.ListedVehicle;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VehiclesInMap extends AppCompatActivity implements OnMapReadyCallback {
    ArrayList<ListedVehicle> vehicles = new ArrayList<>();
    GoogleMap map;
    ArrayList<LatLng> locations = new ArrayList<LatLng>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicles_in_map_layout);
        //initialization
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        vehicles = AllVehicles.unslodVehicleList;

        for(int i=0; i<vehicles.size();i++){
            locations.add(new LatLng(Double.parseDouble(vehicles.get(i).getLatitude()),
                                    Double.parseDouble(vehicles.get(i).getLongitude())));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        for (int i=0; i<locations.size();i++){
            String vehicleName = vehicles.get(i).getName();
            map.addMarker(new MarkerOptions().position(locations.get(i))
                            .title(vehicleName));
            map.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(locations.get(i),15));
        }
   }
   
}
