package com.example.motorbazar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class BookersDetails extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 0;
    TextView fullname, country, city, contact, email;
    ImageView userImage;
    Button callBtn, smsBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booker_detail_layout);
        handlePermission();
        initialize();

        String fullname_obtained = getIntent().getStringExtra("fullname");
        String country_obtained = getIntent().getStringExtra("country");
        final String city_obtained = getIntent().getStringExtra("city");
        String email_obtained = getIntent().getStringExtra("email");
        final String contact_obtained = getIntent().getStringExtra("contact");
        String imageLink_obtained = getIntent().getStringExtra("image");

        fullname.setText(fullname_obtained);
        country.setText(country_obtained);
        city.setText(city_obtained);
        contact.setText(contact_obtained);
        email.setText(email_obtained);
        Glide.with(BookersDetails.this).load(imageLink_obtained).into(userImage);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = contact_obtained;

                AlertDialog.Builder builder = new AlertDialog.Builder(BookersDetails.this);
                builder.setTitle("Make a Call");
                builder.setMessage("Do you want to call the user?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    //@RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(PhoneCallActivity.this, "ok clicked", Toast.LENGTH_SHORT).show();
                        String s = "tel:" + phone;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(s));

                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED){
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST);
                        }
                        startActivity(intent);
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

        smsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = contact_obtained;
                AlertDialog.Builder builder = new AlertDialog.Builder(BookersDetails.this);
                builder.setTitle("Send SMS");
                builder.setMessage("Do you want to send SMS to the user?");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(PhoneCallActivity.this, "ok clicked", Toast.LENGTH_SHORT).show();
                        String s = "tel:" + phone;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.putExtra("address",phone);
                        intent.putExtra("sms_body","This is my number");
                        intent.setType("vnd.android-dir/mms-sms");
                        startActivity(intent);
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

    }
    public void initialize(){
        fullname = findViewById(R.id.user_name);
        country = findViewById(R.id.user_country);
        city = findViewById(R.id.user_city);
        contact = findViewById(R.id.user_contact);
        email = findViewById(R.id.user_email);
        userImage = findViewById(R.id.image_user);
        callBtn = findViewById(R.id.call_user_btn);
        smsBtn = findViewById(R.id.sms_user_btn);
    }
    //permission for phone call
    public void handlePermission(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST);
        }
    }
}
