package com.sara_developers.android.newzfly;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class SessionManager {

    static SharedPreferences pref;
    static SharedPreferences.Editor editor;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "n**l%0333w3e8sm";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_USER_NAME = "user_Name";
    private static final String KEY_USER_IMG = "user_img";
    private static final String KEY_USER_AUTH = "user_auth";
    private static final String USER_EMAIL="user_emailId";
    private static final String APP_DEMONSTRATION="app_demonstration";





    public SessionManager(Context ctx){
        pref = ctx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public boolean createSellerLoginSession(String userName,String user_authId,String userEmailId) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_USER_NAME, userName);
        editor.putString(KEY_USER_AUTH, user_authId);
        editor.putString(USER_EMAIL,userEmailId);
        editor.commit();
        return true;
    }
    public boolean checkLogin(Activity ctx){
        // Check login status
        if(!this.isLoggedIn()){
            Intent i = new Intent(ctx, SignInScreen.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            ctx.startActivity(i);
            ctx.finish();
            return false;
        }else{
            return true;
        }
    }


    public boolean SetUserName(String userName){

        editor.putString(KEY_USER_NAME,userName);
        editor.commit();
        return true;

    }

    public boolean SetUserImageLink(String userImg){

        editor.putString(KEY_USER_IMG,userImg);
        editor.commit();
        return true;

    }
    public boolean DemonstrationDone(){
        editor.putBoolean(APP_DEMONSTRATION,true);
        editor.commit();
        return true;
    }
    public static boolean getDemonstrationStatus(){
        return pref.getBoolean(APP_DEMONSTRATION,false);
    }
    public static String getUserEmail() {

        return pref.getString(USER_EMAIL,null);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    public static String getKeyUserAuth() {
        return pref.getString(KEY_USER_AUTH,null);
    }

    public static String getKeyUserImg() {
        return pref.getString(KEY_USER_IMG,null);
    }

    public static String getKeyUserName() {
        return pref.getString(KEY_USER_NAME,null);
    }

}
