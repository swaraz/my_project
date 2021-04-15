package com.example.motorbazar.Sort;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motorbazar.Adapters.AllVehiclesAdapter;
import com.example.motorbazar.Adapters.SortedVehiclesAdapter;
import com.example.motorbazar.Fragments.HomePackage.HomeTabPackage.AllVehicles;
import com.example.motorbazar.R;
import com.example.motorbazar.model.ListedVehicle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortItems extends AppCompatActivity {
    Spinner sortingItem;
    RecyclerView sortView;
    ArrayList<ListedVehicle> sortedVehicle = AllVehicles.unslodVehicleList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sort_layout);
        sortingItem = findViewById(R.id.sort_item);

        sortView = findViewById(R.id.sortView);
        sortView.setLayoutManager(new LinearLayoutManager(this));

        spinnerOnSelection();

    }

    private void spinnerOnSelection(){
        sortingItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int sortId = (int) adapterView.getItemIdAtPosition(i);
                switch(sortId){
                    case 0:
                        Collections.sort(sortedVehicle, new priceAscending());
                        sortView.setAdapter(new SortedVehiclesAdapter(getApplicationContext(), sortedVehicle));
                        break;
                    case 1:
                        Collections.sort(sortedVehicle, new priceDescending());
                        sortView.setAdapter(new SortedVehiclesAdapter(getApplicationContext(), sortedVehicle));
                        break;
                    case 2:
                        Collections.sort(sortedVehicle, new ratingComparator());
                        sortView.setAdapter(new SortedVehiclesAdapter(getApplicationContext(), sortedVehicle));
                        break;
                    case 3:
                        Collections.sort(sortedVehicle, new ratingAscending());
                        sortView.setAdapter(new SortedVehiclesAdapter(getApplicationContext(), sortedVehicle));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    /*private ArrayList<ListedVehicle> sortLowToHigh(){
        int size = sortedVehicle.size();
        for (int i = 0; i<size-1; i++){
            for(int j=0; j<size-i-1;j++){
                if (sortedVehicle.get(j).getPrice() > sortedVehicle.get(j+1).getPrice()){
                    //swap
                    Collections.swap(sortedVehicle,j,j+1);
                }
            }
        }
        return sortedVehicle;
    }*/
    private class priceAscending implements Comparator<ListedVehicle>{
        @Override
        public int compare(ListedVehicle v1, ListedVehicle v2) {
            return v1.getPrice().compareTo(v2.getPrice());
        }
    }
    private class priceDescending implements Comparator<ListedVehicle>{
        @Override
        public int compare(ListedVehicle v1, ListedVehicle v2) {
            return v2.getPrice().compareTo(v1.getPrice());
        }
    }
    private class ratingComparator implements Comparator<ListedVehicle>{
        @Override
        public int compare(ListedVehicle v1, ListedVehicle v2) {
            return v2.getCondition().compareTo(v1.getCondition());
        }
    }
    private class ratingAscending implements Comparator<ListedVehicle>{
        @Override
        public int compare(ListedVehicle v1, ListedVehicle v2) {
            return v1.getCondition().compareTo(v2.getCondition());
        }
    }
}
