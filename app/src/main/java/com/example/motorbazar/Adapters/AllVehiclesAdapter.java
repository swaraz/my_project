package com.example.motorbazar.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.motorbazar.Fragments.HomePackage.HomeTabPackage.AllVehicles;
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.User_Dashboard.UserDashboard;
import com.example.motorbazar.VehicleDetails;
import com.example.motorbazar.model.ListedVehicle;
import com.example.motorbazar.model.VehicleImage;
import com.example.motorbazar.model.WishlistModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllVehiclesAdapter extends RecyclerView.Adapter<AllVehiclesAdapter.VehicleViewHolder> {

    String mtoken = TokenHolder.userToken;
    private Context context;
    public  ArrayList<ListedVehicle> vehicleList;

    public static ArrayList<WishlistModel> userWishList = AllVehicles.userWishList;

    public AllVehiclesAdapter(Context context, ArrayList<ListedVehicle> vehicleList) {
        this.context = context;
        this.vehicleList = vehicleList;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.vehicle_display_design, viewGroup, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder vehicleViewHolder, final int i) {
        //String price = vehicledata[i];
        final String price = (vehicleList.get(i).getPrice()).toString();
        final String modelName = vehicleList.get(i).getVehicleModel().getName();
        String brandName = vehicleList.get(i).getVehicleBrand().getName();
        final String ownername = vehicleList.get(i).getApplicationUser().getUserName();
        final String ownerProfile = vehicleList.get(i).getApplicationUser().getProfileImage();
        if(vehicleList.get(i).getVehicleImages().size()>0){
            String imageLink = vehicleList.get(i).getVehicleImages().get(0).getImageUrl();
            Glide.with(vehicleViewHolder.vehicle_image.getContext()).load(imageLink).into(vehicleViewHolder.vehicle_image);
        }
        //final String imageLink = vehicleList.get(i).getVehicleImages().get(0).getImageUrl();
        final int id = vehicleList.get(i).getId();

        int rating = vehicleList.get(i).getCondition();
        switch (rating) {
            case 1:
                vehicleViewHolder.rating.setRating(1.0f);
                break;
            case 2:
                vehicleViewHolder.rating.setRating(2.0f);
                break;
            case 3:
                vehicleViewHolder.rating.setRating(3.0f);
                break;
            case 4:
                vehicleViewHolder.rating.setRating(4.0f);
                break;
            case 5:
                vehicleViewHolder.rating.setRating(5.0f);
                break;
        }
        if(checkWishList(id)){
            vehicleViewHolder.deleteFromWishlist.setVisibility(View.VISIBLE);
        }else{
            vehicleViewHolder.addToWishlist.setVisibility(View.VISIBLE);
        }
        vehicleViewHolder.price.setText(price);

        /*vehicleViewHolder.addToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "wish vehicleId: "+id, Toast.LENGTH_SHORT).show();
            }
        });*/

        vehicleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),VehicleDetails.class);
                int vehicleId = vehicleList.get(i).getId();
                intent.putExtra("vehicleId", vehicleId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vehicleList.size();

    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {
        ImageView vehicle_image, addToWishlist,deleteFromWishlist;
        TextView price;
        RatingBar rating;
        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.display_price);
            vehicle_image = itemView.findViewById(R.id.vehicle_image);
            addToWishlist = (ImageView) itemView.findViewById(R.id.addToWishlist);
            deleteFromWishlist = (ImageView) itemView.findViewById(R.id.removeFromWishList);
            rating = itemView.findViewById(R.id.vehicle_rating);

        }
    }
    private boolean checkWishList(int vehicleId){
        for (int i=0; i<userWishList.size();i++){
            if(vehicleId == userWishList.get(i).getVehicleId()){
                return true;
            }
        }
        return false;
    }
    private void deleteFromWish(int wishlistId){
        String url = "http://motorbazartoken.ghimiremilan.com.np/api/wishlist/"+wishlistId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Successfully Removed", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Can't remove from wishlist", Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + mtoken);
                return params;
            }
        };
        queue.add(request);
    }
}
