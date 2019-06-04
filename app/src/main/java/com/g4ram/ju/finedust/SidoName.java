package com.g4ram.ju.finedust;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.g4ram.ju.finedust.R;

public class SidoName extends AppCompatActivity {

    private RecyclerView recyclerViewSidoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_fine_dust_by_sido_name);
        init();

    }
    public void init(){
        String[] SidoNames = {"서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주", "세종"};
        recyclerViewSidoName = findViewById(R.id.RecyclerViewSidoName);
        AdapterSidoName adapterSidoName = new AdapterSidoName(SidoNames,this);
        recyclerViewSidoName.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSidoName.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSidoName.setAdapter(adapterSidoName);
    }
}
