package com.example.ju.finedust;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
// Add import statements for the new library.
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

public class SidoName extends AppCompatActivity {


    private RecyclerView recyclerViewSidoName;
    private String apiKey = "AIzaSyBdI4hf8XJHK7U5oUzMAOkhsmQXSZMTEro";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fine_dust_by_sido_name);
        init();

    }
    public void init(){
        String[] SidoNames = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"};
        recyclerViewSidoName = findViewById(R.id.RecyclerViewSidoName);
        AdapterSidoName adapterSidoName = new AdapterSidoName(SidoNames,this);
        recyclerViewSidoName.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSidoName.setLayoutManager(new GridLayoutManager(this,3));
        recyclerViewSidoName.setAdapter(adapterSidoName);
        // Initialize Places.
        Places.initialize(getApplicationContext(), apiKey);

// Create a new Places client instance.
        final PlacesClient placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

// Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
// Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                LatLng l = place.getLatLng();
                Log.i("SidoName_autocomplete", "Place: " + place.getName() + ", " + place.getId()+"위도"+place.getLatLng().latitude+"경도"+place.getLatLng().longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("SidoName_autocomplete", "An error occurred: " + status);
            }
        });
    }
}
