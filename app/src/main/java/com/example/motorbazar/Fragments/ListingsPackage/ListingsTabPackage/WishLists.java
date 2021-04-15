package com.example.motorbazar.Fragments.ListingsPackage.ListingsTabPackage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorbazar.Adapters.SellingsAdapter;
import com.example.motorbazar.Adapters.WishlistAdapter;
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.model.MyVehicles;
import com.example.motorbazar.model.WishlistModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishLists extends Fragment {
    final String mywishlistUrl = "http://motorbazartoken.ghimiremilan.com.np/api/wishlist";
    final String mtoken = TokenHolder.userToken;
    ArrayList<MyVehicles> myWishList = new ArrayList<MyVehicles>();
    RecyclerView wishlist_recyclerView;
    ProgressDialog progressDialog;
    ArrayList<WishlistModel> wishlistVehicles = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wishlists_viewpager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wishlist_recyclerView = view.findViewById(R.id.wish_list);
        wishlist_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //apiConnection();
        callingWishlistAPI();

    }

    private void callingWishlistAPI(){
        progressDialog();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mywishlistUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        wishlistVehicles.clear();
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                int vehicleid = obj.getInt("vehicleId");
                                int wishlistid = obj.getInt("id");
                                wishlistVehicles.add(new WishlistModel(vehicleid,wishlistid));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        wishlist_recyclerView.setAdapter(new WishlistAdapter(getContext(),wishlistVehicles));
                    }
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
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization","Bearer " + mtoken);
                return params;
            }
        };
        queue.add(jsonArrayRequest);
    }
    private void progressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading vehicles");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

}
