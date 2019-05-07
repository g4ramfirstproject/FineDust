package com.example.ju.finedust.Item;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AreaInfoList {

    @SerializedName("list")
    @Expose
    private List<AreaInfoRetrofit> list = null;

    public List<AreaInfoRetrofit> getList() {
        return list;
    }

    public void setList(List<AreaInfoRetrofit> list) {
        this.list = list;
    }
}
