package com.example.motorbazar.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.motorbazar.EditVehicleDetails;
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.User_Dashboard.UserDashboard;
import com.example.motorbazar.model.ListedVehicle;
import com.example.motorbazar.model.MyVehicles;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellingsAdapter extends RecyclerView.Adapter<SellingsAdapter.SellingsViewHolder> {
    String mtoken = TokenHolder.userToken;
    Context context;
    ArrayList<ListedVehicle> myVehiclesList = new ArrayList<ListedVehicle>();

    String name,regNo, longitude, latitude, description;
    double mileage, odometerReading, price;
    int categoryId, brandId, engineId, modelId, condition;

    public SellingsAdapter(Context context, ArrayList<ListedVehicle> myVehiclesList){
        this.context = context;
        this.myVehiclesList = myVehiclesList;
    }

    @NonNull
    @Override
    public SellingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.sellings_display_design, parent,false);
        return new SellingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellingsViewHolder sellingsViewHolder, int position) {
        final int vehicleId = myVehiclesList.get(position).getId();
        String model = myVehiclesList.get(position).getVehicleModel().getName();
        String price = myVehiclesList.get(position).getPrice().toString();
        if(myVehiclesList.get(position).getVehicleImages().size()>0){
            String imageLink = myVehiclesList.get(position).getVehicleImages().get(0).getImageUrl();
            Glide.with(sellingsViewHolder.image.getContext()).load(imageLink).into(sellingsViewHolder.image);
        }
        sellingsViewHolder.modelName.setText(model);
        sellingsViewHolder.price.setText(price);

        sellingsViewHolder.soldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Mark vehicle as sold?");
                builder.setMessage("Vehicle wont be displayed to other user.\nDo you want to continue?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        soldVehicle(vehicleId);
                        Toast.makeText(context, "Successfully marked as sold", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, UserDashboard.class));
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });

        sellingsViewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Delete vehicleId: "+vehicleId, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Vehicle");
                builder.setMessage("Do you want to remove the vehicle from listings?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteVehicle(vehicleId);
                        Toast.makeText(context, "Successfully removed", Toast.LENGTH_SHORT).show();
                        context.startActivity(new Intent(context, UserDashboard.class));
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.create().show();
            }
        });
        sellingsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EditVehicleDetails.class);
                intent.putExtra("vehicleId", vehicleId);
                context.startActivity(intent);
                //context.startActivity(new Intent(context, EditVehicleDetails.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return myVehiclesList.size();
    }

    public class SellingsViewHolder extends RecyclerView.ViewHolder{
        TextView price, modelName;
        ImageView image;
        Button soldBtn, deleteBtn;
        public SellingsViewHolder(@NonNull View itemView) {
            super(itemView);
            price = itemView.findViewById(R.id.sellings_Price);
            modelName = itemView.findViewById(R.id.sellings_modelName);
            image = itemView.findViewById(R.id.sellings_image);
            soldBtn = itemView.findViewById(R.id.soldBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);

        }
    }
    public void deleteVehicle(int vehicleId){
        String url = "http://motorbazartoken.ghimiremilan.com.np/api/vehicle/"+vehicleId;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Can't delete the vehicle", Toast.LENGTH_SHORT).show();
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
    private void soldVehicle(int vehicleId){
        for(int i =0; i<myVehiclesList.size(); i++){
            if(myVehiclesList.get(i).getId().equals(vehicleId)){
                name = myVehiclesList.get(i).getName();
                mileage = myVehiclesList.get(i).getMileage();
                regNo = myVehiclesList.get(i).getRegistrationNumber();
                odometerReading = myVehiclesList.get(i).getOdometerReading();
                price = myVehiclesList.get(i).getPrice();
                condition = myVehiclesList.get(i).getCondition();
                longitude = myVehiclesList.get(i).getLongitude();
                latitude = myVehiclesList.get(i).getLatitude();
                description = myVehiclesList.get(i).getDescription();
                categoryId = myVehiclesList.get(i).getVehicleCategoryId();
                brandId = myVehiclesList.get(i).getVehicleBrandId();
                modelId = myVehiclesList.get(i).getVehicleModelId();
                engineId = myVehiclesList.get(i).getVehicleEngineId();
            }
        }
        String url = "http://motorbazartoken.ghimiremilan.com.np/api/vehicle";
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject obj = new JSONObject();
        try {
            obj.put("Id",vehicleId);
            obj.put("name",name);
            obj.put("status",true);
            obj.put("mileage",mileage);
            obj.put("RegistrationNumber",regNo);
            obj.put("OdometerReading",odometerReading);
            obj.put("Price",price);
            obj.put("Condition",condition);
            obj.put("Longitude",longitude);
            obj.put("Latitude",latitude);
            obj.put("Description",description);
            obj.put("isSold",true);
            obj.put("VehicleCategoryId",categoryId);
            obj.put("VehicleBrandId",brandId);
            obj.put("VehicleModelId",modelId);
            obj.put("VehicleEngineId",engineId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error: "+error.toString(), Toast.LENGTH_SHORT).show();
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
}
