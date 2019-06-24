package com.pmsadmin.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.dashboard.LogoutService;
import com.pmsadmin.dialog.ErrorMessageDialog;
import com.pmsadmin.dialog.ForgotPasswordDialog;
import com.pmsadmin.giveattandence.addattandencemodel.AttendanceAddModel;
import com.pmsadmin.giveattandence.services.BackgroundLocationService;
import com.pmsadmin.login.model.LoginModel;
import com.pmsadmin.netconnection.ConnectionDetector;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_copyright, tv_tender, tv_forgot;
    EditText et_login, et_password;
    Button btn_login;
    private LoadingData loader;

    private SimpleLocation location;

    private double currentLat;
    private double currentLng;
    public List<Address> addresses;
    Geocoder geocoder;


    private static final int RC_LOCATION_CONTACTS_PERM = 126;
    private static final String[] LOCATION_AND_CONTACTS =
            {Manifest.permission.ACCESS_FINE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MethodUtils.setStickyBar(LoginActivity.this);
        loader = new LoadingData(LoginActivity.this);

        geocoder = new Geocoder(LoginActivity.this, Locale.getDefault());

        viewBind();
        clickEvent();
        setFont();
          //et_login.setText("mangal.das@shyamfuture.com");
            // et_password.setText("mangal@123");

       // et_login.setText("admin");//local
       // et_password.setText("Shyam2019");
        /*et_login.setText("santanu.pal@shyamfuture.com");
        et_password.setText("hvNzeqhkTR");*/
        et_login.setText("");
        et_password.setText("");
        /*et_login.setText("admin");
        et_password.setText("admin");*/


        /*---------------------------- Arghya--------------------------------------*/


        location = new SimpleLocation(this, false, false, 10000);

        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        /*---------------------------- Arghya--------------------------------------*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (location.hasLocationEnabled()) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);


            } else {
                location.beginUpdates();
            }


        }
    }


    private void clickEvent() {
        btn_login.setOnClickListener(this);
        tv_copyright.setOnClickListener(this);
        tv_forgot.setOnClickListener(this);
    }

    private void setValidation() {

        if (location.hasLocationEnabled()) {


            if (et_login.getText().toString().equals("")) {
                errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.enter_email));
            } else if (et_password.getText().toString().equals("")) {
                errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.enter_password));
            } else if (!ConnectionDetector.isConnectingToInternet(LoginActivity.this)) {
                errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.no_internet));
            } else {

                callLoginApi();


            }

        } else {
            SimpleLocation.openSettings(this);
        }
    }






    private void callLoginApi() {


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


            try {
                addresses = geocoder.getFromLocation(currentLat, currentLng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("username", et_login.getText().toString().trim());
        object.addProperty("password", et_password.getText().toString().trim());
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_loginApi("application/json",object);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    //String responseString = response.body().string();
                    if (response.code() == 200) {
                        String responseString = response.body().string();
                        Log.d("responsestring",responseString);
                        Gson gson = new Gson();
                        LoginModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            LoginShared.setLoginToken(LoginActivity.this, jsonObject.optString("token"));
                            loginModel = gson.fromJson(responseString, LoginModel.class);
                            LoginShared.setLoginDataModel(LoginActivity.this, loginModel);

                            if (LoginShared.getAttendanceFirstLoginTime(LoginActivity.this).equals("1")) {
                                //MethodUtils.errorMsg(DashBoardActivity.this,"You are already Logged in");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    // perform Opertaion
                                    startForegroundService(new Intent(LoginActivity.this, BackgroundLocationService.class));
                                } else {
                                    startService(new Intent(LoginActivity.this, BackgroundLocationService.class));
                                }

                                navigateToDashBoard();
                            } else {
                                addAttendance();
                            }
                            //navigateToHome();
                        } else if (jsonObject.optInt("request_status") == 0) {
                            errorMsg(LoginActivity.this, jsonObject.optString("msg"));
                            et_login.setText("");
                            et_password.setText("");
                        } else {
                            errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        et_login.setText("");
                        et_password.setText("");
                        errorMsg(LoginActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {

                    System.out.println("exception: "+e.toString());
                    et_login.setText("");
                    et_password.setText("");
                    errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                et_login.setText("");
                et_password.setText("");
                errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred));
            }
        });
    }

    private void addAttendance() {
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("type", 1);
        //object.addProperty("employee", LoginShared.getLoginDataModel(DashBoardActivity.this).getUserId());
        object.addProperty("date", getTodaysDate() + "T" + getCurrentTimeUsingDate());
        object.addProperty("login_time", getTodaysDate() + "T" + getCurrentTimeUsingDate());
        //object.addProperty("login_latitude", gpsTracker.getLatitude());
        object.addProperty("login_latitude", currentLat);
        //object.addProperty("login_longitude", gpsTracker.getLongitude());
        object.addProperty("login_longitude", currentLng);
        //object.addProperty("user_project_id", 1);
        if(addresses!=null) {
            if (addresses.size() > 0) {
                object.addProperty("login_address", addresses.get(0).getLocality() + "," + addresses.get(0).getAdminArea());
            } else {
                object.addProperty("login_address", "");
            }
        }else{
            object.addProperty("login_address", "");
        }
        //object.addProperty("justification", "");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_attendanceAddApi("Token "
                        + LoginShared.getLoginDataModel(LoginActivity.this).getToken(),
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
                            loginModel = gson.fromJson(responseString, AttendanceAddModel.class);
                            LoginShared.setAttendanceAddDataModel(LoginActivity.this, loginModel);
                            LoginShared.setAttendanceFirstLoginTime(LoginActivity.this, "1");
                            //MethodUtils.errorMsg(DashBoardActivity.this, jsonObject.optString("msg"));
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                startForegroundService(new Intent(LoginActivity.this, BackgroundLocationService.class));
                            }else{
                                startService(new Intent(LoginActivity.this, BackgroundLocationService.class));
                            }

                            navigateToDashBoard();
                            /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                                startForegroundService(new Intent(LoginActivity.this, LogoutService.class));
                            }else{
                                startService(new Intent(LoginActivity.this, LogoutService.class));
                            }*/
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(LoginActivity.this, jsonObject.optString("msg"));
                            //navigateToLogin();
                        } else {
                            MethodUtils.errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred));
                            //navigateToLogin();
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(LoginActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(LoginActivity.this, LoginActivity.this.getString(R.string.error_occurred));
            }
        });
    }

    private void navigateToDashBoard() {

        Intent profileIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
        startActivity(profileIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void navigateToHome() {
        //Toast.makeText(LoginActivity.this,LoginShared.getLoginDataModel(LoginActivity.this).getToken().toString(), Toast.LENGTH_LONG).show();
        Intent profileIntent = new Intent(LoginActivity.this, DashBoardActivity.class);
        startActivity(profileIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void setFont() {
        tv_copyright.setLinkTextColor(getResources().getColor(R.color.link_color));
        tv_copyright.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        tv_tender.setTypeface(MethodUtils.getBoldFont(LoginActivity.this));
        tv_forgot.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        et_login.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        et_password.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        btn_login.setTypeface(MethodUtils.getBoldFont(LoginActivity.this));
    }

    private void viewBind() {
        tv_copyright = findViewById(R.id.tv_copyright);
        tv_tender = findViewById(R.id.tv_tender);
        tv_forgot = findViewById(R.id.tv_forgot);
        et_login = findViewById(R.id.et_login);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
    }

    public void errorMsg(Context context, String msg) {
        ErrorMessageDialog.getInstant(context).show(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                setValidation();
                break;
            case R.id.tv_copyright:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://shyamsteel.com/"));
                startActivity(browserIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.tv_forgot:
                new ForgotPasswordDialog(LoginActivity.this).show();
                break;
        }
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
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
