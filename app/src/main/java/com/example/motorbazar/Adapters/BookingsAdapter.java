package com.example.motorbazar.Adapters;

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
import com.example.motorbazar.model.BookingsModel;
import com.example.motorbazar.model.ListedVehicle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingsViewHolder> {
    String mtoken = TokenHolder.userToken;
    Context context;
    ArrayList<BookingsModel> bookings = new ArrayList<>();

    public BookingsAdapter(Context context, ArrayList<BookingsModel> bookings){
        this.context = context;
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.wishlist_display_design, parent, false);
        return new BookingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingsViewHolder holder, int position) {
        final int vehicleId = bookings.get(position).getVehicleId();
        final int bookingId = bookings.get(position).getBookingId();

        callingAPI(vehicleId, bookingId, holder.modelName, holder.price,holder.image);

        holder.cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Vehicle Id: "+vehicleId+"cancel clicked" , Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Booking");
                builder.setMessage("Do you want delete booking for this vehicle?");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteBookingAPI(bookingId);
                        //Toast.makeText(context, "Booking cancelled successfully.", Toast.LENGTH_SHORT).show();
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
                intent.putExtra("vehicleId", vehicleId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class BookingsViewHolder extends RecyclerView.ViewHolder{
        TextView price, modelName, cancelBooking;
        ImageView image;
        public BookingsViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.txtPrice);
            modelName = itemView.findViewById(R.id.txtName);
            image = itemView.findViewById(R.id.image);
            cancelBooking = itemView.findViewById(R.id.cancelBooking);
            cancelBooking.setVisibility(View.VISIBLE);
        }
    }
    private void callingAPI(final int vehicleID, final int bookingID, final TextView modelName, final TextView price,
                            final ImageView image){
        String selectedVehicleUrl = "http://motorbazartoken.ghimiremilan.com.np/api/vehicle/" + vehicleID;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                selectedVehicleUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context, "esponse: "+response, Toast.LENGTH_SHORT).show();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        ListedVehicle bookedItem = new Gson().fromJson(response, ListedVehicle.class);
                        modelName.setText(bookedItem.getVehicleModel().getName());
                        price.setText(bookedItem.getPrice().toString());

                        if(bookedItem.getVehicleImages().size()>0){
                            String imageLink = bookedItem.getVehicleImages().get(0).getImageUrl();
                            Glide.with(image.getContext()).load(imageLink).into(image);
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
                params.put("Authorization", "Bearer " + mtoken);
                return params;
            }
        };
        queue.add(request);
    }
    public void deleteBookingAPI(int bookingId){
        String url = "http://motorbazartoken.ghimiremilan.com.np/api/booking/"+bookingId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Booking cancelled successfully.", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
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
