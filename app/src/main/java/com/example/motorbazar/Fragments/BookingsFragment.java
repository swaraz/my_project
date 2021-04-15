package com.example.motorbazar.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.motorbazar.Adapters.BookingsAdapter;
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.model.BookingsModel;
import com.example.motorbazar.model.WishlistModel;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingsFragment extends Fragment {
    TextView visibility;
    String bookingURL = "http://motorbazartoken.ghimiremilan.com.np/api/booking";
    String mtoken = TokenHolder.userToken;
    ProgressDialog progressDialog;
    RecyclerView bookings_recycler_view;
    private Toolbar bookings_toolbar;
    ArrayList<BookingsModel> bookings = new ArrayList<BookingsModel>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bookings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uiInitialize(view);

        //setting up tool bar
        bookings_toolbar.setTitle("Bookings");
        ((AppCompatActivity)getActivity()).setSupportActionBar(bookings_toolbar);

        bookings_recycler_view = view.findViewById(R.id.bookings_list);
        bookings_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        visibility = view.findViewById(R.id.booking_visibility);

        callingAPI();
//        String [] price = {"1000000","5000000","7000000","500000"};
//        bookings_recycler_view.setAdapter(new BookingsAdapter(price));
//        app:layout_scrollFlags="scroll|enterAlways"
    }
    public void uiInitialize(View view){
        bookings_toolbar = view.findViewById(R.id.bookings_toolbar);
    }

    private void callingAPI(){
        progressDialog();
        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                bookingURL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        for(int i=0;i<response.length();i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                int vehicleId = obj.getInt("vehicleId");
                                int bookingId = obj.getInt("id");
                                bookings.add(new BookingsModel(vehicleId, bookingId));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(bookings.size()>0){
                        bookings_recycler_view.setAdapter(new BookingsAdapter(getContext(),bookings));
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
    private void progressDialog() {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading bookings done");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

}
