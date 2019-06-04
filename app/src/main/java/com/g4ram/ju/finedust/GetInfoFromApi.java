package com.g4ram.ju.finedust;

import android.content.Context;
import android.widget.Toast;

import com.g4ram.ju.finedust.R;
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

    public void GetCityInfo(final Context parentContext, String sidoName, final CityNamesInSido.BookmarkCallback callback){
        final List<AreaInfoRetrofit> cityInfosNotDup = new ArrayList<>();
        Retrofit retrofit;
        String api_key = parentContext.getString(R.string.api_key);
        String baseUrlGetCityInfo = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/";
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrlGetCityInfo)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitGetAreaInfo retrofitGetCityInfo = retrofit.create(RetrofitGetAreaInfo.class);
        Call<AreaInfoList> call = retrofitGetCityInfo.getAreaInfo(sidoName,api_key);
        call.enqueue(new Callback<AreaInfoList>() {
            @Override
            public void onResponse(Call<AreaInfoList> call, Response<AreaInfoList> response) {
                List<AreaInfoRetrofit> cityinfos = response.body().getAreaInfoList();
                boolean isItFirst = true;
                for(AreaInfoRetrofit areainfo : cityinfos ) {
                    if (isItFirst) {
                        cityInfosNotDup.add(areainfo);
                        isItFirst = false;
                    } else {
                        if (!cityInfosNotDup.get(0).cityName.equals(areainfo.cityName)) {
                            cityInfosNotDup.add(areainfo);
                        } else {
                            break;
                        }
                    }
                }
                callback.onSuccess(cityInfosNotDup);
            }
            @Override
            public void onFailure(Call<AreaInfoList> call, Throwable t) {
                Toast.makeText(parentContext, "fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    interface RetrofitGetAreaInfo {
        @GET("getCtprvnMesureSidoLIst?_returnType=json&searchCondition=DAILY&pageNo=1&numOfRows=40")
        Call<AreaInfoList> getAreaInfo(
                @Query(value = "sidoName") String sidoName,
                @Query(value = "serviceKey", encoded = true) String serviceKey);

    }
    class AreaInfoList {
        @SerializedName("list")
        @Expose
        private List<AreaInfoRetrofit> areaInfoList = null;

        public List<AreaInfoRetrofit> getAreaInfoList() {
            return areaInfoList;
        }

        public void setAreaInfoList(List<AreaInfoRetrofit> areaInfoList) {
            this.areaInfoList = areaInfoList;
        }
    }

    class AreaInfoRetrofit implements Serializable {

        @SerializedName("cityName")
        @Expose
        private String cityName;

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }
        @SerializedName("coValue")
        @Expose
        private String coValue;

        @SerializedName("dataTime")
        @Expose
        private String dataTime;

        @SerializedName("no2Value")
        @Expose
        private String no2Value;

        @SerializedName("o3Value")
        @Expose
        private String o3Value;

        @SerializedName("pm10Value")
        @Expose
        private String pm10Value;

        @SerializedName("pm25Value")
        @Expose
        private String pm25Value;

        @SerializedName("so2Value")
        @Expose
        private String so2Value;

        public String getCoValue() {
            return coValue;
        }

        public void setCoValue(String coValue) {
            this.coValue = coValue;
        }

        public String getDataTime() {
            return dataTime;
        }

        public void setDataTime(String dataTime) {
            this.dataTime = dataTime;
        }

        public String getNo2Value() {
            return no2Value;
        }

        public void setNo2Value(String no2Value) {
            this.no2Value = no2Value;
        }

        public String getO3Value() {
            return o3Value;
        }

        public void setO3Value(String o3Value) {
            this.o3Value = o3Value;
        }

        public String getPm10Value() {
            return pm10Value;
        }

        public void setPm10Value(String pm10Value) {
            this.pm10Value = pm10Value;
        }

        public String getPm25Value() {
            return pm25Value;
        }

        public void setPm25Value(String pm25Value) {
            this.pm25Value = pm25Value;
        }

        public String getSo2Value() {
            return so2Value;
        }

        public void setSo2Value(String so2Value) {
            this.so2Value = so2Value;
        }
    }
}
