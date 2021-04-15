package com.example.motorbazar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorbazar.Fragments.HomePackage.HomeTabPackage.AllVehicles;
import com.example.motorbazar.Fragments.ListingsPackage.ListingsTabPackage.SellingLists;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditVehicleDetails extends AppCompatActivity {

    TextInputEditText brandEditText, modelEditText, engineEditText, priceEditText, nameEditText,
                        mileageEditText, registrationEditText, odometerEditText, descriptionEditText;
    Button updateBtn;
    int selectedVehicelId, categoryId, brandId, modelId, engineId, condition;

    String modelName, brandName, engineCC, price, vehicleName, regNo, mileage, odometerReading, description,
            longitude, latitude;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_vehicle_details_layout);
        uiInitialize();
        Intent intent = getIntent();
        selectedVehicelId = intent.getIntExtra("vehicleId",0);
        getVehicleDetails();

        updateBtnClicked();

    }
    private void uiInitialize(){
        brandEditText = findViewById(R.id.brand);
        modelEditText = findViewById(R.id.model);
        engineEditText = findViewById(R.id.engine);
        priceEditText = findViewById(R.id.price);
        nameEditText = findViewById(R.id.name);
        mileageEditText = findViewById(R.id.mileage);
        registrationEditText = findViewById(R.id.registration);
        odometerEditText = findViewById(R.id.odometer);
        descriptionEditText = findViewById(R.id.description);
        updateBtn = findViewById(R.id.update);
        brandEditText.setEnabled(false);
        modelEditText.setEnabled(false);
        engineEditText.setEnabled(false);
        registrationEditText.setEnabled(false);
    }
    public void getVehicleDetails(){
        for(int i = 0; i< AllVehicles.unslodVehicleList.size(); i++){
            if(SellingLists.myVehiclesList.get(i).getId().equals(selectedVehicelId)){
                categoryId = SellingLists.myVehiclesList.get(i).getVehicleCategoryId();
                brandId = SellingLists.myVehiclesList.get(i).getVehicleBrandId();
                modelId = SellingLists.myVehiclesList.get(i).getVehicleModelId();
                engineId = SellingLists.myVehiclesList.get(i).getVehicleEngineId();
                modelName = SellingLists.myVehiclesList.get(i).getVehicleModel().getName();
                brandName = SellingLists.myVehiclesList.get(i).getVehicleBrand().getName();
                engineCC =  SellingLists.myVehiclesList.get(i).getVehicleEngine().getName();
                price = SellingLists.myVehiclesList.get(i).getPrice().toString();
                vehicleName = SellingLists.myVehiclesList.get(i).getName();
                regNo = SellingLists.myVehiclesList.get(i).getRegistrationNumber();
                mileage = SellingLists.myVehiclesList.get(i).getMileage().toString();
                odometerReading = SellingLists.myVehiclesList.get(i).getOdometerReading().toString();
                description = SellingLists.myVehiclesList.get(i).getDescription();
                condition = SellingLists.myVehiclesList.get(i).getCondition();
                longitude = SellingLists.myVehiclesList.get(i).getLongitude();
                latitude = SellingLists.myVehiclesList.get(i).getLatitude();
                Toast.makeText(this, "details: "+modelName, Toast.LENGTH_SHORT).show();
                //setting value to the edit text
                brandEditText.setText(brandName);
                modelEditText.setText(modelName);
                engineEditText.setText(engineCC);
                priceEditText.setText(price);
                nameEditText.setText(vehicleName);
                mileageEditText.setText(mileage);
                registrationEditText.setText(regNo);
                odometerEditText.setText(odometerReading);
                descriptionEditText.setText(description);
            }
            break;
        }
    }
    private void updateBtnClicked(){
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(priceEditText.getText().toString().equals(price)
                   && nameEditText.getText().toString().equals(vehicleName)
                    && mileageEditText.getText().toString().equals(mileage)
                    && odometerEditText.getText().toString().equals(odometerReading)
                    && descriptionEditText.getText().toString().equals(description))
                {
                    Toast.makeText(EditVehicleDetails.this, "No changes done", Toast.LENGTH_SHORT).show();
                }
                else if(priceEditText.getText().toString().isEmpty()){
                    priceEditText.setError("Price required");
                }
                else if(nameEditText.getText().toString().isEmpty()){
                    nameEditText.setError("Name is required");
                }
                else if(mileageEditText.getText().toString().isEmpty()){
                    mileageEditText.setError("Mileage required");
                }
                else if(odometerEditText.getText().toString().isEmpty()){
                    odometerEditText.setError("Odometer reading required");
                }
                else if(descriptionEditText.getText().toString().isEmpty()){
                    descriptionEditText.setError("Description required");
                }
                else{
                    //Toast.makeText(EditVehicleDetails.this, "Ready to update", Toast.LENGTH_SHORT).show();
                    callingUpdateApi();
                }
            }
        });
    }
    private void callingUpdateApi(){
        String url = "http://motorbazartoken.ghimiremilan.com.np/api/vehicle";
        RequestQueue queue = Volley.newRequestQueue(this);
        final String mtoken = TokenHolder.userToken;
        progressDialog();
        JSONObject obj = new JSONObject();
        try {
            obj.put("Id",selectedVehicelId);
            obj.put("name",nameEditText.getText().toString());
            obj.put("status",true);
            obj.put("mileage",Double.parseDouble(mileageEditText.getText().toString()));
            obj.put("RegistrationNumber",regNo);
            obj.put("OdometerReading",Double.parseDouble(odometerEditText.getText().toString()));
            obj.put("Price",Double.parseDouble(priceEditText.getText().toString()));
            obj.put("Condition",condition);
            obj.put("Longitude",longitude);
            obj.put("Latitude",latitude);
            obj.put("Description",descriptionEditText.getText().toString());
            obj.put("isSold",false);
            obj.put("VehicleCategoryId",categoryId);
            obj.put("VehicleBrandId",brandId);
            obj.put("VehicleModelId",modelId);
            obj.put("VehicleEngineId",engineId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            String result = response.getString("message");
                            Toast.makeText(EditVehicleDetails.this, result, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditVehicleDetails.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + mtoken);
                return params;
            }
        };
        queue.add(request);
    }
    private void progressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading vehicles");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

}
