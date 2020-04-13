package com.example.fleamarket.utils;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

public class GetNetImgUtil {
    public static void roadingNetImg(SimpleDraweeView simpleDraweeView,String url){
        Uri uri = Uri.parse(url);
        simpleDraweeView.setImageURI(uri);
    }
   }
