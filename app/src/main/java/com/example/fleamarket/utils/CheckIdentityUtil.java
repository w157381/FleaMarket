package com.example.fleamarket.utils;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.fleamarket.utils.RequestParamUtils.of_dec_hex36;
import static com.example.fleamarket.utils.RequestParamUtils.stringToAscii;


public class CheckIdentityUtil{

    public static String of_encrypt(String as_str, String as_key) {
        if (as_str == null || "".equals(as_str) || as_key == null || "".equals(as_key)) {
            return as_str;
        }
        String ls_rtn1 = "";
        String ls_rtn2 = "";
        int ll_keylen = as_key.length();
        int ll_strlen = as_str.length();
        int ll_loop = (int) Math.ceil((((double) ll_strlen) * 1.0d) / ((double) ll_keylen));
        int ll_add = (((int) Math.ceil((((((double) ll_strlen) * 3.0d) * 6.0d) / 9.0d) / 6.0d)) * 6) % ll_keylen;
        for (int ll_i = 0; ll_i < ll_loop; ll_i++) {
            for (int ll_k = 1; ll_k <= ll_keylen; ll_k++) {
                int ll_pos = (ll_i * ll_keylen) + ll_k;
                String ls_s = "000" + String.valueOf(Integer.valueOf(stringToAscii(as_str.substring(ll_pos - 1, ll_pos))).intValue() + Integer.valueOf(stringToAscii(as_key.substring(ll_k - 1, ll_k))).intValue() + ll_add);
                ls_rtn1 = ls_rtn1 + ls_s.substring(ls_s.length() - 3, ls_s.length());
                if (ll_pos == ll_strlen) {
                    break;
                }
            }
        }
        int ll_pos2 = 0;
        while (ll_pos2 < ls_rtn1.length()) {
            int start = ll_pos2;
            int end = start + 9;
            if (end >= ls_rtn1.length()) {
                end = ls_rtn1.length();
            }
            ll_pos2 += 9;
            String ls_s2 = "000000" + of_dec_hex36(Long.valueOf(ls_rtn1.substring(start, end)).longValue());
            ls_rtn2 = ls_rtn2 + ls_s2.substring(ls_s2.length() - 6, ls_s2.length());
        }
        return ls_rtn2;
    }

}
