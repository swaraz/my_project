package com.example.motorbazar.RegisterPackage;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.motorbazar.R;
import com.example.motorbazar.SplashScreenPackage.SplashScreen;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.google.android.material.textfield.TextInputEditText;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



public class RegisterUser extends AppCompatActivity {
    String registerURL = "http://motorbazartoken.ghimiremilan.com.np/api/Auth/register";
    AwesomeValidation awesomeValidation;
    TextInputEditText editTextFirstname, editTextLastName, editTextEmail,editTextusername,editTextpassword,
             editTextConfirmPassword, editTextContact, editTextCountry, editTextCity, editTextProvience;
    Button registerBtn;

    Bitmap bitmap;
    ProgressDialog progressDialog;
    Uri selectedImagePath;
    CircularImageView userImage;
    private static final int PERMISSION_REQUEST = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                             .getColor(R.color.actionBarColor)));

        uiInitialize();

        handlePermission();
        imageClicked();
        registerBtnClick();
    }

    private void uiInitialize(){
        editTextFirstname =     findViewById(R.id.firstname);
        editTextLastName =      findViewById(R.id.lastname);
        editTextEmail =         findViewById(R.id.email);
        editTextusername =      findViewById(R.id.username_register);
        editTextpassword =      findViewById(R.id.password_register);
        editTextConfirmPassword = findViewById(R.id.confirmPassword_register);
        editTextContact =       findViewById(R.id.contact);
        editTextCountry =       findViewById(R.id.country);
        editTextCity =          findViewById(R.id.city);
        editTextProvience =     findViewById(R.id.provience);
        registerBtn =           findViewById(R.id.registerbtn);
        userImage =             findViewById(R.id.profileImage);
    }
    public void validation(){
        awesomeValidation =new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.firstname,
                RegexTemplate.NOT_EMPTY,R.string.enter_name);
        awesomeValidation.addValidation(this,R.id.lastname,
                RegexTemplate.NOT_EMPTY,R.string.enter_name);
        awesomeValidation.addValidation(this,R.id.email,
                Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        awesomeValidation.addValidation(this,R.id.username_register,
                RegexTemplate.NOT_EMPTY,R.string.enter_username);
        awesomeValidation.addValidation(this,R.id.password_register,
                RegexTemplate.NOT_EMPTY,R.string.enter_password);
        awesomeValidation.addValidation(this,R.id.confirmPassword_register,
                R.id.password_register,R.string.invalid_password);
        awesomeValidation.addValidation(this,R.id.contact,
                RegexTemplate.NOT_EMPTY,R.string.enter_contact);
        awesomeValidation.addValidation(this,R.id.country,
                RegexTemplate.NOT_EMPTY,R.string.enter_country);
        awesomeValidation.addValidation(this,R.id.city,
                RegexTemplate.NOT_EMPTY,R.string.enter_city);
        awesomeValidation.addValidation(this,R.id.provience,
                RegexTemplate.NOT_EMPTY,R.string.enter_provience);
    }
    public void registerBtnClick(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
                if(awesomeValidation.validate()){
                    String username = editTextusername.getText().toString();
                    if(username.length()<8){
                        editTextusername.setError("must have atleat 8 characters");
                    }
                    else if(editTextpassword.getText().toString().length()<8){

                        editTextpassword.setError("must have atleat 8 characters");
                    }
                    else if(editTextContact.getText().toString().length()<10){
                        editTextContact.setError("Contact should be 10 digits");
                    }
                    else {
                        //Toast.makeText(RegisterUser.this, "success", Toast.LENGTH_SHORT).show();
                        registerApiConnection();
                    }
                }
            }
        });
    }
    private void registerApiConnection(){
        progressDialog();
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(
                Request.Method.POST,
                registerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(RegisterUser.this, "Successfully registered", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(RegisterUser.this, json, Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", editTextusername.getText().toString());
                params.put("password", editTextpassword.getText().toString());
                params.put("email", editTextEmail.getText().toString());
                params.put("country", editTextCountry.getText().toString());
                params.put("provience", editTextProvience.getText().toString());
                params.put("city", editTextCity.getText().toString());
                params.put("phoneNumber", editTextContact.getText().toString());
                params.put("firstname", editTextFirstname.getText().toString());
                params.put("lastname", editTextLastName.getText().toString());
                params.put("ProfilePicture", bitmapToString(bitmap));
                return params;
            }
        };
        queue.add(request);
    }

    public void handlePermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST);
        }
    }
    private void imageClicked(){
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }
    public void openGallery(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {
            selectedImagePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImagePath);
                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String bitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }
    private void progressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to cancel register process?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(RegisterUser.this, SplashScreen.class));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle("Attention!!");
        alertDialog.show();
    }


}
