package com.example.ju.finedust;

import android.util.Log;

import com.example.ju.finedust.Item.StationDustreturns;
import com.example.ju.finedust.Item.MoniteringStationreturns;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindMoniteringStation {
    private Retrofit client;
    private Connection apiService;
    private OkHttpClient stetho;
    private String stationName = "";


    public FindMoniteringStation() {
        stetho = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        client = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(MainActivity.baseURL)
                .client(stetho)
                .build();

        apiService = client.create(Connection.class);
    }

    //TM 좌표료 측정소 찾기 -> 바로 그 측정소 대기정보 메서드 실행
    public void getUserLocalMoniteringStation(String tmX, String tmY) {
        //임시

        Call<MoniteringStationreturns> callBack = apiService.getMoniteringStation(tmX, tmY);
        callBack.enqueue(new Callback<MoniteringStationreturns>() {
            @Override
            public void onResponse(Call<MoniteringStationreturns> call, Response<MoniteringStationreturns> response) {
                MoniteringStationreturns moniteringStationreturns = response.body();
                stationName = moniteringStationreturns.getList().get(0).getStationName();
                getLocalFineDust(stationName);
            }

            @Override
            public void onFailure(Call<MoniteringStationreturns> call, Throwable t) {
                Log.e("테스트 CallBack", "MS onFailure: " + t.getMessage());
            }
        });

    }

    //측정소 대기정보 가져오기
    public void getLocalFineDust(String stationName) {
        String dataTerm = "DAILY";
        float version = 1.0f;
        Call<StationDustreturns> callBack = apiService.getStationDust(stationName, dataTerm, version);
        callBack.enqueue(new Callback<StationDustreturns>() {
            @Override
            public void onResponse(Call<StationDustreturns> call, Response<StationDustreturns> response) {
                StationDustreturns stationDustreturns = response.body();
                Log.e("테스트 CallBack", "onResponse: " + stationDustreturns.getList().get(0));
            }

            @Override
            public void onFailure(Call<StationDustreturns> call, Throwable t) {
                Log.e("테스트 CallBack", "LF onFailure: " + t.getMessage());
            }
        });
    }
}
