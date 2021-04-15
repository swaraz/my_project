package com.example.motorbazar.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.User_Dashboard.UserDashboard;
import com.example.motorbazar.VehicleDetails;
import com.example.motorbazar.model.ListedVehicle;
import com.example.motorbazar.model.MyVehicles;
import com.example.motorbazar.model.WishlistModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {
    String mtoken = TokenHolder.userToken;
    Context context;
    ArrayList<WishlistModel> wishlists = new ArrayList<>();



    public WishlistAdapter(Context context, ArrayList<WishlistModel> wishlists) {
        this.context = context;
        this.wishlists = wishlists;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.wishlist_display_design, parent, false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final WishlistViewHolder holder, final int position) {
        final int vehicleid = wishlists.get(position).getVehicleId();
        final int wishlistId = wishlists.get(position).getWishlistId();
        final String id = String.valueOf(vehicleid);
        System.out.println("------vehicleId------" + vehicleid);

        callingAPI(id, holder.model_name, holder.price, holder.image, position,vehicleid,wishlistId);

        holder.removeWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "vehicleId: "+vehicleid+"wish canceL", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove from wishlist");
                builder.setMessage("Do you want to remove from your Wishlist?");
                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeFromWishList(wishlistId);
                        Toast.makeText(context, "Removed successful", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, UserDashboard.class));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VehicleDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("vehicleId", vehicleid);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return wishlists.size();
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder {
        TextView price, model_name;
        ImageView image;
        TextView removeWishlist;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.txtPrice);
            model_name = itemView.findViewById(R.id.txtName);
            image = itemView.findViewById(R.id.image);
            removeWishlist = itemView.findViewById(R.id.removeFromWishlist);
            removeWishlist.setVisibility(View.VISIBLE);
        }
    }

    private void callingAPI(final String vehicleID, final TextView model_name,
                            final TextView price, final ImageView image,
                            final int position, final int vehicleId,final int wishlistId) {
        String selectedVehicleURL = "http://motorbazartoken.ghimiremilan.com.np/api/vehicle/" + vehicleID;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                selectedVehicleURL,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context, "response wla: "+response, Toast.LENGTH_LONG).show();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        ListedVehicle wishItem = new Gson().fromJson(response, ListedVehicle.class);
                        model_name.setText(wishItem.getName());
                        price.setText(wishItem.getPrice().toString());

                        if(wishItem.getVehicleImages().size()>0){
                            String imageLink = wishItem.getVehicleImages().get(0).getImageUrl();
                            Glide.with(image.getContext()).load(imageLink).into(image);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "error: " + error, Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer " + mtoken);
                return params;
            }
        };
        queue.add(request);
    }
    public void removeFromWishList(int wishlistId){
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
