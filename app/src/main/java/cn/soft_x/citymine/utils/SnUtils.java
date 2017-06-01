package cn.soft_x.citymine.utils;

import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 百度地图sn
 * Created by Administrator on 2016-11-29.
 */
public class SnUtils {

    public static final String SK = "1BtG3NAW5ZxpZBvoMOYZ9D5m3KdoV3Kg";

    public static final String AK = "EnOfvRoO51PgvqjXK6pFZ3L40AGCD5lO";

    public static String makeSn(Map<?, ?> data) {
        String sn = "";

        try {
            String paramsStr = toQueryString(data);

            String wholeStr = new String("/geosearch/v3/nearby?" + paramsStr + SK);

            String tempStr = URLEncoder.encode(wholeStr, "UTF-8");

            sn = MD5(tempStr);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Logger.i("sn->%s", sn);

        return sn;
    }


    // 对Map内所有value作utf8编码，拼接返回结果
    public static String toQueryString(Map<?, ?> data)
            throws UnsupportedEncodingException {
        StringBuffer queryString = new StringBuffer();
        for (Map.Entry<?, ?> pair : data.entrySet()) {
            queryString.append(pair.getKey() + "=");
            String pairstr = (String) pair.getValue();
//            String[] s = pairstr.split(",");
            //            if (pairstr.indexOf(",") > -1) {
            //                for (int i = 0; i < s.length; i++) {
            //                    if (i == (s.length - 1)) {
            //                        queryString.append(URLEncoder.encode(s[i], "UTF-8") + "&");
            //                    } else {
            //                        queryString.append(URLEncoder.encode(s[i], "UTF-8"));
            //                        queryString.append(",");
            //                    }
            //                }
            //
            //            }
            if (pair.getKey().equals("location")) {
                Logger.i("有个location");
                queryString.append(pair.getValue()+"&");
            } else {
                queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8") + "&");
            }
        }
        if (queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        return queryString.toString();
    }

    // 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制
    public static String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest
                    .getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100)
                        .substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

}
