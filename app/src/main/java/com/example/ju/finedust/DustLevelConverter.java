package com.example.ju.finedust;

public class DustLevelConverter {
    private String return10pm;
    private String return25pm;
    private int returnImage;
    private String returnAvgDustLevel;

    public String getReturn10pm() {
        return return10pm;
    }

    public String getReturn25pm() {
        return return25pm;
    }

    public int getReturnImage() {
        return returnImage;
    }

    public String getReturnAvgDustLevel() { return returnAvgDustLevel; }


    public String dust10ValuetoText(int dustvalue) {
        if (dustvalue > 100) {
            return "매우나쁨";
        } else if (dustvalue > 50) {
            return "나쁨";
        } else if (dustvalue > 30) {
            return "보통";
        } else {
            return "좋음";
        }
    }

    private String dust25ValuetoText(int dustvalue) {
        if (dustvalue > 50) {
            return "매우나쁨";
        } else if (dustvalue > 35) {
            return "나쁨";
        } else if (dustvalue > 16) {
            return "보통";
        } else {
            return "좋음";
        }
    }


    public void setMainImageAndLevelText(String value10pm, String value25pm) {
        int value10 = Integer.parseInt(value10pm);
        int value25 = Integer.parseInt(value25pm);
        return10pm = dust10ValuetoText(value10);
        return25pm = dust25ValuetoText(value25);

        if (return10pm.equals("매우나쁨") || return25pm.equals("매우나쁨")) {
                returnImage = R.drawable.dustred;
                returnAvgDustLevel = "매우나쁨";
            } else if (return10pm.equals("나쁨") || return25pm.equals("나쁨")) {
                returnImage = R.drawable.dustyellow;
                returnAvgDustLevel = "나쁨";
            } else if (return10pm.equals("보통") || return25pm.equals("보통")) {
                returnImage = R.drawable.dustgreen;
                returnAvgDustLevel = "보통";
            } else {
                returnImage = R.drawable.dustblue;
                returnAvgDustLevel = "좋음";
            }
        }
    }
