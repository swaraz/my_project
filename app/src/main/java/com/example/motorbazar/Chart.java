package com.example.motorbazar;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.motorbazar.Fragments.HomePackage.HomeTabPackage.AllVehicles;
import com.example.motorbazar.Fragments.HomePackage.HomeTabPackage.Bikes;
import com.example.motorbazar.Fragments.HomePackage.HomeTabPackage.Cars;
import com.example.motorbazar.model.ListedVehicle;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

public class Chart extends AppCompatActivity {

    PieChart pieChart;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_layout);
        pieChart = findViewById(R.id.chart);

        int bikes = Bikes.bikesList.size();
        int cars = Cars.carsList.size();



        if(bikes==0 && cars==0){
            Toast.makeText(this, "No vehicles Available", Toast.LENGTH_SHORT).show();
            pieChart.addPieSlice(new PieModel("No Vehicles",0, Color.parseColor("#F16F6B")));
        }
        else {
            Toast.makeText(this, "bikes: "+bikes+" cars: "+cars, Toast.LENGTH_SHORT).show();
            pieChart.addPieSlice(new PieModel("Bikes", bikes, Color.parseColor("#FFA726")));
            pieChart.addPieSlice(new PieModel("Cars", cars, Color.parseColor("#29B6F6")));
        }
        pieChart.startAnimation();
    }
}
