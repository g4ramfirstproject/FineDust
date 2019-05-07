package com.example.ju.finedust;

import com.example.ju.finedust.Item.AreaInfoList;
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
    int numOfRows = 25;
    String returnType = "json";
    String fixURL = "_returnType="+returnType+
            "&pageNo="+pageNO+
            "&numOfRows="+numOfRows+
            "&ServiceKey="+serviceKey;

    String findMoniteringStation_URL = "MsrstnInfoInqireSvc/getNearbyMsrstnList?";
    String findLocalFineDust_URL = "ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?";
    String getCityDustInfo_URL = "ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?searchCondition=DAILY&";

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

    //각 시도에 포함된 행정구역(구/군)의 미세먼지 정보
    @GET(getCityDustInfo_URL+fixURL)
    Call<AreaInfoList> getAreaInfo(
            @Query(value = "sidoName") String sidoName,
            @Query(value = "serviceKey", encoded = true) String serviceKey);
}
