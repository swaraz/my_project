package com.example.motorbazar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.motorbazar.model.VehicleImage;

import java.util.ArrayList;

public class SliderViewpagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] images = {R.drawable.bike,R.drawable.car};
    ArrayList<VehicleImage>imageList = new ArrayList<>();
    public SliderViewpagerAdapter(Context context, ArrayList<VehicleImage>imageList) {
        this.context = context;
        this.imageList =  imageList;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.imageslider_custom_design,container,false);
        ImageView imageView = view.findViewById(R.id.imageSlider_view);

        if(!(imageList.get(position).getImageUrl().equals(""))){
            Glide.with(context).load(imageList.get(position).getImageUrl())
                               .into(imageView);
        }
//        imageView.setImageResource(images[position]);
        ViewPager viewPager = (ViewPager)container;
        viewPager.addView(view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "clicked: "+(position+1), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager)container;
        View view = (View)object;
        viewPager.removeView(view);
    }
}
