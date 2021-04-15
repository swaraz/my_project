package com.example.motorbazar.SplashScreenPackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorbazar.R;
import com.example.motorbazar.RegisterPackage.RegisterUser;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.User_Dashboard.UserDashboard;
import com.example.motorbazar.model.LogInModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    TextInputEditText username, password;
    CheckBox cbrememberMe;
    Button directLogin;
    TextView signup;
    ProgressDialog progressDialog;
    int mStatusCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        animationControl();
        initializeUI();
        login();
        //forgotPassword();
        onSignup();
    }

    public void animationControl() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(SplashScreen.this, R.layout.login_screen_layout);
                    ChangeBounds transition = new ChangeBounds();
                    transition.setDuration(500);
                    TransitionManager.beginDelayedTransition(constraintLayout, transition);
                    constraintSet.applyTo(constraintLayout);
                }
            }
        }, 2000);
    }

    public void initializeUI() {
        constraintLayout = findViewById(R.id.parent);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        cbrememberMe = findViewById(R.id.remember_me);
        directLogin = findViewById(R.id.loginBtn);
        //fbLogin = findViewById(R.id.loginFacebookBtn);
        signup = findViewById(R.id.signUp);
    }

    private void progressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void login() {
        directLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if(user.isEmpty() || pass.isEmpty()){
                    if (user.isEmpty()) {
                        username.setError("UserName Is Required");
                    } else {
                        password.setError("PassWord Is Required");
                    }
                }
                else {
                    loginApiConnection();
                    }
                }
            });
    }
    // method for calling login api
    public void loginApiConnection(){
        try {
            progressDialog();
            String url = "http://motorbazartoken.ghimiremilan.com.np/api/Auth/login";
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonObject = new JSONObject();
            //adding values to JsonObject
            jsonObject.put("username", username.getText().toString());
            jsonObject.put("password", password.getText().toString());
            JsonObjectRequest loginRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            try {
                                //Gson converts Json type to Java object type
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                LogInModel logInModel = gson.fromJson(String.valueOf(response), LogInModel.class);
                                if (!logInModel.getAccessToken().isEmpty()) {
                                    if (TokenHolder.setToken(logInModel.getAccessToken())) {
                                        Toast.makeText(SplashScreen.this, "Login successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SplashScreen.this, UserDashboard.class));
                                    }
                                }
                            } catch (Exception ex) {
                                Toast.makeText(SplashScreen.this, "User doesnot Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Toast.makeText(SplashScreen.this, "Invalid Username or Password.", Toast.LENGTH_SHORT).show();
                        }
                    }
            ){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    mStatusCode =response.statusCode;
                    return super.parseNetworkResponse(response);
                }
            };
            queue.add(loginRequest);
            //startActivity(new Intent(SplashScreen.this, UserDashboard.class));
        } catch (Exception ex) {
            Toast.makeText(SplashScreen.this, "Exception: " + ex, Toast.LENGTH_SHORT).show();
        }
    }

    /*public void forgotPassword() {
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SplashScreen.this, "forgot password clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    public void onSignup() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SplashScreen.this, RegisterUser.class));
                //Toast.makeText(SplashScreen.this, "signup clicked.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed()
    {

    }
}
