package com.example.ju.finedust;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NationWideFineDustActivity extends AppCompatActivity {

    private Connection fineDustApi;
    private ArrayList<Integer> arrrNationWideList;
    private ArrayList<CircleImageView> arrCircleImageViewList;
    OkHttpClient stetho;
    @BindView(R.id.circle_busan)
    CircleImageView circle_busan;
    @BindView(R.id.circle_gyeonggi)
    CircleImageView circle_gyeonggi;
    @BindView(R.id.circle_gangwon)
    CircleImageView circle_gangwon;
    @BindView(R.id.circle_chungnam)
    CircleImageView circle_chungnam;
    @BindView(R.id.circle_chungbuk)
    CircleImageView circle_chungbuk;
    @BindView(R.id.circle_gyeongbuk)
    CircleImageView circle_gyeongbuk;
    @BindView(R.id.circle_daejeon)
    CircleImageView circle_daejeon;
    @BindView(R.id.circle_daegu)
    CircleImageView circle_daegu;
    @BindView(R.id.circle_jeonbuk)
    CircleImageView circle_jeonbuk;
    @BindView(R.id.circle_gwangju)
    CircleImageView circle_gwangju;
    @BindView(R.id.circle_jeonnam)
    CircleImageView circle_jeonnam;
    @BindView(R.id.circle_jeju)
    CircleImageView circle_jeju;
    @BindView(R.id.circle_gyeongnam)
    CircleImageView circle_gyeongnam;
    @BindView(R.id.circle_ulsan)
    CircleImageView circle_ulsan;
    @BindView(R.id.circle_seoul)
    CircleImageView circle_seoul;
    @BindView(R.id.measuretime_tv)
    //측정시간
    TextView measuretime_tv;
    //측정값
    @BindView(R.id.gyeongggi_tv)
    TextView  gyeongggi_tv;
    @BindView(R.id.gangwon_tv)
    TextView gangwon_tv;
    @BindView(R.id.seoul_tv)
    TextView seoult_tv;
    @BindView(R.id.chungnam_tv)
    TextView chungnam_tv;
    @BindView(R.id.chungbuk_tv)
    TextView chungbuk_tv;
    @BindView(R.id.gyeongbuk_tv)
    TextView gyeongbuk_tv;
    @BindView(R.id.daejeon_tv)
    TextView daejeon_tv;
    @BindView(R.id.daegu_tv)
    TextView daegu_tv;
    @BindView(R.id.jeonbuk_tv)
    TextView jeonbuk_tv;
    @BindView(R.id.ulsan_tv)
    TextView ulsan_tv;
    @BindView(R.id.gwangju_tv)
    TextView gwangju_tv;
    @BindView(R.id.jeonnam_tv)
    TextView jeonnam_tv;
    @BindView(R.id.gyeongnam_tv)
    TextView gyeongnam_tv;
    @BindView(R.id.busan_tv)
    TextView busan_tv;
    @BindView(R.id.jeju_tv)
    TextView jeju_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nation_wide_fine_dust);
        arrrNationWideList = new ArrayList<>();
        arrCircleImageViewList = new ArrayList<>();
        ButterKnife.bind(this);
        stetho = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        Stetho.initializeWithDefaults(this);
        //레트로핏초기화
        initializeretrofit();
        //circleimageview 어레이리스트에 초기화
        inputCircleImageviewInArrayList();
        //실시간 전국미세먼지 현황 데이터불러오기
        getNationWideFineDustData();

    }
    //실시간 전국미세먼지현황 데이터불러오기
    private void getNationWideFineDustData()
    {
        Call<NationWideFineDustData> call = fineDustApi.getNationWideFineDust();
        call.enqueue(new Callback<NationWideFineDustData>() {
            @Override
            public void onResponse(Call<NationWideFineDustData> call, Response<NationWideFineDustData> response) {
                NationWideFineDustData getNationWideFineDustData = response.body();
                //최신데이터
                String  gangwon =getNationWideFineDustData.getList().get(0).getGangwon();
                String  gwangju = getNationWideFineDustData.getList().get(0).getGwangju();
                String  ulsan = getNationWideFineDustData.getList().get(0).getUlsan();
                String  gyeongnam = getNationWideFineDustData.getList().get(0).getGyeongnam();
                String  chungnam = getNationWideFineDustData.getList().get(0).getChungnam();
                String  daejeon = getNationWideFineDustData.getList().get(0).getDaejeon();
                String  daegu= getNationWideFineDustData.getList().get(0).getDaegu();
                String  gyeonggi = getNationWideFineDustData.getList().get(0).getGyeonggi();
                String  jeonbuk  = getNationWideFineDustData.getList().get(0).getJeonbuk();
                String  chungbuk = getNationWideFineDustData.getList().get(0).getChungbuk();
                String  seoul = getNationWideFineDustData.getList().get(0).getSeoul();
                String  dataTime =  getNationWideFineDustData.getList().get(0).getDataTime();
                String  busan = getNationWideFineDustData.getList().get(0).getBusan();
                String  gyeongbuk = getNationWideFineDustData.getList().get(0).getGyeongbuk();
                String  jeju = getNationWideFineDustData.getList().get(0).getJeju();
                String  jeonnam = getNationWideFineDustData.getList().get(0).getJeonnam();
                //측정시간settext
                measuretime_tv.setText("측정시간: "+ dataTime);
                //각지역마다 측정값 SETTEXT하기
                gyeongggi_tv.setText(gyeonggi);
                gangwon_tv.setText(gangwon);
                seoult_tv.setText(seoul);
                chungnam_tv.setText(chungnam);
                chungbuk_tv.setText(chungbuk);
                gyeongbuk_tv.setText(gyeongbuk);
                daejeon_tv.setText(daejeon);
                daegu_tv.setText(daegu);
                jeonbuk_tv.setText(jeonbuk);
                ulsan_tv.setText(ulsan);
                gwangju_tv.setText(gwangju);
                jeonnam_tv.setText(jeonnam);
                gyeongnam_tv.setText(gyeongnam);
                busan_tv.setText(busan);
                jeju_tv.setText(jeju);
                //측정값 arraylist에 담기
                arrrNationWideList.add(Integer.parseInt(busan));
                arrrNationWideList.add(Integer.parseInt(gyeonggi));
                arrrNationWideList.add(Integer.parseInt(gangwon));
                arrrNationWideList.add(Integer.parseInt(gwangju));
                arrrNationWideList.add(Integer.parseInt(gyeongnam));
                arrrNationWideList.add(Integer.parseInt(gyeongbuk));
                arrrNationWideList.add(Integer.parseInt(chungnam));
                arrrNationWideList.add(Integer.parseInt(chungbuk));
                arrrNationWideList.add(Integer.parseInt(daejeon));
                arrrNationWideList.add(Integer.parseInt(daegu));
                arrrNationWideList.add(Integer.parseInt(jeonbuk));
                arrrNationWideList.add(Integer.parseInt(jeonnam));
                arrrNationWideList.add(Integer.parseInt(jeju));
                arrrNationWideList.add(Integer.parseInt(ulsan));
                arrrNationWideList.add(Integer.parseInt(seoul));
                //측정값WHO기준에 따라 분리하기
                sortCurrentNationWideFineDust();
                arrrNationWideList.clear();
            }

            @Override
            public void onFailure(Call<NationWideFineDustData> call, Throwable t) {
                Toast.makeText(NationWideFineDustActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //레트로핏초기화및 apiconfig 인터페이스 초기화
    private void initializeretrofit()
    {
        //레트로핏 빌드하기
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://openapi.airkorea.or.kr/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(stetho)
                .build();
        //mapiconfig 초기화
        fineDustApi = retrofit.create(Connection.class);
    }
    //측정값WHO기준에 따라 분리하기
    private void sortCurrentNationWideFineDust()
    {
        for(int i =0; i<arrrNationWideList.size(); i++)
        {
            //좋음
            if(arrrNationWideList.get(i)<30)
            {
                arrCircleImageViewList.get(i).setImageResource(R.color.colorGood);
            }
            //보통
            else if(arrrNationWideList.get(i)<=50 && arrrNationWideList.get(i)>30)
            {
                arrCircleImageViewList.get(i).setImageResource(R.color.colorNormal);
            }
            //나쁨
            else if(arrrNationWideList.get(i)<=100 && arrrNationWideList.get(i)>50)
            {
                arrCircleImageViewList.get(i).setImageResource(R.color.colorBad);
            }
            //매우나쁨
            else if(arrrNationWideList.get(i)>100)
            {
                arrCircleImageViewList.get(i).setImageResource(R.color.colorVeryBad);
            }
        }

    }
    private void inputCircleImageviewInArrayList()
    {
        arrCircleImageViewList.add(circle_busan);
        arrCircleImageViewList.add(circle_gyeonggi);
        arrCircleImageViewList.add(circle_gangwon);
        arrCircleImageViewList.add(circle_gwangju);
        arrCircleImageViewList.add(circle_gyeongnam);
        arrCircleImageViewList.add(circle_gyeongbuk);
        arrCircleImageViewList.add(circle_chungnam);
        arrCircleImageViewList.add(circle_chungbuk);
        arrCircleImageViewList.add(circle_daejeon);
        arrCircleImageViewList.add(circle_daegu);
        arrCircleImageViewList.add(circle_jeonbuk);
        arrCircleImageViewList.add(circle_jeonnam);
        arrCircleImageViewList.add(circle_jeju);
        arrCircleImageViewList.add(circle_ulsan);
        arrCircleImageViewList.add(circle_seoul);
    }






}
