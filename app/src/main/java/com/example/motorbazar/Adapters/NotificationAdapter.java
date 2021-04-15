package com.example.motorbazar.Adapters;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.motorbazar.BookersDetails;
import com.example.motorbazar.Notifications;
import com.example.motorbazar.R;
import com.example.motorbazar.TokenHolder.TokenHolder;
import com.example.motorbazar.User_Dashboard.UserDashboard;
import com.example.motorbazar.VehicleDetails;
import com.example.motorbazar.model.Notification;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationHolder> {
    Context context;
    ArrayList<Notification> notificationList = new ArrayList<>();

    public NotificationAdapter(Context context, ArrayList<Notification> notificationList){
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.notification_display_design, parent, false);
        return new NotificationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationHolder holder, int position) {
        String bookerId = notificationList.get(position).getApplicationUserId();
        int vehicleId = notificationList.get(position).getVehicleId();
        final int notificationId = notificationList.get(position).getId();
        String bookerFirstName = notificationList.get(position).getApplicationUser().getFirstName();
        String bookerLastName = notificationList.get(position).getApplicationUser().getLastName();
        final String fullname = bookerFirstName+" "+bookerLastName;
        final String country = notificationList.get(position).getApplicationUser().getCountry();
        final String email = notificationList.get(position).getApplicationUser().getEmail();
        final String city = notificationList.get(position).getApplicationUser().getCity();
        String provience = notificationList.get(position).getApplicationUser().getProvience();
        final String contact = notificationList.get(position).getApplicationUser().getPhoneNumber();
        final String imageLink = notificationList.get(position).getApplicationUser().getProfileImage();

        holder.bookerName.setText(fullname);
        if(notificationList.get(position).getApplicationUser().getProfileImage()!= null){
            //String imageLink = notificationList.get(position).getApplicationUser().getProfileImage();
            Glide.with(holder.bookerImage.getContext()).load(imageLink).into(holder.bookerImage);
        }
        holder.showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookersDetails.class);
                intent.putExtra("fullname",fullname);
                intent.putExtra("country",country);
                intent.putExtra("city",city);
                intent.putExtra("email",email);
                intent.putExtra("contact",contact);
                intent.putExtra("image",imageLink);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.deletenoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotification(notificationId);
                Toast.makeText(context, "Notification deleted successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, UserDashboard.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class NotificationHolder extends RecyclerView.ViewHolder{
        ImageView bookerImage;
        TextView bookerName, showDetails, deletenoti;
        public NotificationHolder(@NonNull View itemView) {
            super(itemView);
            bookerImage = itemView.findViewById(R.id.user_image);
            bookerName = itemView.findViewById(R.id.name);
            showDetails = itemView.findViewById(R.id.details);
            deletenoti = itemView.findViewById(R.id.deleteNotification);
            //cancelIcon = (ImageView) itemView.findViewById(R.id.cancelNotification);
        }
    }
    private void deleteNotification(int notificationId){
        String url = "http://motorbazartoken.ghimiremilan.com.np/api/notification/"+notificationId;
        final String mtoken = TokenHolder.userToken;
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

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


}
