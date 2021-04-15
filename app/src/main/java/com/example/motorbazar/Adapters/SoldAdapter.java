package com.example.motorbazar.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.motorbazar.Fragments.ListingsPackage.ListingsTabPackage.SellingLists;
import com.example.motorbazar.R;
import com.example.motorbazar.model.ListedVehicle;

import java.util.ArrayList;

public class SoldAdapter extends RecyclerView.Adapter<SoldAdapter.SoldViewHolder> {

    Context context;
    ArrayList<ListedVehicle> mySoldList = new ArrayList<ListedVehicle>();

    public SoldAdapter(Context context, ArrayList<ListedVehicle> mySoldList){
        this.context = context;
        this.mySoldList = mySoldList;
    }

    @NonNull
    @Override
    public SoldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sold_display_design,parent,false);
        return new SoldViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SoldViewHolder soldViewHolder, int position) {
        //String price = soldItems[position];
        //holder.price.setText(price);
        String model = mySoldList.get(position).getVehicleModel().getName();
        String price = mySoldList.get(position).getPrice().toString();
        if(mySoldList.get(position).getVehicleImages().size()>0){
            String imageLink = mySoldList.get(position).getVehicleImages().get(0).getImageUrl();
            Glide.with(soldViewHolder.vehicle_image.getContext()).load(imageLink).into(soldViewHolder.vehicle_image);
        }

        soldViewHolder.model_name.setText(model);
        soldViewHolder.price.setText(price);
    }

    @Override
    public int getItemCount() {
        return mySoldList.size();
    }

    public class SoldViewHolder extends RecyclerView.ViewHolder{
        TextView price;
        ImageView vehicle_image;
        TextView model_name;
        public SoldViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.soldVehiclePrice);
            model_name = itemView.findViewById(R.id.soldVehicleName);
            vehicle_image = itemView.findViewById(R.id.soldVehicleimage);
        }
    }
}
