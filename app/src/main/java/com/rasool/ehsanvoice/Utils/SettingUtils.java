package com.rasool.ehsanvoice.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import com.rasool.ehsanvoice.Constants;
import static com.rasool.ehsanvoice.Constants.TEXT_SIZE;

public class SettingUtils  {

    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    public static void setSize(final Activity activity,int size) {
        pref = activity.getApplicationContext().getSharedPreferences(Constants.SESSION_PREF, Constants.PRIVATE_MODE);
        editor = pref.edit();
        editor.putInt(TEXT_SIZE,size);
        editor.apply();
    }

    public static int getSize(final Activity activity) {
        pref = activity.getApplicationContext().getSharedPreferences(Constants.SESSION_PREF, Constants.PRIVATE_MODE);
        return pref.getInt(TEXT_SIZE, 16);
    }



}
