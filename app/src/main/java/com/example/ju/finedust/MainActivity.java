package com.example.ju.finedust;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ju.finedust.Item.ItemHourlyForecast;
import com.example.ju.finedust.Item.StationDustreturns;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search :
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent,0);
                return true ;
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
        currentLocation.getCurrentLocation();
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 0:
                    Double lat = data.getDoubleExtra("Lat",0);
                    Double lng = data.getDoubleExtra("Lng",0);
                    currentLocation.transcoord(lng,lat);
                    break;
            }
        }
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
                mAdapter.clear();
                mStationDustreturns = (StationDustreturns) msg.obj;
                StationDustreturns.list dustvaluelist = mStationDustreturns.getList().get(0);
                if(dustvaluelist.getPm10Value().equals("-") || dustvaluelist.getPm25Value().equals("-")){
                    dustvaluelist = mStationDustreturns.getList().get(1);
                }
                locationName.setText(mStationDustreturns.getStationName());
                locationDustLevel.setText(dustvaluelist.getPm10Value());
                locationFineDustLevel.setText(dustvaluelist.getPm25Value());
                setMainImageAndLevelText(dustvaluelist.getPm10Value(),dustvaluelist.getPm25Value());
                for(int i=0; i<22; i+=3)
                {
                    StationDustreturns.list huijung = mStationDustreturns.getList().get(i);
                    String datatime = huijung.getDataTime().substring(11,13);
                    if(!huijung.getPm10Value().equals("-"))
                    {
                        String pm10Value = dust10ValuetoText(Integer.parseInt(huijung.getPm10Value()));
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
}