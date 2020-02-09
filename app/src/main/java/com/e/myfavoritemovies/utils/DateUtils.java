package com.e.myfavoritemovies.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date utility class
 */
public class DateUtils {

    /**
     *
     * @param dateString
     * @return
     */
    public static String formatDate(String dateString) {
        Date d=null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            d = sdf.parse(dateString);
        } catch(ParseException ex){
            Log.v("Exception", ex.getLocalizedMessage());
        }

        sdf.applyPattern("MMM dd yyyy");
        System.out.println(sdf.format(d));

        return sdf.format(d);
   }
}
