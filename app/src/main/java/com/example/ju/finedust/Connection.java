package com.example.ju.finedust;

import com.example.ju.finedust.Item.TM;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Connection {

    String kakaoApiKey = "24514f11cfa24621d324726a5606a2c4";

    //카카오 REST Api 좌표 변환
    @Headers("Authorization: KakaoAK 24514f11cfa24621d324726a5606a2c4")
    @GET("v2/local/geo/transcoord.json")
    Call<TM> transcoord(@Query("x") double longitude, @Query("y") double latitude, @Query("input_coord") String inputType,
                        @Query("output_coord") String outputType);

    //String url = "https://dapi.kakao.com/v2/local/geo/transcoord.json?x=126.9808842&y=37.4806089&input_coord=WGS84&output_coord=TM"



}
