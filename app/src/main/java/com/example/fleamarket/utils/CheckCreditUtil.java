package com.example.fleamarket.utils;

import android.graphics.Color;
import android.widget.TextView;

public class CheckCreditUtil {
    public static void getCreditStr(TextView credit,int a){
        if(a>89){
            credit.setText("信誉优秀");
            credit.setTextColor(Color.parseColor("#005831"));
        }else if(a>79){
            credit.setText("信誉良好");
            credit.setTextColor(Color.parseColor("#1d953f"));
        }else if(a>59){
            credit.setText("信誉一般");
            credit.setTextColor(Color.parseColor("#f47920"));
        }else{
            credit.setText("信誉差");
            credit.setTextColor(Color.parseColor("#ed1941"));
        }

    }
}
