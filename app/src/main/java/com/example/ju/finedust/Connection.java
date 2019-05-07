package com.example.ju.finedust;

import com.example.ju.finedust.Item.StationDustreturns;
import com.example.ju.finedust.Item.MoniteringStationreturns;
import com.example.ju.finedust.Item.TM;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Connection {

    String kakaoApiKey = "24514f11cfa24621d324726a5606a2c4";

    String serviceKey = "87cT%2Fs7HFMgdsA2Yk2i3%2Bz%2FBRjNDquiyvrr3fFyYnHdwCmJU0Xy2ahBrSED4XZTvInfPB62j1%2BchnMJSkDZM3w%3D%3D";

    int pageNO = 1;
    int numOfRows = 1;
    String returnType = "json";
    String fixURL = "_returnType="+returnType+
            "&pageNo="+pageNO+
            "&numOfRows="+numOfRows+
            "&ServiceKey="+serviceKey;

    String findMoniteringStation_URL = "MsrstnInfoInqireSvc/getNearbyMsrstnList?";
    String findLocalFineDust_URL = "ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?";

    //카카오 REST Api 좌표 변환
    @Headers("Authorization: KakaoAK 24514f11cfa24621d324726a5606a2c4")
    @GET("v2/local/geo/transcoord.json")
    Call<TM> transcoord(@Query("x") double longitude, @Query("y") double latitude, @Query("input_coord") String inputType,
                        @Query("output_coord") String outputType);


    //근처 측정소 정보
    @GET(findMoniteringStation_URL+fixURL)
    Call<MoniteringStationreturns> getMoniteringStation(
            @Query("tmX") String tmX,
            @Query("tmY") String tmY
    );

    //측정소의 대기정보
    @GET(findLocalFineDust_URL+fixURL)
    Call<StationDustreturns> getStationDust(
            @Query("stationName") String stationName,
            @Query("dataTerm") String dataTerm,
            @Query("ver") float ver
    );
    //현재전국미세먼지현황
    @GET("openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureLIst?itemCode=PM10&dataGubun=HOUR&searchCondition=WEEK&pageNo=1&numOfRows=10&ServiceKey=87cT%2Fs7HFMgdsA2Yk2i3%2Bz%2FBRjNDquiyvrr3fFyYnHdwCmJU0Xy2ahBrSED4XZTvInfPB62j1%2BchnMJSkDZM3w%3D%3D&ver=1.3&_returnType=json")
    Call<NationWideFineDustData> getNationWideFineDust();
}
