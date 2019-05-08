package com.example.ju.finedust;

import android.content.Context;
import android.widget.Toast;

import com.example.ju.finedust.Item.AreaInfoList;
import com.example.ju.finedust.Item.AreaInfoRetrofit;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GetInfoFromApi {

    public void GetCityInfo(final Context parentContext, String sidoName, final CallbackFuncInterface callback){
        final List<AreaInfoRetrofit> cityInfosNotDup = new ArrayList<>();
        Retrofit retrofit;
        String api_key = parentContext.getString(R.string.api_key);
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Connection retrofitGetCityInfo = retrofit.create(Connection.class);
        Call<AreaInfoList> call = retrofitGetCityInfo.getAreaInfo(sidoName,api_key);
        String a = call.request().url().toString();
        call.enqueue(new Callback<AreaInfoList>() {
            @Override
            public void onResponse(Call<AreaInfoList> call, Response<AreaInfoList> response) {
                List<AreaInfoRetrofit> cityinfos = response.body().getList();
                boolean isItFirst = true;
                for(AreaInfoRetrofit areainfo : cityinfos ) {
                    if (isItFirst) {
                        cityInfosNotDup.add(areainfo);
                        isItFirst = false;
                    } else {
                        if (!cityInfosNotDup.get(0).getCityName().equals(areainfo.getCityName())) {
                            cityInfosNotDup.add(areainfo);
                        } else {
                            break;
                        }
                    }
                }
                callback.onSuccess_getCityDustInfo(cityInfosNotDup);
            }
            @Override
            public void onFailure(Call<AreaInfoList> call, Throwable t) {
                Toast.makeText(parentContext, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
