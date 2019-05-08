package com.example.ju.finedust;

import com.example.ju.finedust.Item.AreaInfoRetrofit;
import com.example.ju.finedust.Item.StationDustreturns;

import java.util.List;

public interface CallbackFuncInterface {
    void onSuccess_getCityTmCoordinate(String[] strings);
    void onSuccess_getCityDustInfo(List<AreaInfoRetrofit> areaInfoRetrofitList);
    void onSuccess_getDusInfoFromMoniteringStation(StationDustreturns.list stationDustreturns);
}

