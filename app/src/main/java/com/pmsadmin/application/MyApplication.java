package com.pmsadmin.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.pmsadmin.attendancelist.reportlistmodel.LogDetail;

import java.util.ArrayList;
import java.util.List;


public class MyApplication extends Application {

    public static boolean onActivityState = false;
    private static MyApplication myApplication;

    public GoogleMap googleMap;

    public static MyApplication getInstance() {
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = this;

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                //activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //include this method
    }
}
