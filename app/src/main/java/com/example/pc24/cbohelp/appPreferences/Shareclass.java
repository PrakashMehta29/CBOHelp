package com.example.pc24.cbohelp.appPreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pc24 on 09/12/2016.
 */

public class Shareclass {
    public static final String PREFS_NAME = "CBOhelp";
    public static final String PREFS_KEY = "AOP_PREFS_String";

    public Shareclass() {
        super();
    }
    public void save(Context context, String key, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString(key, text); //3
        editor.commit(); //4
    }

    public String getValue(Context context,String key,String Default) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(key, Default);
        return text;
    }
    public void clearSharedPreference(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeValue(Context context,String key) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(key);
        editor.commit();
    }

}
