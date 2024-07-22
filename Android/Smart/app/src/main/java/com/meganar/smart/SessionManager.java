package com.meganar.smart;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionManager {
    private static final String TAG = SessionManager.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
   private static final  String PREF_NAME="androidHiveLogin";
   private  static  final String KEY_IS_LOGGED_IN = "isLoggedIn";


    public SessionManager(Context context){
        this._context =context;
        pref =_context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor =pref.edit();
    }
    public  void setLogin(boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGED_IN,isLoggedIn);
        editor.commit();
        Log.d(TAG,"User login session Modified");
    }
    public boolean isLoggedIn(){

        return
                pref.getBoolean(KEY_IS_LOGGED_IN,false);
    }
    public void logout(){
        editor.clear();
        editor.apply();
        editor.commit();
    }

    public static void setThemeColor(Context context, String themeColor){
        SharedPreferences sharedpreferences = context.getSharedPreferences("theme_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("theme", themeColor);
        editor.apply();
    }
    public static String getThemeColor(Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences("theme_data", Context.MODE_PRIVATE);
        return sharedpreferences.getString("theme", "green");
    }

}