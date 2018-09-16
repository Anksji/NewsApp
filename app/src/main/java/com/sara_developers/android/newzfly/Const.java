package com.sara_developers.android.newzfly;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ankit on 9/15/2017.
 */

public class Const {

    public static final int PICK_FROM_GALLERY = 1;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name

    public static String[] languageISO = {"en","hi"};
    public static String PrivateShairedPref="@In@ews#)f#l$y";

    public static void setLanguage(String language, Context context){
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(PrivateShairedPref, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("language",language);
        editor.commit();
    }


    public static String  GetLanguageFromSharedPref(Context context){
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(PrivateShairedPref, MODE_PRIVATE);
        String language= pref.getString("language","English");  // getting boolean
        return language;
    }

    public static void NEWS_APP_setLocale(Context ctx, String languageISO){
        Configuration cfg = new Configuration();

        if (!languageISO.isEmpty()) {
            cfg.locale = new Locale(languageISO);
        } else {
            cfg.locale = Locale.getDefault();
        }

        Locale.setDefault(cfg.locale);
        ctx.getResources().updateConfiguration(cfg, null);
    }

}
