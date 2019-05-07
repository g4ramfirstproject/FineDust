package com.example.ju.finedust;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.ju.finedust.Item.AreaInfoRetrofit;

public class EachCityDustInfo extends AppCompatActivity {

    TextView valueDetailCityNameTextView, valueDetailCityFineDustValue,valueDetailCityUltraFineDustValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_city_dust_info);
        AreaInfoRetrofit dustInfo = (AreaInfoRetrofit) getIntent().getSerializableExtra("dustInfo");
        if(dustInfo.getPm10Value().equals("")){
            dustInfo.setPm10Value("정보 없음");
        }
        if(dustInfo.getPm25Value().equals("")){
            dustInfo.setPm25Value("정보 없음");
        }
        valueDetailCityNameTextView = findViewById(R.id.valueDetailCityNameTextView);
        valueDetailCityNameTextView.setText(dustInfo.getCityName());
        valueDetailCityUltraFineDustValue = findViewById(R.id.valueDetailCityUltraFineDustValue);
        valueDetailCityUltraFineDustValue.setText(dustInfo.getPm10Value());
        valueDetailCityFineDustValue = findViewById(R.id.valueDetailCityFineDustValue);
        valueDetailCityFineDustValue.setText(dustInfo.getPm25Value());
    }
}
