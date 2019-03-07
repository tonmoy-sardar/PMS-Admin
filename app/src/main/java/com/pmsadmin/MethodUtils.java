package com.pmsadmin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class MethodUtils {

    public static void fullScreen(Activity activity) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams winParams = window.getAttributes();
        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        window.setAttributes(winParams);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void setStickyBar(Activity activity) {

        if (activity.getActionBar() != null && activity.getActionBar().isShowing())
            activity.getActionBar().hide();

        Window window = activity.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.statusBarColor));
        }
    }

    public static Typeface getBoldFont(Activity activity){
        Typeface typefaceRegular;
        typefaceRegular = Typeface.createFromAsset(activity.getAssets(), "font/GOTHICB.TTF");
        return typefaceRegular;
    }

    public static Typeface getNormalFont(Activity activity){
        Typeface typefaceRegular;
        typefaceRegular = Typeface.createFromAsset(activity.getAssets(), "font/GOTHIC.TTF");
        return typefaceRegular;
    }

    public static Typeface getItalicBoldFont(Activity activity){
        Typeface typefaceRegular;
        typefaceRegular = Typeface.createFromAsset(activity.getAssets(), "font/GOTHICBI.TTF");
        return typefaceRegular;
    }

    public static Typeface getItalicNormalFont(Activity activity){
        Typeface typefaceRegular;
        typefaceRegular = Typeface.createFromAsset(activity.getAssets(), "font/GOTHICI.TTF");
        return typefaceRegular;
    }
}
