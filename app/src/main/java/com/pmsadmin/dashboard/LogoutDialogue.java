package com.pmsadmin.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.giveattandence.addattandencemodel.AttendanceAddModel;
import com.pmsadmin.giveattandence.services.BackgroundLocationService;
import com.pmsadmin.location.GPSTracker;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.GeneralToApp;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LogoutDialogue extends Activity {

    private EditText etJustification;
    private Button btSubmit;



    Geocoder geocoder;

    private double currentLat;
    private double currentLng;

    private SimpleLocation location;
    public GPSTracker gpsTracker;
    public List<Address> addresses;

    private LoadingData loader;

    private ImageView ivClose;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_dialogue);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        loader = new LoadingData(LogoutDialogue.this);

        gpsTracker = new GPSTracker(LogoutDialogue.this);
        geocoder = new Geocoder(LogoutDialogue.this, Locale.getDefault());

        /*---------------------------- Arghya--------------------------------------*/


        location = new SimpleLocation(this, false, false, 10000);

        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        /*---------------------------- Arghya--------------------------------------*/

        initLayout();
    }


    @Override
    protected void onResume() {
        super.onResume();

        location.beginUpdates();
    }

    private void initLayout() {

        etJustification = (EditText) findViewById(R.id.etJustification);
        btSubmit = (Button) findViewById(R.id.btSubmit);

        ivClose = (ImageView) findViewById(R.id.ivClose);


        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etJustification.getText().toString().trim().equals("")) {
                    MethodUtils.errorMsg(LogoutDialogue.this, "you have attempted to log out before 10 hours!");
                }else {
                    logoutApiArghya();
                }
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

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


        JsonObject object = new JsonObject();
        /*object.addProperty("logout_time", getTodaysDate() + "T" + getCurrentTimeUsingDate());
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
        }*/
        object.addProperty("approved_status", 4);
        object.addProperty("justification", etJustification.getText().toString().trim());

        System.out.println("logoutObject: " + object.toString()+ ": "
                + LoginShared.getAttendanceAddDataModel(LogoutDialogue.this).getResult().getId().toString());


        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        final Call<ResponseBody> register = apiInterface.call_attendanceLogoutApi("Token "
                        + LoginShared.getLoginDataModel(LogoutDialogue.this).getToken(),
                LoginShared.getAttendanceAddDataModel(LogoutDialogue.this).getResult().getId().toString(),
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
                            MethodUtils.errorMsg(LogoutDialogue.this, jsonObject.optString("msg"));

                            callLogoutApi();
                            /*new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    navigateToLogin();
                                }
                            }, GeneralToApp.SPLASH_WAIT_TIME);*/
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(LogoutDialogue.this, jsonObject.optString("msg"));
                            new android.os.Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    callLogoutApi();
                                }
                            }, GeneralToApp.SPLASH_WAIT_TIME);
                        } else {
                            MethodUtils.errorMsg(LogoutDialogue.this, getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(LogoutDialogue.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(LogoutDialogue.this, LogoutDialogue.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(LogoutDialogue.this, LogoutDialogue.this.getString(R.string.error_occurred));
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


    private void navigateToLogin() {
        LoginShared.destroySessionTypePreference();
        stopService(new Intent(LogoutDialogue.this, BackgroundLocationService.class));
        BackgroundLocationService.stoplocationservice();
        Intent logIntent = new Intent(LogoutDialogue.this, LoginActivity.class);
        startActivity(logIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }




    private void callLogoutApi() {


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
                        + LoginShared.getLoginDataModel(getApplicationContext()).getToken(),
                LoginShared.getAttendanceAddDataModel(getApplicationContext()).getResult().getId().toString(),
                object);


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {

                    try {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optString("msg").equals("Success")) {

                            Toast.makeText(getApplicationContext(),jsonObject.optString("msg"),Toast.LENGTH_SHORT).show();
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



}
