package com.example.ju.finedust;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final String baseURL = "http://openapi.airkorea.or.kr/openapi/services/rest/";
    private PermissionRequest permissionRequest;
    private CurrentLocation mlocation;

    private TextView locationName, currentTime, locationDustLevel, locationDustLevelText, locationFineDustLevel, locationFineDustLevelText;
    private RecyclerView timeRecyclerView, dailyRecyclerView;
    private ImageView searchBtn, shareBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewSetup();
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

    private void viewSetup() {
        locationName = findViewById(R.id.locationName_tv);
        currentTime = findViewById(R.id.MainTime_tv);
        locationDustLevel = findViewById(R.id.MainDustLevel_tv);
        locationDustLevelText = findViewById(R.id.MainDustLevelText_tv);
        locationFineDustLevel = findViewById(R.id.MainFineDustLevel_tv);
        locationFineDustLevelText = findViewById(R.id.MainFineDustLevelText_tv);
        timeRecyclerView = findViewById(R.id.MainrecyclerView);
        searchBtn = findViewById(R.id.MainAppBarSearch_iv);
        shareBtn = findViewById(R.id.MainAppBarShare_iv);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}