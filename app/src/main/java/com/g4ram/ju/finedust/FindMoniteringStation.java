package com.g4ram.ju.finedust;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.g4ram.ju.finedust.Item.StationDustreturns;
import com.g4ram.ju.finedust.Item.MoniteringStationreturns;
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
    private String mstationName = "";
    private StationDustreturns mStationDustreturns;
    private MoniteringStationreturns moniteringStationreturns;
    private Handler mhandler;

    public FindMoniteringStation(Handler handler) {
        this.mhandler = handler;
        setUp();
    }

    public void setUp() {
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
        getUserLocalMoniteringStation(tmX, tmY, null);
    }

    //측정소 대기정보 가져오기
    public void getLocalFineDust(final String mstationName) {
        getLocalFineDust(mstationName, null);
    }

    //TM 좌표료 측정소 찾기 -> 바로 그 측정소 대기정보 메서드 실행
    public void getUserLocalMoniteringStation(String tmX, String tmY, final String searchedAddress) {
        //임시

        Call<MoniteringStationreturns> callBack = apiService.getMoniteringStation(tmX, tmY);
        callBack.enqueue(new Callback<MoniteringStationreturns>() {
            @Override
            public void onResponse(Call<MoniteringStationreturns> call, Response<MoniteringStationreturns> response) {
                moniteringStationreturns = response.body();


                if (moniteringStationreturns.getList()== null) {
                    Message msg = mhandler.obtainMessage();
                    msg.what = 1;
                    mhandler.sendMessage(msg);
                    return;
                }

                mstationName = moniteringStationreturns.getList().get(0).getStationName();
                //측정소 대기현황 요청 메서드
                if (searchedAddress != null) {
                    getLocalFineDust(mstationName, searchedAddress);
                } else {
                    getLocalFineDust(mstationName);
                }
            }

            @Override
            public void onFailure(Call<MoniteringStationreturns> call, Throwable t) {
                Log.e("테스트 CallBack", "MS onFailure: " + t.getMessage());
            }
        });

    }

    //측정소 대기정보 가져오기
    public void getLocalFineDust(final String mstationName, final String searchedAddress) {
        String dataTerm = "DAILY";
        float version = 1.3f;
        Call<StationDustreturns> callBack = apiService.getStationDust(mstationName, dataTerm, version);
        callBack.enqueue(new Callback<StationDustreturns>() {
            @Override
            public void onResponse(Call<StationDustreturns> call, Response<StationDustreturns> response) {
                //main 에서 쓸 대기정보객체 생성
                mStationDustreturns = new StationDustreturns();
                mStationDustreturns = response.body();
                if (mStationDustreturns.getList().get(0).getPm10Value().equals("-")) {
                    getLocalFineDust(moniteringStationreturns.getList().get(1).getStationName());
                    return;
                }
                mStationDustreturns.setStationName(mstationName);
                Message msg = mhandler.obtainMessage();
                msg.what = 0;
                if (searchedAddress != null) {
                    mStationDustreturns.setStationName(searchedAddress);
                }
                msg.obj = mStationDustreturns;
                mhandler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<StationDustreturns> call, Throwable t) {
                Log.e("테스트 CallBack", "LF onFailure: " + t.getMessage());
            }
        });
    }
}
