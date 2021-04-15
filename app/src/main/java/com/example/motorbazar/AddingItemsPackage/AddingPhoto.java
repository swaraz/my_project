package com.example.motorbazar.AddingItemsPackage;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorbazar.Fragments.HomePackage.HomeFragment;
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.User_Dashboard.UserDashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddingPhoto extends AppCompatActivity {
    ImageView image1, image2, image3, image4;
    Button add_btn;
    private static final int PERMISSION_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private int imagePosition;
    int vehicleId;
    String vehicleName;
    ProgressDialog progressDialog;

    Bitmap bitmap1,bitmap2, bitmap3, bitmap4;
    String imageOne, imageTwo, imageThree, imageFour;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_photos);
        uiInitialize();
        fetchImage();
        handlePermission();
        add_btn_clicked();

        vehicleId = getIntent().getIntExtra("vehicleId",0);
        vehicleName = getIntent().getStringExtra("vehicleName");
        Toast.makeText(this, "vehicleId: "+vehicleId+
                                        "\nVehicleName: "+vehicleName,
                                            Toast.LENGTH_SHORT).show();
    }
    public void uiInitialize(){
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        image4 = findViewById(R.id.image4);
        add_btn = findViewById(R.id.add_image);
    }
    public void fetchImage(){
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //selectImage();
                imagePosition=1;
                openGallery();
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePosition=2;
                openGallery();
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePosition=3;
                openGallery();
            }
        });
        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePosition=4;
                openGallery();
            }
        });
    }
    public void openGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
    public void handlePermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST:
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (imagePosition) {
            case 1:
                switch (requestCode) {
                    case RESULT_LOAD_IMAGE:
                        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
                            final Uri selectedImage = data.getData();
                            try {
                                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                                image1.setImageBitmap(bitmap1);
                                //imageString.add(bitmapToString(bitmap));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //image1.setImageURI(selectedImage);
                        }
                }
                break;
            case 2:
                switch (requestCode) {
                    case RESULT_LOAD_IMAGE:
                        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
                            final Uri selectedImage = data.getData();
                            try {
                                bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                                image2.setImageBitmap(bitmap2);
                                //imageString.add(bitmapToString(bitmap));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //image2.setImageURI(selectedImage);
                        }
                }
                break;
            case 3:
                switch (requestCode) {
                    case RESULT_LOAD_IMAGE:
                        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
                            final Uri selectedImage = data.getData();
                            try {
                                bitmap3 = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                                image3.setImageBitmap(bitmap3);
                                //imageString.add(bitmapToString(bitmap));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //image3.setImageURI(selectedImage);
                        }
                }
                break;
            case 4:
                switch (requestCode) {
                    case RESULT_LOAD_IMAGE:
                        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
                            final Uri selectedImage = data.getData();
                            try {
                                bitmap4 = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                                image4.setImageBitmap(bitmap4);
                                //imageString.add(bitmapToString(bitmap));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //image4.setImageURI(selectedImage);
                        }
                }
                break;
        }
    }
    private String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }
    public void add_btn_clicked(){
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image1.getDrawable()==null || image2.getDrawable()==null ||
                        image3.getDrawable()==null || image4.getDrawable()==null ){
                    AlertDialog alertDialog = new AlertDialog.Builder(AddingPhoto.this).create();
                    alertDialog.setTitle("Incomplete input");
                    alertDialog.setMessage("Please enter four photos.");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.show();
                }
                else{
                    addingVehicleImageApi1();
                    /*addingVehicleImageApi2();
                    addingVehicleImageApi3();
                    addingVehicleImageApi4();*/

//                    addingVehicleImageApi(bitmap1);
//                    addingVehicleImageApi(bitmap2);
//                    addingVehicleImageApi(bitmap3);
//                    addingVehicleImageApi(bitmap4);
                    //startActivity(new Intent(AddingPhoto.this, UserDashboard.class));
                    //Toast.makeText(AddingPhoto.this, "Image1 :"+imageOne, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(AddingPhoto.this, "all photo added", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean addingVehicleImageApi1(){
        String url = "http://motorbazartoken.ghimiremilan.com.np/api/vehicleImage";
        final String mtoken = TokenHolder.userToken;
        progressDialog();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name",vehicleName);
            jsonObj.put("status",true);
            jsonObj.put("vehicleId",vehicleId);
            jsonObj.put("imageurl",bitmapToString(bitmap1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            String result = response.getString("message");
                            Toast.makeText(AddingPhoto.this, "First Image added successfully.", Toast.LENGTH_SHORT).show();
                            addingVehicleImageApi2();
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
                        Toast.makeText(AddingPhoto.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
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
        return true;
    }
    private boolean addingVehicleImageApi2(){
        String url = "http://motorbazartoken.ghimiremilan.com.np/api/vehicleImage";
        final String mtoken = TokenHolder.userToken;
        progressDialog();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name",vehicleName);
            jsonObj.put("status",true);
            jsonObj.put("vehicleId",vehicleId);
            jsonObj.put("imageurl",bitmapToString(bitmap2));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {

                            String result = response.getString("message");
                            Toast.makeText(AddingPhoto.this, "Second Image added successfully.", Toast.LENGTH_SHORT).show();
                            addingVehicleImageApi3();
                            //Toast.makeText(AddingPhoto.this, result, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddingPhoto.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
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
        return true;
    }
    private boolean addingVehicleImageApi3(){
        String url = "http://motorbazartoken.ghimiremilan.com.np/api/vehicleImage";
        final String mtoken = TokenHolder.userToken;
        progressDialog();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name",vehicleName);
            jsonObj.put("status",true);
            jsonObj.put("vehicleId",vehicleId);
            jsonObj.put("imageurl",bitmapToString(bitmap3));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            String result = response.getString("message");
                            Toast.makeText(AddingPhoto.this, "Third Image added successfully.", Toast.LENGTH_SHORT).show();
                            addingVehicleImageApi4();
                            //Toast.makeText(AddingPhoto.this, result, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(AddingPhoto.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
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
        return true;
    }
    private boolean addingVehicleImageApi4(){
        String url = "http://motorbazartoken.ghimiremilan.com.np/api/vehicleImage";
        final String mtoken = TokenHolder.userToken;
        progressDialog();
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("name",vehicleName);
            jsonObj.put("status",true);
            jsonObj.put("vehicleId",vehicleId);
            jsonObj.put("imageurl",bitmapToString(bitmap4));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        try {
                            String result = response.getString("message");
                            Toast.makeText(AddingPhoto.this, "All vehicle image successfully added.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent (AddingPhoto.this, UserDashboard.class));
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
                        Toast.makeText(AddingPhoto.this, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
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
        return true;
    }
    private void progressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

}
