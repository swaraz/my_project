package com.example.motorbazar.Fragments.ListingsPackage.ListingsTabPackage;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorbazar.Adapters.SellingsAdapter;
import com.example.motorbazar.Adapters.SoldAdapter;
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.model.ListedVehicle;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SoldLists extends Fragment {
    TextView visibility;
    final String myVehiclesUrl = "http://motorbazartoken.ghimiremilan.com.np/api/vehicle/MyVehicles";
    String mtoken = TokenHolder.userToken;

    RecyclerView soldItems_view;
    ProgressDialog progressDialog;
    public ArrayList<ListedVehicle> mySoldList= new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sold_items_viewpager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        soldItems_view = view.findViewById(R.id.sold_list);
        soldItems_view.setLayoutManager(new LinearLayoutManager(getContext()));
        visibility = view.findViewById(R.id.visibility_sold);

        callAPI();
    }
    private void callAPI(){
        progressDialog();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(
                Request.Method.GET,
                myVehiclesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        mySoldList.clear();
                        //Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_LONG).show();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        ArrayList<ListedVehicle> myVehiclesList = new Gson().fromJson(response,new TypeToken<List<ListedVehicle>>(){}.getType());
                        for(int i = 0; i<myVehiclesList.size();i++){
                            if(myVehiclesList.get(i).getIsSold()){
                                mySoldList.add(myVehiclesList.get(i));
                            }
                        }
                        if (mySoldList.size()>0){
                            soldItems_view.setAdapter(new SoldAdapter(getContext(),mySoldList));
                        }
                        else{
                            visibility.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        //Toast.makeText(getContext(), "error: "+error, Toast.LENGTH_LONG).show();
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
}
