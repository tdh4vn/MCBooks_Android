package vn.mcbooks.mcbooks.utils;

import android.content.Context;

import vn.mcbooks.mcbooks.R;
import vn.mcbooks.mcbooks.model.Information;

/**
 * Created by hungtran on 6/11/16.
 */
public class StringUtils {
    public static String convertToCurencyUnitStyle(String str){
        StringBuilder stringBuilder = new StringBuilder(str);
        int count = 0;
        for (int i = stringBuilder.length() - 1; i > 0; i--){
            count++;
            if (count == 3){
                stringBuilder = stringBuilder.insert(i,'.');
                i--;
                count = 1;
            }
        }
        return stringBuilder.toString();
    }

    public static String tokenBuild(String str){
        return "Access_token " + str;
    }
    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
    public static String getShortDetail(Information information){
        String rs;
        rs = "NXB: " + information.getPublisher() + "\n" + "Tác giả: " + information.getAuthor();
        return rs;

    }
    public static String ratingLabel(int rate){
        switch(rate){
            case 1:
                return "&#xF005;&#xf006;&#xf006;&#xf006;&#xf006;";
            case 2:
                return "&#xF005;&#xf005;&#xf006;&#xf006;&#xf006;";
            case 3:
                return "&#xF005;&#xf005;&#xf005;&#xf006;&#xf006;";
            case 4:
                return "&#xF005;&#xf005;&#xf005;&#xf005;&#xf006;";
            case 5:
                return "&#xF005;&#xf005;&#xf005;&#xf005;&#xf005;";
            default:
                return "&#xF006;&#xf006;&#xf006;&#xf006;&#xf006;";
        }
    }

    public static String ratingLabel(int rate, Context mContext){
        switch(rate){
            case 1:
                return mContext.getString(R.string.star_1);
            case 2:
                return mContext.getString(R.string.star_2);
            case 3:
                return mContext.getString(R.string.star_3);
            case 4:
                return mContext.getString(R.string.star_4);
            case 5:
                return mContext.getString(R.string.star_5);
            default:
                return mContext.getString(R.string.star_0);
        }
    }
}
