package com.g4ram.ju.finedust.Item;

import com.g4ram.ju.finedust.R;

public class ItemHourlyForecast {
    String currentTime;
    int resultcolor;
    String result;

    public ItemHourlyForecast(String currentTime, int resultcolor, String result) {
        this.currentTime = currentTime;
        this.resultcolor = resultcolor;
        this.result = result;
    }

    public ItemHourlyForecast(String currentTime, String result) {
        this.currentTime = currentTime;
        this.result = result;
        if(result.equals("나쁨")){
            this.resultcolor = R.drawable.dustyellow;
        }
        else if(result.equals("매우나쁨"))
        {
            this.resultcolor = R.drawable.dustred;
        }
        else if(result.equals("보통"))
        {
            this.resultcolor = R.drawable.dustgreen;
        }
        else if(result.equals("좋음"))
        {
            this.resultcolor = R.drawable.dustblue;
        }
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public int getResultcolor() {
        return resultcolor;
    }

    public void setResultcolor(int resultcolor) {
        this.resultcolor = resultcolor;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
