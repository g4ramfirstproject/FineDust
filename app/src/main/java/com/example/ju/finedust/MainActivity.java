package com.example.ju.finedust;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
    static final String baseURL = "http://openapi.airkorea.or.kr/openapi/services/rest/";
    private PermissionRequest permissionRequest;
    private CurrentLocation mlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, SidoName.class);
        startActivity(intent);
    }
}
