package com.example.ju.finedust;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.ju.finedust.Item.Documents;
import com.example.ju.finedust.Item.TM;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CurrentLocation {

    Context mcontext;

    private LocationManager mlocationManager;
    private LocationListener mlocationListener;
    private FindMoniteringStation mfindMoniteringStation;
    //위도
    private double mlatitude;
    //경도
    private double mlongitude;
    //TM X
    private String mTmX;
    //TM Y
    private String mTmY;

    public void setMlatitude(double mlatitude) {
        this.mlatitude = mlatitude;
    }

    public void setMlongitude(double mlongitude) {
        this.mlongitude = mlongitude;
    }

    private Retrofit retrofit;
    private OkHttpClient stetho;
    private Connection apiservice;
    private Handler mhandler;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private OnCompleteListener<Location> mCompleteListener;

    public CurrentLocation(Context context) {
        this.mcontext = context;
        //mlocationManager = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mcontext);
        mCompleteListener = new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    Location mCurrentLocation = task.getResult();
//                    Toast.makeText(mcontext, "lat : " + mCurrentLocation.getLatitude()
//                            + "lng" +mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                    Log.e("새로운 위도", String.valueOf(mCurrentLocation.getLatitude()));
                    Log.e("새로운 경도", String.valueOf(mCurrentLocation.getLongitude()));
                    double latitude = mCurrentLocation.getLatitude();
                    double longitude = mCurrentLocation.getLongitude();

                    transcoord(longitude, latitude);
                }
                else{

                }
            }
        };
    }
    //현재 위치에 따른 위도, 경도 받아오기
    public void getCurrentLocation(){

        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mcontext, "위치 권한을 허용 해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(mCompleteListener);
    }

    //받아온 위도 경도 - > TM 좌표로 변환
    public void transcoord(double longitude, double latitude) {

        Stetho.initializeWithDefaults(mcontext);

        stetho = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(stetho)
                .build();


        apiservice = retrofit.create(Connection.class);

        Call<TM> call = apiservice.transcoord(longitude, latitude, "WGS84", "TM");

        call.enqueue(new Callback<TM>() {
            @Override
            public void onResponse(Call<TM> call, Response<TM> response) {
                TM tm = response.body();
                Log.e("응답TM", tm.toString());
                List<Documents> list = tm.getDocuments();
                Documents documents = list.get(0);
                mTmX = tm.getX();
                mTmY = tm.getY();

                mfindMoniteringStation = new FindMoniteringStation(mhandler);
                mfindMoniteringStation.getUserLocalMoniteringStation(mTmX,mTmY);

                Log.e("TM좌표", mTmX + "     " + mTmY);
            }

            @Override
            public void onFailure(Call<TM> call, Throwable t) {
                Toast.makeText(mcontext, "요청 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tmLookup() {
        getCurrentLocation();
    }

    public void tmLookup(Handler handler){
        this.mhandler = handler;
        tmLookup();
    }

    public double getMlatitude() {
        return mlatitude;
    }

    public double getMlongitude() {
        return mlongitude;
    }

    public String getmTmX() {
        return mTmX;
    }

    public String getmTmY() {
        return mTmY;
    }

    public void locationLookup() {

        if (mlocationManager != null) {

            boolean isGPSEnable = mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnable = mlocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            mlocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mlatitude = location.getLatitude();
                    mlongitude = location.getLongitude();

                    Log.e("위도", String.valueOf(mlatitude));
                    Log.e("경도", String.valueOf(mlongitude));

                    transcoord(mlongitude, mlatitude);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    //Log.e(TAG, "onStatusChanged : ");
                }

                @Override
                public void onProviderEnabled(String provider) {
                    //Log.e(TAG, "onProviderEnabled : ");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    //Log.e(TAG, "onProviderDisabled : ");
                }
            };

            //아래 코드를 실행시키기 위해서 임의적으로 한번 더 권한 체크를 하여야함. 그렇지 않으면 error
            if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            if (isNetworkEnable) {
                //mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 100, mlocationListener);
                mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, mlocationListener);

            } else if (!isNetworkEnable) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mcontext.startActivity(intent);
            }
        }
    }
}
