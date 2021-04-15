package com.example.motorbazar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.motorbazar.Adapters.AllVehiclesAdapter;
import com.example.motorbazar.Fragments.HomePackage.HomeTabPackage.AllVehicles;
import com.example.motorbazar.Fragments.ListingsPackage.ListingsTabPackage.WishLists;
import com.example.motorbazar.RegisterPackage.RegisterUser;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.User_Dashboard.UserDashboard;
import com.example.motorbazar.model.ListedVehicle;
import com.example.motorbazar.model.VehicleImage;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class VehicleDetails extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 0;
    String mtoken = TokenHolder.userToken;
    ViewPager viewPager;
    TextView txtModelName, txtEngineName, txtBrandName, txtPrice, txtRegNo, txtOdometerReading;
    RatingBar ratingBar;
    Button book_btn, addToWish_btn, ownerdetails_btn, locationDetailBtn;
    int selectedVehicelId;
    String modelName, brandName, engineCC, price, regNo, odometerReading,
           ownerName, firstName,lastName, ownerEmail, ownerCity, ownerCountry, ownerContact, ownerPic,
            longitude, latitude;
    int vehicleCondition;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_details);
        uiInitialize();
        //setLayout();
        ownerDetailsBtnClick();
        wishVehicle();
        bookVehicle();
        locationClicked();

        //SliderViewpagerAdapter viewPagerAdapter = new SliderViewpagerAdapter(this);
        //viewPager.setAdapter(viewPagerAdapter);

        Intent intent = getIntent();
        selectedVehicelId = intent.getIntExtra("vehicleId",0);
        Toast.makeText(this, "vehicleId: "+selectedVehicelId, Toast.LENGTH_SHORT).show();

        getVehicleDetails();

        txtModelName.setText(modelName);
        txtBrandName.setText(brandName);
        txtPrice.setText(price);
        txtEngineName.setText(engineCC);
        txtRegNo.setText(regNo);
        txtOdometerReading.setText(odometerReading);
        switch (vehicleCondition) {
            case 1:
                ratingBar.setRating(1.0f);
                break;
            case 2:
                ratingBar.setRating(2.0f);
                break;
            case 3:
                ratingBar.setRating(3.0f);
                break;
            case 4:
                ratingBar.setRating(4.0f);
                break;
            case 5:
                ratingBar.setRating(5.0f);
                break;
        }


    }
    public void uiInitialize() {
        viewPager = findViewById(R.id.slider_viewPager);
        ownerdetails_btn = findViewById(R.id.ownerDetails_btn);
        book_btn = findViewById(R.id.book_btn);
        addToWish_btn = findViewById(R.id.add_to_wishlist);
        locationDetailBtn = findViewById(R.id.locationDetail_btn);
        txtModelName = findViewById(R.id.vehicle_model);
        txtBrandName = findViewById(R.id.vehicle_brand);
        txtEngineName = findViewById(R.id.vehicle_engine);
        txtPrice = findViewById(R.id.vehicle_price);
        txtOdometerReading = findViewById(R.id.vehicle_odometerReading);
        txtRegNo = findViewById(R.id.vehicle_regNo);
        ratingBar = findViewById(R.id.rating);
    }
    public void getVehicleDetails(){
        for(int i=0; i<AllVehicles.unslodVehicleList.size(); i++){
            if(AllVehicles.unslodVehicleList.get(i).getId().equals(selectedVehicelId)){
                modelName = AllVehicles.unslodVehicleList.get(i).getVehicleModel().getName();
                brandName = AllVehicles.unslodVehicleList.get(i).getVehicleBrand().getName();
                engineCC =  AllVehicles.unslodVehicleList.get(i).getVehicleEngine().getName();
                price = AllVehicles.unslodVehicleList.get(i).getPrice().toString();
                regNo = AllVehicles.unslodVehicleList.get(i).getRegistrationNumber();
                odometerReading = AllVehicles.unslodVehicleList.get(i).getOdometerReading().toString();
                firstName = AllVehicles.unslodVehicleList.get(i).getApplicationUser().getFirstName();
                lastName = AllVehicles.unslodVehicleList.get(i).getApplicationUser().getLastName();
                ownerName = firstName+" "+lastName;
                ownerEmail = AllVehicles.unslodVehicleList.get(i).getApplicationUser().getEmail();
                ownerCountry = AllVehicles.unslodVehicleList.get(i).getApplicationUser().getCountry();
                ownerCity = AllVehicles.unslodVehicleList.get(i).getApplicationUser().getCity();
                ownerContact = AllVehicles.unslodVehicleList.get(i).getApplicationUser().getPhoneNumber();
                ownerPic = AllVehicles.unslodVehicleList.get(i).getApplicationUser().getProfileImage();
                longitude = AllVehicles.unslodVehicleList.get(i).getLongitude();
                latitude = AllVehicles.unslodVehicleList.get(i).getLatitude();
                vehicleCondition = AllVehicles.unslodVehicleList.get(i).getCondition();

                if(AllVehicles.unslodVehicleList.get(i).getVehicleImages().size()>0){
                    ArrayList<VehicleImage> imageList = (ArrayList<VehicleImage>) AllVehicles.unslodVehicleList.get(i).getVehicleImages();
                    SliderViewpagerAdapter viewPagerAdapter = new SliderViewpagerAdapter(this, imageList);
                    viewPager.setAdapter(viewPagerAdapter);
                }
            }

        }
    }

    public void ownerDetailsBtnClick() {
        ownerdetails_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlePermission();
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(VehicleDetails.this);
                bottomSheetDialog.setContentView(R.layout.user_details_layout);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                //initializing buttomsheet components
                CircularImageView userImage = bottomSheetDialog.findViewById(R.id.image_user);
                TextView txt_name = bottomSheetDialog.findViewById(R.id.user_name);
                ImageView cancelImage = bottomSheetDialog.findViewById(R.id.cancel_bottomsheet);
                TextView txt_address = bottomSheetDialog.findViewById(R.id.user_address);
                TextView txt_country = bottomSheetDialog.findViewById(R.id.user_country);
                TextView txt_email = bottomSheetDialog.findViewById(R.id.user_email);
                TextView txt_contact = bottomSheetDialog.findViewById(R.id.user_contact);
                Button call_btn = bottomSheetDialog.findViewById(R.id.call_user_btn);
                Button sms_btn = bottomSheetDialog.findViewById(R.id.sms_user_btn);
                //showing bottomsheet
                bottomSheetDialog.show();
                //setting values to buttonsheet
                Glide.with(VehicleDetails.this).load(ownerPic).into(userImage);
                txt_name.setText(ownerName);
                txt_country.setText(ownerCountry);
                txt_contact.setText(ownerContact);
                txt_address.setText(ownerCity);
                txt_email.setText(ownerEmail);


                //for cross btn in buttom sheet
                cancelImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog.cancel();
                    }
                });
                //for call button in button sheet
                call_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String phone = ownerContact;

                        AlertDialog.Builder builder = new AlertDialog.Builder(VehicleDetails.this);
                        builder.setTitle("Make a Call");
                        builder.setMessage("Do you want to call the user?");

                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            //@RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(PhoneCallActivity.this, "ok clicked", Toast.LENGTH_SHORT).show();
                                String s = "tel:" + phone;
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse(s));

                                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED){
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST);
                                }
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.create().show();
                    }

                });

                //for sms bottom clicked
                sms_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String phone = ownerContact;
                        AlertDialog.Builder builder = new AlertDialog.Builder(VehicleDetails.this);
                        builder.setTitle("Send SMS");
                        builder.setMessage("Do you want to send SMS to the user?");

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Toast.makeText(PhoneCallActivity.this, "ok clicked", Toast.LENGTH_SHORT).show();
                                String s = "tel:" + phone;
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.putExtra("address",phone);
                                intent.putExtra("sms_body","This is my number");
                                intent.setType("vnd.android-dir/mms-sms");
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        builder.create().show();
                    }
                });

            }
        });

    }

    public void wishVehicle(){
        addToWish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VehicleDetails.this);
                builder.setTitle("Add to wishlist?");
                builder.setMessage("Do you want to add to your Wishlist?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(wishValidation()){
                            //Toast.makeText(VehicleDetails.this, "true", Toast.LENGTH_SHORT).show();

                            addingToWishlistAPI(selectedVehicelId,mtoken);
                            startActivity(new Intent(VehicleDetails.this, UserDashboard.class));

                        }
                        else{
                            Toast.makeText(VehicleDetails.this, "Already exist in your wishlist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }
    private boolean wishValidation(){
        for(int i = 0; i<AllVehicles.userWishList.size();i++){
            if(AllVehicles.userWishList.get(i).getVehicleId()==selectedVehicelId){
                //Toast.makeText(VehicleDetails.this, "Already exist in your wishlist", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
    public void bookVehicle() {
        book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VehicleDetails.this);
                builder.setTitle("Make Booking");
                builder.setMessage("Do you want to book the vehicle?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(bookValidation()){
                            if(AllVehicles.userBooking.size()<3){
                                //Toast.makeText(VehicleDetails.this, "booked item: ", Toast.LENGTH_SHORT).show();
                                bookingVehicleAPI(selectedVehicelId,mtoken);
                                addToNotificationAPI(selectedVehicelId,mtoken);
                                startActivity(new Intent(VehicleDetails.this, UserDashboard.class));
                            }
                            else{
                                //Toast.makeText(VehicleDetails.this, "sorry!!You can book only 3 vehicles.", Toast.LENGTH_SHORT).show();
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VehicleDetails.this);
                                builder.setTitle("Sorry!!");
                                builder.setMessage("You can not book more than 3 vehicles.");

                                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.create().show();
                            }
                        }
                        else{
                            Toast.makeText(VehicleDetails.this, "vehicle already booked.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();

            }
        });
    }
    private boolean bookValidation(){
        for(int i = 0; i<AllVehicles.userBooking.size();i++){
            if(AllVehicles.userBooking.get(i).getVehicleId()==selectedVehicelId){
                //Toast.makeText(VehicleDetails.this, "Already exist in your wishlist", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public void addingToWishlistAPI(final int vehicleId, final String userToken){
        progressDialog();
        String URL = "http://motorbazartoken.ghimiremilan.com.np/api/wishlist";
        RequestQueue queue = Volley.newRequestQueue(VehicleDetails.this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status",true);
            jsonObject.put("vehicleId", vehicleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            String result = response.getString("message");
                            Toast.makeText(VehicleDetails.this, result, Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(VehicleDetails.this, "addwish error: ", Toast.LENGTH_SHORT).show();
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null){
                            switch(response.statusCode){
                                case 400:
                                    json = new String(response.data);
                                    json = trimMessage(json, "message");
                                    if(json != null)
                                        Toast.makeText(VehicleDetails.this, json, Toast.LENGTH_SHORT).show();
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
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + userToken);
                return params;
            }
        };
        queue.add(request);
    }

    public void bookingVehicleAPI(final int vehicleId, final String userToken){
        String bookingURL = "http://motorbazartoken.ghimiremilan.com.np/api/booking";
        RequestQueue queue = Volley.newRequestQueue(VehicleDetails.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status",true);
            jsonObject.put("vehicleId", vehicleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                bookingURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String result = response.getString("message");
                            Toast.makeText(VehicleDetails.this, result, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VehicleDetails.this, "objError: "+error, Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + userToken);
                return params;
            }
        };
        queue.add(request);
    }

    //for adding to notification table
    public void addToNotificationAPI(final int vehicleId, final String userToken){
        String notificationURL = "http://motorbazartoken.ghimiremilan.com.np/api/notification";
        RequestQueue queue = Volley.newRequestQueue(VehicleDetails.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status",true);
            jsonObject.put("vehicleId", vehicleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                notificationURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(VehicleDetails.this, "objResponse "+response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(VehicleDetails.this, "Added to notification", Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + userToken);
                return params;
            }
        };
        queue.add(request);
    }
    //permission for phone call
    public void handlePermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST);
        }
    }
    public void locationClicked(){
        locationDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleDetails.this,SingleVehicleMapInfo.class);

                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                startActivity(intent);

                //startActivity(new Intent(VehicleDetails.this, SingleVehicleMapInfo.class));
                Toast.makeText(VehicleDetails.this, "lng: "+longitude
                        +"lat: "+latitude, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void progressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

}
