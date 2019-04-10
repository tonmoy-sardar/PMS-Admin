package com.pmsadmin.dashboard;

import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.pmsadmin.utils.GeneralToApp;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    TextView tv_attendance,tv_dashboard;
    public TextView tv_universal_header;
    private LoadingData loader;
    public GPSTracker gpsTracker;
    public List<Address> addresses;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        loader = new LoadingData(BaseActivity.this);
        viewBind();
        gpsTracker = new GPSTracker(BaseActivity.this);
        geocoder = new Geocoder(BaseActivity.this, Locale.getDefault());
        clickEvent();
        setFont();
        initializeDrawer();
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
                logoutApi();
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

        final Call<ResponseBody> logout =apiInterface.call_logoutApi("Token "
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
                }catch (Exception e){
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
        if(addresses!=null) {
            if (addresses.size() > 0) {
                object.addProperty("logout_address", addresses.get(0).getLocality() + "," + addresses.get(0).getAdminArea());
            } else {
                object.addProperty("logout_address", "");
            }
        }else{
            object.addProperty("logout_address", "");
        }
        object.addProperty("approved_status",4);
        object.addProperty("justification","");
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
                    if (response.code() == 201 || response.code()==200) {
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
        stopService(new Intent(BaseActivity.this,BackgroundLocationService.class));
        BackgroundLocationService.stoplocationservice();
        Intent logIntent = new Intent(BaseActivity.this, LoginActivity.class);
        startActivity(logIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
