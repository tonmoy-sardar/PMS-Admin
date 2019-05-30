package com.pmsadmin.dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.forgot.ForgotPasswordActivity;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.giveattandence.addattandencemodel.AttendanceAddModel;
import com.pmsadmin.giveattandence.services.BackgroundLocationService;
import com.pmsadmin.location.GPSTracker;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.splash_screen.SplashActivity;
import com.pmsadmin.utils.GeneralToApp;
import com.pmsadmin.utils.MarshMallowPermissions;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout base_rl_contentview;
    private ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    public ImageButton img_topbar_menu;
    ImageView iv_cross;
    public ImageView iv_close;
    TextView tv_user_name;
    TextView tv_tender_list;
    TextView tv_help;
    TextView tv_logout;
    TextView tv_attendance, tv_dashboard;
    public TextView tv_universal_header;
    private LoadingData loader;
    public GPSTracker gpsTracker;
    public List<Address> addresses;

    public static List<Address> addressPeriodic;
    Geocoder geocoder;

    private double currentLat;
    private double currentLng;

    private SimpleLocation location;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 0x2;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public static double latPeriodic;
    public static double lonperiodic;

    public static String loginTimePeriodic = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        loader = new LoadingData(BaseActivity.this);

        /*final MarshMallowPermissions mmPermission = new MarshMallowPermissions(BaseActivity.this);
        if (mmPermission.isAllGpsPermissionAllowed()) {
            //buildAlertMessageNoGps();
        }*/
        //checkLocationPermission();

        viewBind();
        gpsTracker = new GPSTracker(BaseActivity.this);
        geocoder = new Geocoder(BaseActivity.this, Locale.getDefault());

        /*---------------------------- Arghya--------------------------------------*/


        location = new SimpleLocation(this, false, false, 10000);

        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);


        }



        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));






        if (LoginShared.getAttendanceFirstLoginTime(BaseActivity.this).equals("1")){


            location.setListener(new SimpleLocation.Listener() {

                public void onPositionChanged() {
                    // new location data has been received and can be accessed

                    latPeriodic = location.getLatitude();
                    lonperiodic = location.getLongitude();

                }

            });

            if (latPeriodic == 0.0 && lonperiodic == 0.0) {
                int i = 0;
                while (i < 5) {
                    latPeriodic = location.getLatitude();
                    lonperiodic = location.getLongitude();
                    if (latPeriodic == 0.0 && lonperiodic == 0.0) {
                        i++;
                    } else {
                        break;
                    }
                }
            }

            try {
                addressPeriodic = geocoder.getFromLocation(latPeriodic, lonperiodic, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*PeriodicWorkRequest.Builder myWorkBuilder =
                    new PeriodicWorkRequest.Builder(MyWorker.class, 15, TimeUnit.MINUTES);
            PeriodicWorkRequest myWork = myWorkBuilder.build();

            WorkManager.getInstance().enqueue(myWork);*/

            /*WorkManager.getInstance()
                    .enqueueUniquePeriodicWork("jobTag", ExistingPeriodicWorkPolicy.KEEP, myWork);*/



        }

        /*---------------------------- Arghya--------------------------------------*/


        clickEvent();
        setFont();
        initializeDrawer();
    }




    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
        }
    };



    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }




    @Override
    protected void onResume() {
        super.onResume();

        location.beginUpdates();
    }

    private void setFont() {
        tv_user_name.setTypeface(MethodUtils.getBoldFont(BaseActivity.this));
        tv_tender_list.setTypeface(MethodUtils.getNormalFont(BaseActivity.this));
        tv_help.setTypeface(MethodUtils.getNormalFont(BaseActivity.this));
        tv_logout.setTypeface(MethodUtils.getNormalFont(BaseActivity.this));
        tv_attendance.setTypeface(MethodUtils.getNormalFont(BaseActivity.this));
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(BaseActivity.this));
    }

    private void viewBind() {
        base_rl_contentview = findViewById(R.id.base_rl_contentview);
        img_topbar_menu = findViewById(R.id.img_topbar_menu);
        iv_cross = findViewById(R.id.iv_cross);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_tender_list = findViewById(R.id.tv_tender_list);
        tv_help = findViewById(R.id.tv_help);
        tv_logout = findViewById(R.id.tv_logout);
        tv_attendance = findViewById(R.id.tv_attendance);
        tv_universal_header = findViewById(R.id.tv_universal_header);
        iv_close = findViewById(R.id.iv_close);
        tv_dashboard = findViewById(R.id.tv_dashboard);

        //tv_user_name.setText("");

        tv_user_name.setText(LoginShared.getLoginDataModel(BaseActivity.this).getEmail());

    }

    private void clickEvent() {
        img_topbar_menu.setOnClickListener(this);
        iv_cross.setOnClickListener(this);
        tv_user_name.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        tv_attendance.setOnClickListener(this);
        tv_dashboard.setOnClickListener(this);
    }

    public void addContentView(View view) {
        base_rl_contentview.removeAllViews();
        base_rl_contentview.addView(view,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * Initialize side menu
     */
    private void initializeDrawer() {
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name
        ) {
            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        initializeDrawerToggle(mDrawerToggle);
    }

    public void initializeDrawerToggle(ActionBarDrawerToggle mDrawerToggle) {
        this.mDrawerToggle = mDrawerToggle;
    }

    /**
     * To check whether the side menu is open or not
     */
    private boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_topbar_menu:
                if (isDrawerOpen())
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.iv_cross:
                if (isDrawerOpen()) {
                    mDrawerLayout.closeDrawers();
                }
                break;
            case R.id.tv_user_name:
                Intent profileIntent = new Intent(BaseActivity.this, ForgotPasswordActivity.class);
                startActivity(profileIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_logout:
                //logoutApi();

                mDrawerLayout.closeDrawer(Gravity.LEFT);
                logoutApiArghya();


                break;
            case R.id.tv_attendance:
                if (isDrawerOpen()) {
                    mDrawerLayout.closeDrawers();
                }
                Intent logIntent = new Intent(BaseActivity.this, GiveAttendanceActivity.class);
                startActivity(logIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_dashboard:
                if (isDrawerOpen()) {
                    mDrawerLayout.closeDrawers();
                }
                Intent intent = new Intent(BaseActivity.this, DashBoardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }


    private void logout() {
        if (isDrawerOpen()) {
            mDrawerLayout.closeDrawers();
        }
        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> logout = apiInterface.call_logoutApi("Token "
                + LoginShared.getLoginToken(BaseActivity.this));
        logout.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();

                try {
                    if (response.code() == 200) {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            MethodUtils.errorMsg(BaseActivity.this, jsonObject.optString("msg"));
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    navigateToLogin();
                                }
                            }, GeneralToApp.SPLASH_WAIT_TIME);
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(BaseActivity.this, jsonObject.optString("msg"));
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    navigateToLogin();
                                }
                            }, GeneralToApp.SPLASH_WAIT_TIME);
                        } else {
                            MethodUtils.errorMsg(BaseActivity.this, getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(BaseActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(BaseActivity.this, getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(BaseActivity.this, getString(R.string.error_occurred));
            }
        });

    }

    private String getTodaysDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }


    private void logoutApiArghya() {

        location.setListener(new SimpleLocation.Listener() {

            public void onPositionChanged() {
                // new location data has been received and can be accessed

                currentLat = location.getLatitude();
                currentLng = location.getLongitude();

            }

        });

        if (currentLat == 0.0 && currentLng == 0.0) {
            int i = 0;
            while (i < 5) {
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();
                if (currentLat == 0.0 && currentLng == 0.0) {
                    i++;
                } else {
                    break;
                }
            }
        }

        try {
            addresses = geocoder.getFromLocation(currentLat, currentLng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("loginTime: " + LoginShared.getAttendanceAddDataModel(BaseActivity.this)
                .getResult().getLoginTime().toString());

        String loginTime = LoginShared.getAttendanceAddDataModel(BaseActivity.this)
                .getResult().getLoginTime().toString();


        String[] separated = loginTime.split("T");

                /*String sep1 = separated[0];
                String sep2 = separated[1];*/
        //attendanceViewHolder.tvLoginValue.setText(separated[1]);

        String loginTimeFormat = separated[1];

        loginTimePeriodic = separated[1];


        //SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");


        try
        {

            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
            Date date1 = format.parse(loginTimeFormat);
            Date date2 = format.parse(getCurrentTimeUsingDate());
            long mills = date2.getTime() - date1.getTime();
            Log.v("Data1", ""+date1.getTime());
            Log.v("Data2", ""+date2.getTime());
            int hours = (int) (mills/(1000 * 60 * 60));
            int mins = (int) (mills/(1000*60)) % 60;

            String diff = hours + ":" + mins; // updated value every1 second

            Toast.makeText(getApplicationContext(), diff, Toast.LENGTH_SHORT);

            System.out.println("difference: "+diff);


            if (hours < 10) {


                callAttandance_editApi();
            } else {
                callAttendandanceLogout();
            }


            //txtCurrentTime.setText(diff);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


       /* try {


            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            Date startDate = simpleDateFormat.parse(loginTimeFormat);
            Date endDate = simpleDateFormat.parse(getCurrentTimeUsingDate());

            long difference = endDate.getTime() - startDate.getTime();
            if (difference < 0) {
                Date dateMax = simpleDateFormat.parse("24:00");
                Date dateMin = simpleDateFormat.parse("00:00");
                difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
            }
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            Log.i("log_tag", "Hours: " + hours + ", Mins: " + min);


            if (hours < 10) {

                callAttandance_editApi();
            } else {
                callAttendandanceLogout();
            }



            *//*String.valueOf("date1: "+ date1.toString()+" "+date2.toString());

            long mills = date1.getTime() - date2.getTime();
            int hours = (int) (mills/(1000 * 60 * 60));
            int mins = (int) ((mills/(1000*60)) % 60);

            String diff = String.valueOf(hours) + ":" + String.valueOf(mins);

            System.out.println("DiffTen: " + diff);*//*

        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        JsonObject object = new JsonObject();
        object.addProperty("logout_time", getTodaysDate() + "T" + getCurrentTimeUsingDate());
        object.addProperty("logout_latitude", String.valueOf(currentLat));
        object.addProperty("logout_longitude", String.valueOf(currentLng));
        if (addresses != null) {
            if (addresses.size() > 0) {
                object.addProperty("logout_address", addresses.get(0).getLocality() + "," + addresses.get(0).getAdminArea());
            } else {
                object.addProperty("logout_address", "");
            }
        } else {
            object.addProperty("logout_address", "");
        }
        object.addProperty("approved_status", 4);
        object.addProperty("justification", "");

        //System.out.println("logoutObject: "+object.toString());


    }

    private void callAttendandanceLogout() {


        location.setListener(new SimpleLocation.Listener() {

            public void onPositionChanged() {
                // new location data has been received and can be accessed

                currentLat = location.getLatitude();
                currentLng = location.getLongitude();

            }

        });

        if (currentLat == 0.0 && currentLng == 0.0) {
            int i = 0;
            while (i < 5) {
                currentLat = location.getLatitude();
                currentLng = location.getLongitude();
                if (currentLat == 0.0 && currentLng == 0.0) {
                    i++;
                } else {
                    break;
                }
            }
        }

        try {
            addresses = geocoder.getFromLocation(currentLat, currentLng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        JsonObject object = new JsonObject();
        object.addProperty("logout_time", getTodaysDate() + "T" + getCurrentTimeUsingDate());
        object.addProperty("logout_latitude", String.valueOf(currentLat));
        object.addProperty("logout_longitude", String.valueOf(currentLng));
        if (addresses != null) {
            if (addresses.size() > 0) {
                object.addProperty("logout_address", addresses.get(0).getLocality() + "," + addresses.get(0).getAdminArea());
            } else {
                object.addProperty("logout_address", "");
            }
        } else {
            object.addProperty("logout_address", "");
        }


        System.out.println("objectLogout: " + object.toString());


        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        final Call<ResponseBody> register = apiInterface.callLogoutApi("Token "
                        + LoginShared.getLoginDataModel(BaseActivity.this).getToken(),
                LoginShared.getAttendanceAddDataModel(BaseActivity.this).getResult().getId().toString(),
                object);


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {

                    try {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optString("msg").equals("Success")) {

                            navigateToLogin();
                            /*new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    navigateToLogin();
                                }
                            }, GeneralToApp.SPLASH_WAIT_TIME);*/
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void callAttandance_editApi() {

        Intent myintent = new Intent(getApplicationContext(), LogoutDialogue.class);
        startActivity(myintent);
    }


    private void logoutApi() {
        try {
            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("logout_time", getTodaysDate() + "T" + getCurrentTimeUsingDate());
        object.addProperty("logout_latitude", gpsTracker.getLatitude());
        object.addProperty("logout_longitude", gpsTracker.getLongitude());
        if (addresses != null) {
            if (addresses.size() > 0) {
                object.addProperty("logout_address", addresses.get(0).getLocality() + "," + addresses.get(0).getAdminArea());
            } else {
                object.addProperty("logout_address", "");
            }
        } else {
            object.addProperty("logout_address", "");
        }
        object.addProperty("approved_status", 4);
        object.addProperty("justification", "");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_attendanceLogoutApi("Token "
                        + LoginShared.getLoginDataModel(BaseActivity.this).getToken(),
                LoginShared.getAttendanceAddDataModel(BaseActivity.this).getResult().getId().toString(),
                object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 201 || response.code() == 200) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        AttendanceAddModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            MethodUtils.errorMsg(BaseActivity.this, jsonObject.optString("msg"));
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    navigateToLogin();
                                }
                            }, GeneralToApp.SPLASH_WAIT_TIME);
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(BaseActivity.this, jsonObject.optString("msg"));
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    navigateToLogin();
                                }
                            }, GeneralToApp.SPLASH_WAIT_TIME);
                        } else {
                            MethodUtils.errorMsg(BaseActivity.this, getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(BaseActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(BaseActivity.this, BaseActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(BaseActivity.this, BaseActivity.this.getString(R.string.error_occurred));
            }
        });

    }

    private void navigateToLogin() {
        LoginShared.destroySessionTypePreference();
        stopService(new Intent(BaseActivity.this, BackgroundLocationService.class));
        BackgroundLocationService.stoplocationservice();
        Intent logIntent = new Intent(BaseActivity.this, LoginActivity.class);
        startActivity(logIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
