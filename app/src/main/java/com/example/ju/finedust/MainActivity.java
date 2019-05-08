package com.example.ju.finedust;

import android.os.Handler;
import android.os.Message;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ju.finedust.Item.StationDustreturns;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final String baseURL = "http://openapi.airkorea.or.kr/openapi/services/rest/";
    private PermissionRequest permissionRequest;
    private CurrentLocation mlocation;

    private TextView locationName, currentTime, locationDustLevel, locationDustLevelText, locationFineDustLevel, locationFineDustLevelText;
    private RecyclerView timeRecyclerView, dailyRecyclerView;
    private ImageView searchBtn, shareBtn;

    private CurrentLocation currentLocation;
    private StationDustreturns mStationDustreturns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
        timeRecyclerView = findViewById(R.id.MainrecyclerView);
//        searchBtn = findViewById(R.id.MainAppBarSearch_iv);
//        shareBtn = findViewById(R.id.MainAppBarShare_iv);
    }

    private String dustValuetoText(String dustvalue){
        int dustvalueint = Integer.parseInt(dustvalue);
        if (dustvalueint == 4){
            return "매우나쁨";
        }else if(dustvalueint == 3){
            return "나쁨";
        }else if(dustvalueint == 2){
            return "보통";
        }else {
            return "좋음";
        }
    }

    private Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0) {
                mStationDustreturns = (StationDustreturns) msg.obj;
                StationDustreturns.list dustvaluelist = mStationDustreturns.getList().get(0);
                locationName.setText(mStationDustreturns.getStationName());
                locationDustLevel.setText(dustvaluelist.getPm10Value());
                locationDustLevelText.setText(dustValuetoText(dustvaluelist.getPm10Grade()));
                locationFineDustLevel.setText(dustvaluelist.getPm25Value());
                locationFineDustLevelText.setText(dustValuetoText(dustvaluelist.getPm25Grade()));
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

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
