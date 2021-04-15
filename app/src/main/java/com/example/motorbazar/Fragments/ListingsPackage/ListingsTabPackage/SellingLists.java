package com.example.motorbazar.Fragments.ListingsPackage.ListingsTabPackage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.model.ListedVehicle;
import com.example.motorbazar.model.MyVehicles;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellingLists extends Fragment {
    TextView visibility;
    final String myVehiclesUrl = "http://motorbazartoken.ghimiremilan.com.np/api/vehicle/MyVehicles";
    final String mtoken = TokenHolder.userToken;

    RecyclerView sellings_recyclerView;
    ProgressDialog progressDialog;
    public static ArrayList<ListedVehicle> myVehiclesList = new ArrayList<>();
    public ArrayList<ListedVehicle> myUnsoldList= new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sellings_viewpager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sellings_recyclerView = view.findViewById(R.id.sellings_list);
        sellings_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        visibility = view.findViewById(R.id.sellings_visibility);

        //String [] price = {"1000000","5000000","7000000","500000"};
        //sellings_recyclerView.setAdapter(new SellingsAdapter(price));
        apiConnection();
    }
    private void apiConnection(){
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
                        myUnsoldList.clear();
                        //Toast.makeText(getContext(), "response: "+response, Toast.LENGTH_LONG).show();

                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        myVehiclesList = new Gson().fromJson(response,new TypeToken<List<ListedVehicle>>(){}.getType());
                        for(int i = 0; i<myVehiclesList.size();i++){
                            if(!(myVehiclesList.get(i).getIsSold())){
                                myUnsoldList.add(myVehiclesList.get(i));
                            }
                        }
                        if(myUnsoldList.size()>0){
                            sellings_recyclerView.setAdapter(new SellingsAdapter(getContext(),myUnsoldList));
                        }
                        else {
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
