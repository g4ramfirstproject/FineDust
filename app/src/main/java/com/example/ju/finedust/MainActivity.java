package com.example.ju.finedust;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ju.finedust.Item.StationDustreturns;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    static final String baseURL = "http://openapi.airkorea.or.kr/openapi/services/rest/";
    private PermissionRequest permissionRequest;
    private CurrentLocation mlocation;

    private TextView locationName, currentTime, locationDustLevel, locationDustLevelText, locationFineDustLevel, locationFineDustLevelText;
    private RecyclerView dailyRecyclerView,timeRecyclerView;
    private ImageView finddustImage;
    private AdapterHourlyForecast mAdapter;
    private SwipeRefreshLayout mainRefreshLayout;

    private CurrentLocation currentLocation;
    private StationDustreturns mStationDustreturns;

    private LinearLayoutManager mLayoutManger;
    private ProgressDialog mprogressDialog;

    private String apiKeyGooglePlaces;
    private Context co_this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        co_this = this;

        startProgressbar();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewSetup();
        localDustlevelSetup();
        getCurrentTime();

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchApiInit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                return true;
                      default :
                return super.onOptionsItemSelected(item) ;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);// main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void startProgressbar(){
        mprogressDialog = new ProgressDialog(this);
        mprogressDialog.setMessage("위치정보를 받아오는 중 입니다");
        mprogressDialog.show();
    }

    private void localDustlevelSetup() {
        //위치정보 퍼미션
        permissionRequest = new PermissionRequest(this);
        permissionRequest.locationAccess();

        //현재위치 대기정보 가져오기
        currentLocation = new CurrentLocation(this);
        currentLocation.tmLookup(mhandler);
    }

    private void viewSetup() {
        locationName = findViewById(R.id.locationName_tv);
        currentTime = findViewById(R.id.MainTime_tv);
        locationDustLevel = findViewById(R.id.MainDustLevel_tv);
        locationDustLevelText = findViewById(R.id.MainDustLevelText_tv);
        locationFineDustLevel = findViewById(R.id.MainFineDustLevel_tv);
        locationFineDustLevelText = findViewById(R.id.MainFineDustLevelText_tv);
        finddustImage = findViewById(R.id.MainFineDustImage);
        //시간별 리사이클러뷰
        timeRecyclerView = findViewById(R.id.MainrecyclerView);
        mLayoutManger = new LinearLayoutManager(this);
        mLayoutManger.setOrientation(LinearLayoutManager.HORIZONTAL);
        timeRecyclerView.setLayoutManager(mLayoutManger);
        mAdapter = new AdapterHourlyForecast(getApplicationContext());
        timeRecyclerView.setAdapter(mAdapter);
        //스와이프레이아웃
        mainRefreshLayout = findViewById(R.id.MainRefreshLayout);
        mainRefreshLayout.setOnRefreshListener(this);

    }


    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                mAdapter.clear();
                mStationDustreturns = (StationDustreturns) msg.obj;
                StationDustreturns.list dustvaluelist = mStationDustreturns.getList().get(0);
                locationName.setText(mStationDustreturns.getStationName());
                locationDustLevel.setText(dustvaluelist.getPm10Value());
                locationFineDustLevel.setText(dustvaluelist.getPm25Value());
              //  setMainImageAndLevelText(dustvaluelist.getPm10Value(),dustvaluelist.getPm25Value());

                DustLevelConverter converter = new DustLevelConverter();
                converter.setMainImageAndLevelText(dustvaluelist.getPm10Value(),dustvaluelist.getPm25Value());
                locationDustLevelText.setText(converter.getReturn10pm());
                locationFineDustLevelText.setText(converter.getReturn25pm());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finddustImage.setImageDrawable(getResources().getDrawable(converter.getReturnImage(), getApplicationContext().getTheme()));
                }else{
                    finddustImage.setImageResource(converter.getReturnImage());
                }

                for(int i=0; i<22; i+=3)
                {
                    StationDustreturns.list huijung = mStationDustreturns.getList().get(i);
                    String datatime = huijung.getDataTime().substring(11,13);
                    if(!huijung.getPm10Value().equals("-"))
                    {
                        String pm10Value = converter.dust10ValuetoText(Integer.parseInt(huijung.getPm10Value()));
                        mAdapter.add(datatime, pm10Value);
                    }


                }

                mprogressDialog.dismiss();
            }
            return false;
        }
    });


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nationwideFindDust) {
            Intent intent = new Intent(MainActivity.this, NationWideFineDustActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    public void getCurrentTime(){
        SimpleDateFormat timeFormat = new SimpleDateFormat ( "a hh시 mm분 ");
        Date time = new Date();
        String current = timeFormat.format(time);
        currentTime.setText(current);
    }

    @Override
    public void onRefresh() {
        localDustlevelSetup();
        getCurrentTime();

        mainRefreshLayout.setRefreshing(false);
    }

    void searchApiInit(){
        ImageButton a = findViewById(R.id.places_autocomplete_search_button);
        a.setImageResource(R.color.colorGood);

        ImageButton b = findViewById(R.id.places_autocomplete_clear_button);
        b.setImageResource(R.color.colorGood);

        apiKeyGooglePlaces = getString(R.string.api_key_googlemap);
        // Initialize Places.
        Places.initialize(getApplicationContext(), apiKeyGooglePlaces);
        // Create a new Places client instance.
        final PlacesClient placesClient = Places.createClient(co_this);
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setHint("");

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                currentLocation.transcoord(place.getLatLng().longitude,place.getLatLng().latitude);
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("SidoName_autocomplete", "An error occurred: " + status);
            }
        });
    }
}