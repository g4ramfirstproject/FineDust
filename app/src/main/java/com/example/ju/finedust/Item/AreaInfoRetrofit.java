package com.example.ju.finedust.Item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AreaInfoRetrofit implements Serializable {

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
