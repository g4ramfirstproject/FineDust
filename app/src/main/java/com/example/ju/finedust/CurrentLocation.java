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
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.ju.finedust.Item.Documents;
import com.example.ju.finedust.Item.TM;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

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
    double mlatitude;
    //경도
    double mlongitude;
    //TM X
    String mTmX;
    //TM Y
    String mTmY;

    private Retrofit retrofit;
    private OkHttpClient stetho;
    private Connection apiservice;
    private Handler mhandler;

    public CurrentLocation(Context context) {
        this.mcontext = context;
        mlocationManager = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);
    }

    //현재 위치에 따른 위도, 경도 받아오기
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

                    Toast.makeText(mcontext, "위도" + mlatitude + "경도" + mlongitude, Toast.LENGTH_SHORT).show();
                    transcoord();
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

            if (isGPSEnable && isNetworkEnable) {
                mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 100, mlocationListener);
                mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 100, mlocationListener);

            } else if (!isGPSEnable) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mcontext.startActivity(intent);
            }
        }
    }

    //받아온 위도 경도 - > TM 좌표로 변환
    public void transcoord() {

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

        Call<TM> call = apiservice.transcoord(mlongitude, mlatitude, "WGS84", "TM");

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
        locationLookup();

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
}
