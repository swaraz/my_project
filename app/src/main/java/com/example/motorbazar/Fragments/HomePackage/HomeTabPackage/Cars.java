package com.example.motorbazar.Fragments.HomePackage.HomeTabPackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorbazar.Adapters.AllVehiclesAdapter;
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.model.ListedVehicle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cars extends Fragment {
    TextView visibility;
    private static final String listedVehicleURL = "http://motorbazartoken.ghimiremilan.com.np/api/vehicle";
    final String mtoken = TokenHolder.userToken;
    RecyclerView cars_list;
    public static ArrayList<ListedVehicle> carsList= new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_vehicles_viewpager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cars_list=(RecyclerView) view.findViewById(R.id.All_vehicle_list);
        cars_list.setLayoutManager(new LinearLayoutManager(getContext()));
        visibility = view.findViewById(R.id.visibility_car);

        callAPI();

    }
    public void callAPI(){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                listedVehicleURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        carsList.clear();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson =gsonBuilder.create();
                        ArrayList<ListedVehicle> vehicleList =
                                new Gson().fromJson(response,new TypeToken<List<ListedVehicle>>(){}.getType());
                        for(int i=0; i<vehicleList.size();i++){
                            if (vehicleList.get(i).getVehicleCategoryId().toString().equals("2") && !(vehicleList.get(i).getIsSold())){
                                carsList.add(vehicleList.get(i));
                            }
                        }
                        if(carsList.size()>0) {
                            cars_list.setAdapter(new AllVehiclesAdapter(getContext(), carsList));
                        }
                        else{
                            visibility.setVisibility(View.VISIBLE);
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
        queue.add(request);
    }

}
