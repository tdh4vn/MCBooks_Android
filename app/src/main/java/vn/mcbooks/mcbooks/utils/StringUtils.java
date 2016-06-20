package vn.mcbooks.mcbooks.utils;

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
}
