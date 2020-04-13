package com.example.fleamarket.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 教务系统请求参数工具类
 *
 * @author zhangwade
 */
public class RequestParamUtils {
    private static Map<Integer, Character> dec_hex36Map;

    /**
     * 参数加密函数
     *
     * @param as_str 明文
     * @param as_key 密钥
     * @return 密文
     */
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

    public static String of_dec_hex36(long al_in) {
        initDec_hex36Map();
        String ls_hex = "";
        if (al_in < 0) {
            return "-" + of_dec_hex36(Math.abs(al_in));
        }
        do {
            String current_hex = dec_hex36Map.get(Integer.valueOf((int) (al_in % 36))).toString();
            if ("".equals(ls_hex)) {
                ls_hex = current_hex;
            } else {
                ls_hex = current_hex + ls_hex;
            }
            al_in /= 36;
        } while (al_in > 0);
        return ls_hex;
    }

    private static void initDec_hex36Map() {
        if (dec_hex36Map == null) {
            dec_hex36Map = new HashMap();
        }
        for (int i = 0; i < 10; i++) {
            dec_hex36Map.put(Integer.valueOf(i), Character.valueOf((char) (i + 48)));
        }
        for (int j = 0; j < 26; j++) {
            dec_hex36Map.put(Integer.valueOf(j + 10), Character.valueOf((char) (j + 97)));
        }
    }

    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }
}