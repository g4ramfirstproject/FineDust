package com.g4ram.ju.finedust;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.g4ram.ju.finedust.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    String apiKeyGooglePlaces;
    FindMoniteringStation mfindMoniteringStation;
    Context contextThis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        contextThis = this;
        init();

    }
    void init(){

        ImageButton a = findViewById(R.id.places_autocomplete_search_button);
        a.setVisibility(View.INVISIBLE);
        apiKeyGooglePlaces = getString(R.string.api_key_googlemap);
        // Initialize Places.
        Places.initialize(getApplicationContext(), apiKeyGooglePlaces);

        // Create a new Places client instance.
        final PlacesClient placesClient = Places.createClient(contextThis);
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
                Intent resultIntent = new Intent();
                resultIntent.putExtra("Lat",place.getLatLng().latitude);
                resultIntent.putExtra("Lng",place.getLatLng().longitude);
                setResult(RESULT_OK,resultIntent);
                finish();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("SidoName_autocomplete", "An error occurred: " + status);
            }
        });
    }
}
