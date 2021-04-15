package com.example.motorbazar.Fragments.SearchPackage;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.motorbazar.Adapters.filteredVehiclesAdapter;
import com.example.motorbazar.Fragments.HomePackage.HomeTabPackage.AllVehicles;
import com.example.motorbazar.R;
import com.example.motorbazar.model.ListedVehicle;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    MaterialSearchBar searchBar;
    RecyclerView recyclerView;
    ArrayList<ListedVehicle> allUnsoldVehicles = new ArrayList<>();
    filteredVehiclesAdapter adapter;
    TextView noVehicleTxt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uiInitialize(view);
        allUnsoldVehicles = AllVehicles.unslodVehicleList;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new filteredVehiclesAdapter(getContext(),allUnsoldVehicles);
        recyclerView.setAdapter(adapter);

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if(buttonCode == MaterialSearchBar.BUTTON_NAVIGATION){

                }
                else if(buttonCode == MaterialSearchBar.BUTTON_BACK){
                    searchBar.disableSearch();
                }
            }
        });
    }
    private void filter(String text){
        ArrayList<ListedVehicle> filteredList = new ArrayList<>();
        for(int i=0; i<allUnsoldVehicles.size();i++){
            if(allUnsoldVehicles.get(i).getVehicleBrand().getName().toLowerCase()
                        .contains(text.toLowerCase())){
                filteredList.add(allUnsoldVehicles.get(i));
            }
        }
        adapter.filterList(filteredList);
    }

    public void uiInitialize(View view){
        searchBar = view.findViewById(R.id.searchBar);
        recyclerView =view.findViewById(R.id.list);
        noVehicleTxt = view.findViewById(R.id.noVehicle);
    }
}
