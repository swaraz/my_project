package com.example.motorbazar.AddingItemsPackage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.motorbazar.R;
import com.example.motorbazar.RegisterPackage.RegisterUser;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.model.VehicleBrand;
import com.example.motorbazar.model.VehicleCategory;
import com.example.motorbazar.model.VehicleCategory2;
import com.example.motorbazar.model.VehicleEngine;
import com.example.motorbazar.model.VehicleModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class AddingVehicle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FusedLocationProviderClient fusedLocationProviderClient;
    ProgressDialog progressDialog;
    Button nxt_btn;
    TextInputEditText priceLayout, mileageLayout, odometerLayout, registrationLayout,
            descriptionLayout, vehicleNameLayout;
    List<String> categoryList = new ArrayList<String>();
    List<String> brandList = new ArrayList<String>();
    List<String> modelList = new ArrayList<String>();
    List<String> engineList = new ArrayList<String>();

    int selectedCategoryId, selectedBrandId, selectedModelId, selectedEngineId, selectedCondition;
    double longitude, latitude;

    private List<VehicleCategory> vehicleCategoryList = new ArrayList<>();
    private List<VehicleBrand> vehicleBrandList = new ArrayList<>();
    private List<VehicleModel> vehicleModelList = new ArrayList<>();
    private List<VehicleEngine> vehicleEngineList = new ArrayList<>();

    private Spinner spinnerCategory, spinnerBrand, spinnerModel, spinnerEngine, spinnerCondition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_vehicle);
        uiInitialize();
        onNextPressed();
        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        spinnerCategory.setOnItemSelectedListener(this);

        spinnerBrand = (Spinner) findViewById(R.id.spinner_brand);
        spinnerBrand.setOnItemSelectedListener(this);

        spinnerModel = (Spinner) findViewById(R.id.spinner_model);
        spinnerModel.setOnItemSelectedListener(this);

        spinnerEngine = (Spinner) findViewById(R.id.spinner_engine);
        spinnerEngine.setOnItemSelectedListener(this);

        spinnerCondition = (Spinner) findViewById(R.id.spinner_condition);
        spinnerCondition.setOnItemSelectedListener(this);

        Log.d("DownloadCategory", "Start");
        downloadCategory();

        categoryOnSelection();
        brandOnSelection();
        modelOnSelection();
        engineOnSelection();
        conditionOnSelection();
        getCurrentLocation();

    }

    private void categoryOnSelection() {
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TAG", "Spinner selected ID : " + parent.getItemIdAtPosition(position));
                Log.d("TAG", "Spinner selected name : " + parent.getItemAtPosition(position));
                int selectedId = (int) parent.getItemIdAtPosition(position);
                selectedCategoryId = vehicleCategoryList.get(selectedId).get_id();
                Log.d("TAG", "Spinner selected name : " + vehicleCategoryList.get(selectedId).get_id());
                if (selectedCategoryId > 0) {
                    downloadBrand(selectedCategoryId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Another interface callback
            }
        });
    }

    private void brandOnSelection() {
        spinnerBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedId = (int) parent.getItemIdAtPosition(position);
                selectedBrandId = vehicleBrandList.get(selectedId).get_id();
                if (selectedBrandId > 0) {
                    downloadModel(selectedBrandId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void modelOnSelection() {
        spinnerModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedId = (int) adapterView.getItemIdAtPosition(i);
                selectedModelId = vehicleModelList.get(selectedId).get_id();
                if (selectedModelId > 0) {
                    downloadEngine();
                    //Toast.makeText(AddingVehicle.this, "Model Id: "+selectedModelId, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void engineOnSelection() {
        spinnerEngine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedId = (int) adapterView.getItemIdAtPosition(i);
                selectedEngineId = vehicleEngineList.get(selectedId).get_id();
                if (selectedEngineId > 0) {
                    //Toast.makeText(AddingVehicle.this, "Engine Id: "+selectedEngineId, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void conditionOnSelection() {
        spinnerCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedConditionId = (int) adapterView.getItemIdAtPosition(i);
                selectedCondition = selectedConditionId + 1;
                //Toast.makeText(AddingVehicle.this, "Condition Id: "+selectedCondition, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    private void populateCategorySpinner() {
        for (VehicleCategory item : vehicleCategoryList) {
            categoryList.add(item.get_name());
        }
        //Creating the ArrayAdapter instance having the category name list
        ArrayAdapter ca = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList);
        ca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerCategory.setAdapter(ca);
    }

    private void populateBrandSpinner() {
        if (brandList.size() > 0) {
            brandList.clear();
        }
        for (VehicleBrand item : vehicleBrandList) {
            brandList.add(item.get_name());
        }
        //Creating the ArrayAdapter instance having the category name list
        ArrayAdapter ba = new ArrayAdapter(this, android.R.layout.simple_spinner_item, brandList);
        ba.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrand.setAdapter(ba);
    }

    public void populateModelSpinner() {
        if (modelList.size() > 0) {
            modelList.clear();
        }
        for (VehicleModel item : vehicleModelList) {
            modelList.add(item.get_name());
        }
        ArrayAdapter ma = new ArrayAdapter(this, android.R.layout.simple_spinner_item, modelList);
        ma.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModel.setAdapter(ma);
    }

    public void populateEngineSpinner() {
        if (engineList.size() > 0) {
            engineList.clear();
        }
        for (VehicleEngine item : vehicleEngineList) {
            engineList.add(item.get_engineCC());
        }
        ArrayAdapter ea = new ArrayAdapter(this, android.R.layout.simple_spinner_item, engineList);
        ea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEngine.setAdapter(ea);
    }

    private void downloadCategory() {
        final String mJSONURLString = "http://motorbazartoken.ghimiremilan.com.np/api/VehicleCategory";
        final String mtoken = TokenHolder.userToken;
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mJSONURLString,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject categories = response.getJSONObject(i);
                                // Get the current student (json object) data
                                int id = 0;
                                try {
                                    id = Integer.parseInt(categories.getString("id"));
                                } catch (NumberFormatException nfe) {
                                    System.out.println("Could not parse " + nfe);
                                }
                                String name = categories.getString("name");
                                String status = categories.getString("status");
                                if (status.equals("true")) {
                                    vehicleCategoryList.add(new VehicleCategory(id, name, true));
                                } else {
                                    vehicleCategoryList.add(new VehicleCategory(id, name, false));
                                }
                                Log.d("TAG", "Name is : " + name);
                            }
                            Log.d("TAG", "Array is : " + vehicleCategoryList.toString());
                            populateCategorySpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "Error occured " + error.toString());
                        Toast.makeText(AddingVehicle.this, "Error: " + error, Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + mtoken);
                return params;
            }
        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    private void downloadBrand(int categoryId) {
        final String brandURL = "http://motorbazartoken.ghimiremilan.com.np/api/VehicleBrand/VehicleCategory/" + categoryId;
        final String mtoken = TokenHolder.userToken;
        //initialize new request queue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        vehicleBrandList.clear();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                brandURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        //Toast.makeText(AddingVehicle.this, "Response: "+response, Toast.LENGTH_SHORT).show();
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject brands = response.getJSONObject(i);

                                // Get the current student (json object) data
                                int id = 0;
                                try {
                                    id = Integer.parseInt(brands.getString("id"));
                                } catch (NumberFormatException nfe) {
                                    System.out.println("Could not parse " + nfe);
                                }
                                String name = brands.getString("name");
                                String status = brands.getString("status");
                                if (status.equals("true")) {
                                    vehicleBrandList.add(new VehicleBrand(id, name, true));
                                } else {
                                    vehicleBrandList.add(new VehicleBrand(id, name, false));
                                }
                                Log.d("TAG", "Name is : " + name);
                            }
                            Log.d("TAG", "Array is : " + vehicleBrandList.toString());
                            populateBrandSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddingVehicle.this, "Response: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + mtoken);
                return params;
            }
        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
        // ToDO download brand from here using volley as done above
        // here you need to pass parameter in   api/VehicleBrand/VehicleCategory/[categoryId]
    }

    private void downloadModel(int brandId) {
        final String brandURL = "http://motorbazartoken.ghimiremilan.com.np/api/vehiclemodel/vehiclebrand/" + brandId;
        final String mtoken = TokenHolder.userToken;
        vehicleModelList.clear();
        //initialize new request queue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                brandURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        //Toast.makeText(AddingVehicle.this, "Response: "+response, Toast.LENGTH_SHORT).show();
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject models = response.getJSONObject(i);

                                // Get the current student (json object) data
                                int id = 0;
                                try {
                                    id = Integer.parseInt(models.getString("id"));
                                } catch (NumberFormatException nfe) {
                                    System.out.println("Could not parse " + nfe);
                                }
                                String name = models.getString("name");
                                String status = models.getString("status");
                                if (status.equals("true")) {
                                    vehicleModelList.add(new VehicleModel(id, name, true));
                                } else {
                                    vehicleModelList.add(new VehicleModel(id, name, false));
                                }
                                Log.d("TAG", "Name is : " + name);
                            }
                            Log.d("TAG", "Array is : " + vehicleModelList.toString());
                            populateModelSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddingVehicle.this, "Response: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + mtoken);
                return params;
            }
        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    private void downloadEngine() {
        final String engineURL = "http://motorbazartoken.ghimiremilan.com.np/api/vehicleengine";
        final String mtoken = TokenHolder.userToken;
        vehicleEngineList.clear();
        //initialize new request queue instance
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                engineURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON
                        try {
                            // Loop through the array elements
                            for (int i = 0; i < response.length(); i++) {
                                // Get current json object
                                JSONObject engines = response.getJSONObject(i);

                                int id = Integer.parseInt(engines.getString("id"));
                                String name = engines.getString("name");
                                String status = engines.getString("status");
                                if (status.equals("true")) {
                                    vehicleEngineList.add(new VehicleEngine(id, name, true));
                                } else {
                                    vehicleEngineList.add(new VehicleEngine(id, name, false));
                                }
                                Log.d("TAG", "Name is : " + name);
                            }
                            Log.d("TAG", "Array is : " + vehicleEngineList.toString());
                            populateEngineSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddingVehicle.this, "Response: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + mtoken);
                return params;
            }
        };
        // Add JsonArrayRequest to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void uiInitialize() {
        nxt_btn = findViewById(R.id.next);
        priceLayout = findViewById(R.id.price);
        mileageLayout = findViewById(R.id.mileage);
        odometerLayout = findViewById(R.id.odometer_reading);
        registrationLayout = findViewById(R.id.registrationLotNumber);
        descriptionLayout = findViewById(R.id.description);
        vehicleNameLayout = findViewById(R.id.vehicle_name);
        //initialize fusedLocationproviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private boolean validate() {
        if ((vehicleNameLayout.getText().toString()).isEmpty() || vehicleNameLayout.getText().toString().equals(null)) {
            vehicleNameLayout.setError("Enter price of vehicle");
            return false;
        }
        if ((priceLayout.getText().toString()).isEmpty() || priceLayout.getText().toString().equals(null)) {
            priceLayout.setError("Enter price of vehicle");
            return false;
        }
        if ((mileageLayout.getText().toString()).isEmpty() || mileageLayout.getText().toString().equals(null)) {
            mileageLayout.setError("Enter mileage of vehicle");
            return false;
        }
        if ((odometerLayout.getText().toString()).isEmpty() || odometerLayout.getText().toString().equals(null)) {
            odometerLayout.setError("Enter odometer reading");
            return false;
        }
        if ((registrationLayout.getText().toString()).isEmpty() || registrationLayout.getText().toString().equals(null)) {
            registrationLayout.setError("Enter registration number");
            return false;
        }
        if ((descriptionLayout.getText().toString()).isEmpty()) {
            descriptionLayout.setError("Description required");
            return false;
        }
        return true;

    }

    private void onNextPressed() {
        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    /*Toast.makeText(AddingVehicle.this, "CategoryID: "+selectedCategoryId
                                            +"\nBrandId: "+selectedBrandId
                                            +"\nModelId: "+selectedModelId
                                            +"\nEngineId: "+selectedEngineId
                                            +"\nLongitude: "+longitude
                                            +"\nLatitude: "+latitude
                                            +"\nCondition: "+selectedCondition,
                                            Toast.LENGTH_SHORT).show();*/
                    addVehicleAPI();
                    //startActivity(new Intent(AddingVehicle.this, AddingPhoto.class));
                }
            }
        });
    }

    private void getCurrentLocation() {
        //check permission
        if (ActivityCompat.checkSelfPermission(AddingVehicle.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //initialize location
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            //initialize geocoder
                            Geocoder geocoder = new Geocoder(AddingVehicle.this,
                                    Locale.getDefault());
                            //initialize address list
                            List<Address> addresses = geocoder.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1
                            );
                            latitude = addresses.get(0).getLatitude();
                            longitude = addresses.get(0).getLongitude();
                            Toast.makeText(AddingVehicle.this, "lat: " + latitude + "\nlong: " + longitude, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            //when permission denied
            ActivityCompat.requestPermissions(AddingVehicle.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    public void addVehicleAPI() {
        progressDialog();
        final String userToken = TokenHolder.userToken;
        String addingDataURL = "http://motorbazartoken.ghimiremilan.com.np/api/vehicle";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject addingObj = new JSONObject();
        try {
            addingObj.put("name",vehicleNameLayout.getText().toString());
            addingObj.put("status", true);
            addingObj.put("mileage",Double.parseDouble(mileageLayout.getText().toString()));
            addingObj.put("RegistrationNumber",registrationLayout.getText().toString());
            addingObj.put("OdometerReading",Double.parseDouble(odometerLayout.getText().toString()));
            addingObj.put("price", Double.parseDouble(priceLayout.getText().toString()));
            addingObj.put("Condition",selectedCondition);
            addingObj.put("Longitude", String.valueOf(longitude));
            addingObj.put("Latitude",String.valueOf(latitude));
            addingObj.put("Description",descriptionLayout.getText().toString());
            addingObj.put("isSold",false);
            addingObj.put("VehicleCategoryId",selectedCategoryId);
            addingObj.put("VehicleBrandId",selectedBrandId);
            addingObj.put("VehicleModelId",selectedModelId);
            addingObj.put("VehicleEngineId", selectedEngineId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                addingDataURL,
                addingObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            String result = response.getString("message");
                            int vehicleId = response.getInt("vehicleId");
                            //Toast.makeText(AddingVehicle.this, result+"\nVehicleId: "+vehicleId, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddingVehicle.this, AddingPhoto.class);
                            intent.putExtra("vehicleId",vehicleId);
                            intent.putExtra("vehicleName",vehicleNameLayout.getText().toString());
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    json = new String(response.data);
                                    json = trimMessage(json, "message");
                                    if(json != null)
                                        Toast.makeText(AddingVehicle.this, json, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    }
                    public String trimMessage(String json, String key){
                        String trimmedString = null;
                        try{
                            JSONObject obj = new JSONObject(json);
                            trimmedString = obj.getString(key);
                        } catch(JSONException e){
                            e.printStackTrace();
                            return null;
                        }
                        return trimmedString;
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + userToken);
                return params;
            }
            /*@Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", "");
                params.put("status", "true");
                params.put("mileage", mileageLayout.getText().toString());
                params.put("RegistrationNumber", registrationLayout.getText().toString());
                params.put("OdometerReading", odometerLayout.getText().toString());
                params.put("Price", priceLayout.getText().toString());
                params.put("Condition", String.valueOf(selectedCondition));
                params.put("Longitude", String.valueOf(longitude));
                params.put("Latitude", String.valueOf(latitude));
                params.put("Description", descriptionLayout.getText().toString());
                params.put("isSold", "false");
                params.put("vehicleCategoryId", String.valueOf(selectedCategoryId));
                params.put("vehicleBrandId", String.valueOf(selectedBrandId));
                params.put("vehicleModelId", String.valueOf(selectedModelId));
                params.put("vehicleEngineId", String.valueOf(selectedEngineId));
                return params;
            }*/
        };
        queue.add(request);
    }

    private void progressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
}
