package com.example.motorbazar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.model.ApplicationUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyProfile extends AppCompatActivity {
    String profileUrl = "http://motorbazartoken.ghimiremilan.com.np/api/Auth/profile";
    String mtoken = TokenHolder.userToken;
    TextView nameTxt, usernameTxt, emailTxt, contactTxt, countryTxt, provienceTxt, cityTxt;
    ImageView userImage;
    ProgressDialog progressDialog;
    //String firstname, lastname,username, email, contact, provience, country, city, image;
    //ApplicationUser userDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        initializeUI();

        callProfileApi();

    }
    private void initializeUI(){
        nameTxt = findViewById(R.id.profile_name);
        usernameTxt = findViewById(R.id.profile_username);
        emailTxt   = findViewById(R.id.profile_email);
        contactTxt = findViewById(R.id.profile_contact);
        countryTxt = findViewById(R.id.profile_country);
        provienceTxt = findViewById(R.id.profile_provience);
        cityTxt = findViewById(R.id.profile_city);
        userImage = findViewById(R.id.profilePic);
    }
    private void callProfileApi(){
        progressDialog();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                profileUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MyProfile.this, response.toString(), Toast.LENGTH_SHORT).show();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        ApplicationUser userDetails = gson.fromJson(String.valueOf(response), ApplicationUser.class);

                        String fullname = userDetails.getFirstName()+" "+userDetails.getLastName();
                        usernameTxt.setText(userDetails.getUserName());
                        nameTxt.setText(fullname);
                        emailTxt.setText(userDetails.getEmail());
                        contactTxt.setText(userDetails.getPhoneNumber());
                        countryTxt.setText(userDetails.getCountry());
                        cityTxt.setText(userDetails.getCity());
                        provienceTxt.setText(userDetails.getProvience());
                        Glide.with(MyProfile.this).load(userDetails.getProfileImage()).into(userImage);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(MyProfile.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
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
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
}
