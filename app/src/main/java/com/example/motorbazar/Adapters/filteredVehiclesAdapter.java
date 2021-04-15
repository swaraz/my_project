package com.example.motorbazar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.motorbazar.R;
import com.example.motorbazar.VehicleDetails;
import com.example.motorbazar.model.ListedVehicle;

import java.util.ArrayList;

public class filteredVehiclesAdapter extends RecyclerView.Adapter<filteredVehiclesAdapter.VehicleViewHolder> {


    private Context context;
    public  ArrayList<ListedVehicle> vehicleList;


    public filteredVehiclesAdapter(Context context, ArrayList<ListedVehicle> vehicleList) {
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

        vehicleViewHolder.price.setText(price);
        //Glide.with(vehicleViewHolder.vehicle_image.getContext()).load(image).into(vehicleViewHolder.vehicle_image);

        vehicleViewHolder.addToWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "wish vehicleId: "+id, Toast.LENGTH_SHORT).show();
            }
        });

        vehicleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "vehicleId: "+id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), VehicleDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        ImageView vehicle_image, addToWishlist,removeWishlist;
        TextView price;
        RatingBar rating;
        public VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.display_price);
            vehicle_image = itemView.findViewById(R.id.vehicle_image);
            addToWishlist = itemView.findViewById(R.id.addToWishlist);
            removeWishlist = itemView.findViewById(R.id.removeFromWishList);
            rating = itemView.findViewById(R.id.vehicle_rating);
        }
    }
    public void filterList(ArrayList<ListedVehicle> filteredlist) {
        this.vehicleList = filteredlist;
        notifyDataSetChanged();
    }

}
