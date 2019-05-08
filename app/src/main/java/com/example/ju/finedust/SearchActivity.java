package com.example.ju.finedust;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ju.finedust.Item.AreaInfoRetrofit;
import com.example.ju.finedust.Item.StationDustreturns;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.List;

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
                mfindMoniteringStation = new FindMoniteringStation();
                final CurrentLocation currentLocation = new CurrentLocation(contextThis);

                CallbackFuncInterface callbackFuncInterface = new CallbackFuncInterface() {
                    @Override
                    public void onSuccess_getCityTmCoordinate(String[] strings) {
                        mfindMoniteringStation.getUserLocalMoniteringStation(currentLocation.mTmX,currentLocation.mTmY);
                    }

                    @Override
                    public void onSuccess_getCityDustInfo(List<AreaInfoRetrofit> areaInfoRetrofitList) {

                    }

                    @Override
                    public void onSuccess_getDusInfoFromMoniteringStation(StationDustreturns.list stationDustreturns) {
                        Log.i("FromMoniteringStation",stationDustreturns.getPm10Grade());
                    }
                };
                mfindMoniteringStation.setCallbackFuncInterface(callbackFuncInterface);
                currentLocation.setCallbackfunc(callbackFuncInterface);
                currentLocation.setMlongitude(place.getLatLng().longitude);
                currentLocation.setMlatitude(place.getLatLng().latitude);
                currentLocation.transcoord();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("SidoName_autocomplete", "An error occurred: " + status);
            }
        });
    }
}
