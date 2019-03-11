package com.pmsadmin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pmsadmin.dashboard.model.DashBoardModelImage;
import com.pmsadmin.dashboard.model.DashboardItemsModel;
import com.pmsadmin.dialog.ErrorMessageDialog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


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

    public static void errorMsg(Context context, String msg) {
        ErrorMessageDialog.getInstant(context).show(msg);
    }

    public static boolean isValidUrl(String url) {
        // if(url.contains("http")){
        if (android.util.Patterns.WEB_URL.matcher(url).matches()) {
            return true;
        } else {
            return false;
        }
        // }else{
        //   if(android.util.Patterns.WEB_URL.matcher("https://"+url).matches()){
        //      return true;
        //  }else{
        //       return false;
        //   }
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

    public static List<DashboardItemsModel>  addDataDashboard() {
        List<DashboardItemsModel> list=new ArrayList<>();
        {
            DashboardItemsModel dashboardItemsModel = new DashboardItemsModel();
            dashboardItemsModel.setItem("Projects");
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Manpower");
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("App Surveyors");
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Plant Machinery");
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Vehicles");
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Hiring");
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Vendors");
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Documentation");
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Approvals");
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Reports");
            list.add(dashboardItemsModel);
        }

        return list;
    }

    public static List<DashBoardModelImage> getItems(){
        List<DashBoardModelImage> list=new ArrayList<>();
        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("TENDER");
            dashBoardModelImage.setImageId(R.drawable.tender_icon);
            list.add(dashBoardModelImage);
        }
        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("PRE EXECUTION");
            dashBoardModelImage.setImageId(R.drawable.pre_execution_icon);
            list.add(dashBoardModelImage);
        }

        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("EXECUTION");
            dashBoardModelImage.setImageId(R.drawable.execution_icon);
            list.add(dashBoardModelImage);
        }

        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("POST EXECUTION");
            dashBoardModelImage.setImageId(R.drawable.post_execution_icon);
            list.add(dashBoardModelImage);
        }

        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("MORE...");
            dashBoardModelImage.setImageId(R.drawable.more_icon);
            list.add(dashBoardModelImage);
        }
        return list;

    }

    public static List<DashBoardModelImage> getSecondDashboardItems(){
        List<DashBoardModelImage> list=new ArrayList<>();
        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("TENDER LIST");
            dashBoardModelImage.setImageId(R.drawable.tenderlist_icon);
            list.add(dashBoardModelImage);
        }
        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("ARCHIVED TENDER ");
            dashBoardModelImage.setImageId(R.drawable.archivedtenders_icon);
            list.add(dashBoardModelImage);
        }

        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("APP SURVEYORS");
            dashBoardModelImage.setImageId(R.drawable.appsurveyors_icon);
            list.add(dashBoardModelImage);
        }

        return list;

    }
}
