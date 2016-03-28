package com.codepath.apps.tweetsclient.utils;

import android.text.format.DateUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by thuypm on 26/03/2016.
 */
public class Utils {



    public static String getTimeString(String inputTime){
        //inputTime = "Tue Mar 26 21:16:23 +0000 2016";
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
        Date convertedDate = new Date();
        try {
            convertedDate = format.parse(inputTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CharSequence result = DateUtils.getRelativeTimeSpanString(convertedDate.getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        return result.toString();
    }

    public static String URLEncode(String input){
        try {
            return URLEncoder.encode(input, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String URLDecode(String input){
        try {
            return URLDecoder.decode(input, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
