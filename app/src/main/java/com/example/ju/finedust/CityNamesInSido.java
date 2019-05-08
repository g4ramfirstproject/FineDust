package com.example.ju.finedust;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ju.finedust.Item.AreaInfoRetrofit;
import com.example.ju.finedust.Item.StationDustreturns;

import java.util.List;

public class CityNamesInSido extends AppCompatActivity {

    RecyclerView recyclerViewCityName;
    AdapterCityName adapterCityName;
    Context contextThis;
    List<AreaInfoRetrofit> cityList;
    String sidoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_names_in_sido);
        recyclerViewCityName = findViewById(R.id.RecyclerViewCityName);
        recyclerViewCityName.setLayoutManager(new GridLayoutManager(this,3));
        recyclerViewCityName.setItemAnimator(new DefaultItemAnimator());
        contextThis = this;
        init();
    }
    void init(){
        Intent intent = getIntent();
        sidoName = intent.getStringExtra("sidoName");
        GetInfoFromApi getInfoFromApi = new GetInfoFromApi();
//        getInfoFromApi.GetCityInfo(this,sidoName,bookmarkCallback);

    }
}
