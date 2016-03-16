package com.pmt.utils;

import android.text.format.DateUtils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 13/03/2016.
 */
public class Utils {



    public static String getLikesString(int likes){
        String result = "♥";
//        
        DecimalFormat df = new DecimalFormat("#,##0");
        result += " " +  df.format((long)likes);
        if(likes>1){
            result += " likes";
        }else{
            result += " like";
        }
        return result;
    }

    public static String getTimeString(long createTime){


//        String result = "";
//        long curTime = System.currentTimeMillis()/1000;
//        long time = curTime - createTime;
//        if(time > 0){
//            if(time <= 60){
//                result += time + "s";
//            }else{
//                time /= 60;
//                if(time <= 60){
//                    result += time + "m";
//                }else{
//                    time /= 60;
//                    if(time <= 24){
//                        result += time + "h";
//                    }else{
//                        time /= 24;
//                        if(time <= 7){
//                            result += time + "d";
//                        }else{
//                            time /= 7;
//                            result += time + "w";
//                        }
//                    }
//                }
//            }
//        }
        CharSequence result = DateUtils.getRelativeTimeSpanString(createTime*1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        return result.toString();
    }


}
