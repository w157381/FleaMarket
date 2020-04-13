package com.example.fleamarket.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTimeUtil {
    public static String gettime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return  simpleDateFormat.format(date);
    }



    public static String getTimeExpend(String startTime, String endTime){
        //传入字串类型 2016/06/28 08:30
        String time =null;
        long longStart = getTimeMillis(startTime); //获取开始时间毫秒数
        long longEnd = getTimeMillis(endTime);  //获取结束时间毫秒数
        long longExpend = longEnd - longStart;  //获取时间差
        long longHours = longExpend / (60 * 60 * 1000); //根据时间差来计算小时数
        long longMinutes = (longExpend - longHours * (60 * 60 * 1000)) / (60 * 1000);   //根据时间差来计算分钟数
        if(longHours==0){
            if(longMinutes==0){
                time = "刚刚";
            }
            time = longMinutes+"分钟以前";
        }else if(longHours>0){
            time = longHours+"小时以前";
        }else if(longHours>24){
            time = longHours/24+"天以前";

        }else if(longHours>24*30){
            time = longHours/(24*30)+"月以前";
        }else if(longHours>24*30*12){
            time = startTime;
        }

        return time;
    }


    public static long getTimelongExpend(String nowTime, String freezeDate){
        //传入字串类型 2016/06/28 08:30
        String time =null;
        long longStart = getTimeMillis(nowTime); //获取开始时间毫秒数
        long longEnd = getTimeMillis(freezeDate)+1000*60*60*24*3;  //获取结束时间毫秒数
        long longExpend = longEnd - longStart;  //获取时间差

        return longExpend;
    }


    private static long getTimeMillis(String strTime) {
        long returnMillis = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = null;
        try {
            d = sdf.parse(strTime);
            returnMillis = d.getTime();
        } catch (ParseException e) {
            Log.e("ParseException",e.getMessage());
        }
        return returnMillis;
    }
    // formatType要转换的string类型的时间格式
    public static String longToString(long currentTime, String formatType)
            throws ParseException {
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型
        String strTime = dateToString(date, formatType); // date类型转成String
        return strTime;
    }
    // data Date类型的时间
    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static Date longToDate(long currentTime, String formatType)
            throws ParseException {
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

}
