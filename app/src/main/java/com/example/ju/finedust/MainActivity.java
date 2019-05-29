package com.example.ju.finedust;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.PixelCopy;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ju.finedust.Item.ItemHourlyForecast;

import com.example.ju.finedust.Item.StationDustreturns;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.storage.ImageDeleteResponse;
import com.kakao.network.storage.ImageUploadResponse;
import com.kakao.util.helper.log.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.http.HEAD;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    static final String baseURL = "http://openapi.airkorea.or.kr/openapi/services/rest/";

    private TextView locationName, currentTime, locationDustLevel, locationDustLevelText, locationFineDustLevel, locationFineDustLevelText;
    private RecyclerView dailyRecyclerView,timeRecyclerView;
    private ImageView finddustImage;
    private AdapterHourlyForecast mAdapter;
    private SwipeRefreshLayout mainRefreshLayout;

    private CurrentLocation currentLocation;
    private StationDustreturns mStationDustreturns;

    private LinearLayoutManager mLayoutManger;
    private ProgressDialog mprogressDialog;
    private int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_READ_EXTERNAL_STORAGE;

    private String dust25StringValue;

    private String apiKeyGooglePlaces;
    private Context co_this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
        co_this = this;

        //startProgressbar();

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
    public Bitmap takeScreenshot() {
        ScrollView scrollView = findViewById(R.id.scrollView_main);
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getChildAt(0).getWidth(), scrollView.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap bitmap_back = Bitmap.createBitmap(scrollView.getChildAt(0).getHeight(), scrollView.getChildAt(0).getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas_back = new Canvas(bitmap_back);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = scrollView.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        scrollView.draw(canvas);
        int centreX = (bitmap_back.getWidth() - bitmap.getWidth()) / 2;
        int centreY = (canvas.getHeight() - bitmap.getHeight()) / 2;
        canvas_back.drawBitmap(bitmap, centreX, centreY, null);
        return bitmap_back;
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public void checkPermissionWriteExternalStorage(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sharePic();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
        } else if (requestCode == MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sharePic();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void checkPermissionReadExternalStorage(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
            }
        }
    }
    public File saveBitmap(Bitmap bitmap) {
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return imagePath;
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
            return imagePath;
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
            return imagePath;
        }
    }
    public void sharePic(){
        Bitmap bitmap = takeScreenshot();
        final File tempCaptureFile = saveBitmap(bitmap);
        KakaoLinkService.getInstance().uploadImage(this, false, tempCaptureFile, new ResponseCallback<ImageUploadResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {

                Log.e("윤희중","실패메세지");

                Log.i("ㅁㄴㅇㄹ",errorResult.toString());


            }

            @Override
            public void onSuccess(ImageUploadResponse result) {
                Log.i("강래민","돌긴도나");
                sendLink(result, tempCaptureFile);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                return true;
            case R.id.menu_share:
                int permissionCheckWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int permissionCheckRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCheckWrite == PackageManager.PERMISSION_DENIED){
                    checkPermissionWriteExternalStorage();
                }
                if(permissionCheckRead == PackageManager.PERMISSION_DENIED){
                    checkPermissionReadExternalStorage();
                }
                if(permissionCheckWrite == PackageManager.PERMISSION_GRANTED && permissionCheckRead == PackageManager.PERMISSION_GRANTED){
                    sharePic();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteImage(String imageUrl) {
        KakaoLinkService.getInstance().deleteImageWithUrl(this, imageUrl, new ResponseCallback<ImageDeleteResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(ImageDeleteResponse result) {
                Log.i("ImageDel", result.toString());
            }
        });
    }

    public void sendLink(ImageUploadResponse result, final File tempCaptureFile) {
        Log.i("강래민","돌긴도나2");

        String stringValue = null;
        switch (dust25StringValue){
            case "매우나쁨":
                stringValue = "대기중에 미세먼지가 매우 많으므로 외출을 삼가해 주세요";
                break;
            case "나쁨":
                stringValue = "미세먼지가 많습니다. 외출하실때 마스크를 꼭 챙기세요.";
                break;
            case "보통":
                stringValue = "무난무난한 날이에요.";
                break;
            case "좋음":
                stringValue = "쾌적한 날이에요.";
                break;
        }


        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("미세먼지 : "+dust25StringValue,
                        result.getOriginal().getUrl(),
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption(stringValue)
                        .build())
                .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                        .setWebUrl("'https://developers.kakao.com")
                        .setMobileWebUrl("'https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();
        Map<String, String> serverCallbackArgs = new HashMap<String, String>();
        serverCallbackArgs.put("user_id", "${current_user_id}");
        serverCallbackArgs.put("product_id", "${shared_product_id}");
        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.i("강래민","돌긴도나3");

                tempCaptureFile.delete();
                Log.e("실수","오마갓");
                Log.e("실수2",errorResult.getErrorMessage().toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                Log.i("강래민","돌긴도나4");

                tempCaptureFile.delete();
                Log.e("성공","오마갓");
                result.getTemplateMsg().toString();
            }
        });
    }
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
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

                dust25StringValue = converter.getReturnAvgDustLevel();
                //dust25StringValue = converter.getReturn25pm();


                converter.setMainImageAndLevelText(dustvaluelist.getPm10Value(),dustvaluelist.getPm25Value());
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

                //mprogressDialog.dismiss();
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
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS_COMPONENTS));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                String address;
                if(place.getAddressComponents().asList().get(0).getTypes().get(0).equals("premise")){
                    address = place.getAddressComponents().asList().get(1).getShortName();
                } else {
                    address = place.getAddressComponents().asList().get(0).getShortName();
                }
                currentLocation.transcoord(place.getLatLng().longitude,place.getLatLng().latitude, address);
            }
            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("SidoName_autocomplete", "An error occurred: " + status);
            }
        });
    }
}