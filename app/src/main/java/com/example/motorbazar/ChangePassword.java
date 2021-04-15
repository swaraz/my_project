package com.example.motorbazar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
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
import com.example.motorbazar.SplashScreenPackage.SplashScreen;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    EditText username, currentPassword,newPassword, confirmPassword;
    Button changePasswordBtn;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_layout);

        initializeUI();
        changePasswordBtnClicked();
    }
    private void initializeUI(){
        username = findViewById(R.id.username);
        currentPassword = findViewById(R.id.current_password);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirmNewPassword);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);
    }
    private void validation(){
        awesomeValidation =new AwesomeValidation(ValidationStyle.BASIC);
        awesomeValidation.addValidation(this,R.id.username,
                RegexTemplate.NOT_EMPTY,R.string.enter_username);
        awesomeValidation.addValidation(this,R.id.current_password,
                RegexTemplate.NOT_EMPTY,R.string.enter_password);
        awesomeValidation.addValidation(this,R.id.new_password,
                RegexTemplate.NOT_EMPTY,R.string.enter_password);
        awesomeValidation.addValidation(this,R.id.confirmNewPassword,
                R.id.new_password,R.string.invalid_password);
    }
    private void changePasswordBtnClicked(){
        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
                if(awesomeValidation.validate()){
                    if(username.getText().toString().length()<8){
                        username.setError("Must be minimum of 8 characters");
                    }
                    else if(currentPassword.getText().toString().equals(newPassword.getText().toString())){
                        newPassword.setError("Must not be same as old password");
                    }
                    else if (newPassword.getText().toString().length()<6){
                        newPassword.setError("Must have minimum of 6 characters");
                    }
                    else{
                        //Toast.makeText(ChangePassword.this, "valication complete", Toast.LENGTH_SHORT).show();
                        changePasswordAPI();

                    }
                }
            }
        });
    }
    private void changePasswordAPI(){
        String changePwdURL = "http://motorbazartoken.ghimiremilan.com.np/api/Auth/changepassword";
        final String userToken = TokenHolder.userToken;
        RequestQueue queue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username",username.getText().toString());
            jsonObject.put("oldpassword",currentPassword.getText().toString());
            jsonObject.put("password",newPassword.getText().toString());
            jsonObject.put("confirmpassword",confirmPassword.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                changePwdURL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(ChangePassword.this, ""+response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangePassword.this, "Username and Password doesnot match", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + userToken);
                return params;
            }
        };
        queue.add(request);
    }
}
