package com.example.motorbazar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorbazar.Adapters.NotificationAdapter;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.model.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

    public class Notifications extends AppCompatActivity {
        TextView visibility;
        String bookingURL = "http://motorbazartoken.ghimiremilan.com.np/api/notification";
        String mtoken = TokenHolder.userToken;
        ProgressDialog progressDialog;
        RecyclerView notifications_recycler_view;
        ArrayList<Notification> notificationList;
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.notification_layout);

            notifications_recycler_view=(RecyclerView) findViewById(R.id.notification_list);
            notifications_recycler_view.setLayoutManager(new LinearLayoutManager(this));
            visibility = findViewById(R.id.visibility);
            callingAPI();
//            if (notificationList.size()==0){
//                notifications_recycler_view.setVisibility(View.GONE);
//
//                visibility.setVisibility(View.VISIBLE);
//            }
        }
        private void callingAPI(){
            progressDialog();
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(
                    Request.Method.GET,
                    bookingURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            Gson gson = gsonBuilder.create();
                            notificationList =new Gson().fromJson(response,new TypeToken<List<Notification>>(){}.getType());
                            if(notificationList.size()<1){
                                visibility.setVisibility(View.VISIBLE);
                            }
                            else{
                            notifications_recycler_view.setAdapter(new NotificationAdapter(getApplicationContext(),notificationList));
                        }}
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization","Bearer " + mtoken);
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
