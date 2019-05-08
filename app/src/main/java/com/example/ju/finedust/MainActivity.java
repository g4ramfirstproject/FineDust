package com.example.ju.finedust;

import android.graphics.drawable.Drawable;
import android.os.Build;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final String baseURL = "http://openapi.airkorea.or.kr/openapi/services/rest/";
    private PermissionRequest permissionRequest;
    private CurrentLocation mlocation;

    private TextView locationName, currentTime, locationDustLevel, locationDustLevelText, locationFineDustLevel, locationFineDustLevelText;
    private RecyclerView timeRecyclerView, dailyRecyclerView;
    private ImageView searchBtn, shareBtn, finddustImage;

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
        finddustImage = findViewById(R.id.MainFineDustImage);
    }

    private String dust10ValuetoText(int dustvalue){
        if (dustvalue > 100){
            return "매우나쁨";
        }else if(dustvalue > 50){
            return "나쁨";
        }else if(dustvalue > 30){
            return "보통";
        }else {
            return "좋음";
        }
    }

    private String dust25ValuetoText(int dustvalue){
        if (dustvalue > 50){
            return "매우나쁨";
        }else if(dustvalue > 35){
            return "나쁨";
        }else if(dustvalue > 16){
            return "보통";
        }else {
            return "좋음";
        }
    }


    private void setMainImageAndLevelText(String value10pm, String value25pm){
        int value10= Integer.parseInt(value10pm);
        int value25 = Integer.parseInt(value25pm);
        String returnvalue10pm = dust10ValuetoText(value10);
        String returnvalue25pm = dust25ValuetoText(value25);

        locationDustLevelText.setText(returnvalue10pm);
        locationFineDustLevelText.setText(returnvalue25pm);
        if (returnvalue10pm.equals("매우나쁨") || returnvalue25pm.equals("매우나쁨")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finddustImage.setImageDrawable(getResources().getDrawable(R.drawable.dustred, getApplicationContext().getTheme()));
            }else{
                finddustImage.setImageResource(R.drawable.dustred);
            }
        }
        else if(returnvalue10pm.equals("나쁨") || returnvalue25pm.equals("나쁨")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finddustImage.setImageDrawable(getResources().getDrawable(R.drawable.dustyellow, getApplicationContext().getTheme()));
            }else{
                finddustImage.setImageResource(R.drawable.dustyellow);
            }
        }
        else if (returnvalue10pm.equals("보통") || returnvalue25pm.equals("보통")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finddustImage.setImageDrawable(getResources().getDrawable(R.drawable.dustgreen, getApplicationContext().getTheme()));
            }else{
                finddustImage.setImageResource(R.drawable.dustgreen);
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finddustImage.setImageDrawable(getResources().getDrawable(R.drawable.dustblue, getApplicationContext().getTheme()));
            }else{
                finddustImage.setImageResource(R.drawable.dustblue);
            }
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
                locationFineDustLevel.setText(dustvaluelist.getPm25Value());
                setMainImageAndLevelText(dustvaluelist.getPm10Value(),dustvaluelist.getPm25Value());
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
