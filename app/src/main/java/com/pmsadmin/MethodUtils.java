package com.pmsadmin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.pmsadmin.dashboard.model.DashBoardModelImage;
import com.pmsadmin.dashboard.model.DashboardItemsModel;
import com.pmsadmin.dialog.ErrorMessageDialog;
import com.pmsadmin.seconddashboard.adapter.ProjectsItem;
import com.pmsadmin.survey.SurveyStaticModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MethodUtils {

    public static int tender_id;

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

    public static String profileDate(String date) {
        String converted_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date0 = sdf.parse(date);
            DateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");
            converted_date = dateFormat.format(date0);
            System.out.println("Converted String: " + converted_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return converted_date;
    }

    public static String deviationTime(String date) {
        String converted_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date0 = sdf.parse(date);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm a");
            converted_date = dateFormat.format(date0);
            System.out.println("Converted String: " + converted_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return converted_date;
    }

    public static String deviationDate(String date) {
        String converted_date = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date0 = sdf.parse(date);
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            converted_date = dateFormat.format(date0);
            System.out.println("Converted String: " + converted_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return converted_date;
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
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Attendance");
            dashboardItemsModel.setImageId(R.drawable.manpower_icon);
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Plant Machinery");
            dashboardItemsModel.setImageId(R.drawable.plantmachinery_icon);
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel = new DashboardItemsModel();
            dashboardItemsModel.setItem("Projects");
            dashboardItemsModel.setImageId(R.drawable.projects_icon);
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("App Surveyors");
            dashboardItemsModel.setImageId(R.drawable.appsurveyors_icon);
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Vehicles");
            dashboardItemsModel.setImageId(R.drawable.vehicles_icon);
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Hiring");
            dashboardItemsModel.setImageId(R.drawable.hirings_icon);
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Vendors");
            dashboardItemsModel.setImageId(R.drawable.vendors_icon);
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Documentation");
            dashboardItemsModel.setImageId(R.drawable.documentation_icon);
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Approvals");
            dashboardItemsModel.setImageId(R.drawable.approvals_icon);
            list.add(dashboardItemsModel);
        }
        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Reports");
            dashboardItemsModel.setImageId(R.drawable.reports_icon);
            list.add(dashboardItemsModel);
        }

        {
            DashboardItemsModel dashboardItemsModel=new DashboardItemsModel();
            dashboardItemsModel.setItem("Planning & Reports");
            dashboardItemsModel.setImageId(R.drawable.ic_launcher_plnning_reports);
            list.add(dashboardItemsModel);
        }

        return list;
    }


    public static List<SurveyStaticModel> getItemsSurvey(){

        List<SurveyStaticModel> list = new ArrayList<>();

        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("SITE PHOTOS");
            surveyStaticModel.setImageId(R.drawable.site_photo);
            list.add(surveyStaticModel);
        }
        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("COORDINATES");
            surveyStaticModel.setImageId(R.drawable.coordinates);
            list.add(surveyStaticModel);
        }
        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("RESOURCE");
            surveyStaticModel.setImageId(R.drawable.resource);
            list.add(surveyStaticModel);
        }

        return list;
    }

    public static List<SurveyStaticModel> getResourceItems(){

        List<SurveyStaticModel> list = new ArrayList<>();

        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("MATERIAL");
            surveyStaticModel.setImageId(R.drawable.site_photo);
            list.add(surveyStaticModel);
        }
        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("ESTABLISHMENT");
            surveyStaticModel.setImageId(R.drawable.coordinates);
            list.add(surveyStaticModel);
        }
        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("HYDROLOGIAL DATA");
            surveyStaticModel.setImageId(R.drawable.resource);
            list.add(surveyStaticModel);
        }
        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("CONTRACTORS/VENDORS");
            surveyStaticModel.setImageId(R.drawable.resource);
            list.add(surveyStaticModel);
        }
        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("CONTACT DETAILS");
            surveyStaticModel.setImageId(R.drawable.resource);
            list.add(surveyStaticModel);
        }

        return list;
    }


    public static List<SurveyStaticModel> getItemCoordinates(){

        List<SurveyStaticModel> list = new ArrayList<>();

        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("CHECK IN");
            surveyStaticModel.setImageId(R.drawable.check_in);
            list.add(surveyStaticModel);
        }
        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("RAW MATERIALS");
            surveyStaticModel.setImageId(R.drawable.raw_materials);
            list.add(surveyStaticModel);
        }
        {
            SurveyStaticModel surveyStaticModel = new SurveyStaticModel();
            surveyStaticModel.setItem("CRUSHER");
            surveyStaticModel.setImageId(R.drawable.crusher);
            list.add(surveyStaticModel);
        }

        return list;
    }







    public static List<DashBoardModelImage> getItems(){
        List<DashBoardModelImage> list=new ArrayList<>();


        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("ATTENDANCE");
            dashBoardModelImage.setImageId(R.drawable.tender_icon);
            list.add(dashBoardModelImage);
        }


        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("SURVEY TENDERS");
            dashBoardModelImage.setImageId(R.drawable.pre_execution_icon);
            list.add(dashBoardModelImage);
        }

        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("UPDATE DAILY PROGRESS");
            dashBoardModelImage.setImageId(R.drawable.add_daily_data);
            list.add(dashBoardModelImage);
        }

        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("APPLY LOCAL CONVEYANCE");
            dashBoardModelImage.setImageId(R.mipmap.conveyance);
            list.add(dashBoardModelImage);
        }


        /*{
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
        }*/

        /*{
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("MORE...");
            dashBoardModelImage.setImageId(R.drawable.more_icon);
            list.add(dashBoardModelImage);
        }*/
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

    public static List<DashBoardModelImage> getSecond2DashboardItems(){
        List<DashBoardModelImage> list=new ArrayList<>();
        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("REPORT");
            dashBoardModelImage.setImageId(R.drawable.reports_icon);
            list.add(dashBoardModelImage);
        }
        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("APPROVAL");
            dashBoardModelImage.setImageId(R.drawable.approvals_icon);
            list.add(dashBoardModelImage);
        }

        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("CONVEYANCE");
            dashBoardModelImage.setImageId(R.drawable.conveyance_icon);
            list.add(dashBoardModelImage);
        }
        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("FOODING");
            dashBoardModelImage.setImageId(R.drawable.fooding_icon);
            list.add(dashBoardModelImage);
        }

        return list;

    }

    public static List<ProjectsItem> getProjectItems(){
        List<ProjectsItem> list=new ArrayList<>();
        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Matla-canning");
            list.add(dashBoardModelImage);
        }
        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Rajkharsawan-Mahalimarup");
            list.add(dashBoardModelImage);
        }

        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Kendpasi-Maluka");
            list.add(dashBoardModelImage);
        }
        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Octagon Bpo");
            list.add(dashBoardModelImage);
        }
        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Tatahitachi/Kharagpur");
            list.add(dashBoardModelImage);
        }

        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Salt lake,Kolkata");
            list.add(dashBoardModelImage);
        }

        return list;

    }

    public static List<ProjectsItem> getFilterProjectItems(){
        List<ProjectsItem> list=new ArrayList<>();
        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Matla-P0001");
            list.add(dashBoardModelImage);
        }
        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Durgapur-P0002");
            list.add(dashBoardModelImage);
        }

        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Kharagpur-P0003");
            list.add(dashBoardModelImage);
        }
        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("New Town-P0004");
            list.add(dashBoardModelImage);
        }

        return list;

    }

    public static List<ProjectsItem> getFilterDesignationItems(){
        List<ProjectsItem> list=new ArrayList<>();
        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Project Manager");
            list.add(dashBoardModelImage);
        }
        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Supervisor");
            list.add(dashBoardModelImage);
        }

        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Skilled Labour");
            list.add(dashBoardModelImage);
        }
        {
            ProjectsItem dashBoardModelImage = new ProjectsItem();
            dashBoardModelImage.setProjectName("Unskilled Labour");
            list.add(dashBoardModelImage);
        }

        return list;

    }

    public static List<DashBoardModelImage> getTenderItems(){
        List<DashBoardModelImage> list=new ArrayList<>();
        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("ADD NEW TENDER");
            dashBoardModelImage.setImageId(R.drawable.addnew_tender_icon);
            list.add(dashBoardModelImage);
        }
        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("TENDER LIST");
            dashBoardModelImage.setImageId(R.drawable.tenderlist_icon);
            list.add(dashBoardModelImage);
        }

        {
            DashBoardModelImage dashBoardModelImage = new DashBoardModelImage();
            dashBoardModelImage.setItem("ARCHIVED TENDERS");
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
