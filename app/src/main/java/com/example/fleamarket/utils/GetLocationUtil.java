package com.example.fleamarket.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by 王鹏飞
 * on 2020-02-15
 */
public class GetLocationUtil {

    @SuppressLint("MissingPermission")
    public static String getLocation(Activity context){
        double lat,lng;
        Location location;
        String location_now ="";

        String serviceString = context.LOCATION_SERVICE;// 获取的是位置服务
        LocationManager locationManager = (LocationManager) context.getSystemService(serviceString);// 调用getSystemService()方法来获取LocationManager对象
        String provider = LocationManager.GPS_PROVIDER;// 指定LocationManager的定位方法
        location = locationManager.getLastKnownLocation(provider);// 调用getLastKnownLocation()方法获取当前的位置信息

        String coordinate;
        String addressStr = "no address \n";
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            coordinate = "Latitude：" + lat + "\nLongitude：" + lng;
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat,
                        lng, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    sb.append(address.getAddressLine(0)).append(" ");
                    location_now = sb.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //如果用户没有允许app访问位置信息 则默认取上海松江经纬度的数据
            lat = 39.25631486;
            lng = 115.63478961;
            coordinate = "no coordinate!\n";
        }
        return location_now;
    }
   

}
