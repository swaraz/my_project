package com.example.motorbazar.Fragments.HomePackage.HomeTabPackage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.example.motorbazar.Adapters.AllVehiclesAdapter;
import com.example.motorbazar.Adapters.WishlistAdapter;
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.model.BookingsModel;
import com.example.motorbazar.model.ListedVehicle;
import com.example.motorbazar.model.WishlistModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllVehicles extends Fragment {
    TextView visibility;
    private static final String listedVehicleURL = "http://motorbazartoken.ghimiremilan.com.np/api/vehicle";
    final String mtoken = TokenHolder.userToken;
    public static ArrayList<ListedVehicle> unslodVehicleList= new ArrayList<>();
    RecyclerView allVehiclesList;
    ProgressDialog progressDialog;
    public static ArrayList<WishlistModel> userWishList = new ArrayList<>();
    public static ArrayList<BookingsModel> userBooking = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_vehicles_viewpager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);
        allVehiclesList=(RecyclerView) view.findViewById(R.id.All_vehicle_list);
        allVehiclesList.setLayoutManager(new LinearLayoutManager(getContext()));
        visibility = view.findViewById(R.id.visibility);
        progressDialog();
        callingwishAPI();
        callingbookAPI();
        //calling Api
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                listedVehicleURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        unslodVehicleList.clear();
                        //Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_LONG).show();

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson =gsonBuilder.create();
                        ArrayList<ListedVehicle> vehicleList = new Gson().fromJson(response,new TypeToken<List<ListedVehicle>>(){}.getType());
                        for(int i = 0; i<vehicleList.size();i++){
                            if(!(vehicleList.get(i).getIsSold())){
                                unslodVehicleList.add(vehicleList.get(i));
                            }
                        }
                        if(unslodVehicleList.size()<1){
                            visibility.setVisibility(View.VISIBLE);
                        }
                        else{
                        allVehiclesList.setAdapter(new AllVehiclesAdapter(getContext(),unslodVehicleList));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        //Toast.makeText(getContext(), "Error: "+error, Toast.LENGTH_LONG).show();
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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading vehicles");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void callingwishAPI(){
        String mywishlistUrl = "http://motorbazartoken.ghimiremilan.com.np/api/wishlist";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                mywishlistUrl,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        userWishList.clear();
                        for(int i=0;i<response.length();i++){
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                int vehicleid = obj.getInt("vehicleId");
                                int wishlistid = obj.getInt("id");
                                userWishList.add(new WishlistModel(vehicleid,wishlistid));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    private void callingbookAPI(){
        String bookingURL = "http://motorbazartoken.ghimiremilan.com.np/api/booking";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                bookingURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        userBooking.clear();
                        for(int i=0;i<response.length();i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                int vehicleId = obj.getInt("vehicleId");
                                int bookingId = obj.getInt("id");
                                userBooking.add(new BookingsModel(vehicleId, bookingId));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
        queue.add(jsonArrayRequest);
    }
}
