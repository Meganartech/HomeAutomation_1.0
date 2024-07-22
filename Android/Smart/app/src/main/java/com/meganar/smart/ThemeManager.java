package com.meganar.smart;

import android.content.Context;

import com.example.smart.R;

public class ThemeManager   {

    public static void setCustomizedThemes(Context context, String theme){
        switch (theme){
            case "blue":
                context.setTheme(R.style.AppTheme);
                break;
            case "green":
                context.setTheme(R.style.Theme1);
                break;
            case "red":
                context.setTheme(R.style.Theme2);
                break;
            case "purple":
                context.setTheme(R.style.Theme3);
                break;
            case "green2":
                context.setTheme(R.style.Theme4);
                break;
            case "grey":
                context.setTheme(R.style.Theme5);
                break;
            case "orange":
                context.setTheme(R.style.Theme6);
                break;
            case "pink":
                context.setTheme(R.style.Theme7);
                break;
        }
    }
}
