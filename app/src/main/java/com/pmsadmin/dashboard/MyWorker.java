package com.pmsadmin.dashboard;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.giveattandence.services.BackgroundLocationService;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyWorker extends Worker {

    Context context;
    public static final String CHANNEL_ONE_ID = "com.pms.user";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    int importance = NotificationManager.IMPORTANCE_HIGH;
    int notifyID = 1;



    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        System.out.println("check: " + "checking" + " "
                + String.valueOf(BaseActivity.latPeriodic) + " " + String.valueOf(BaseActivity.lonperiodic));


        if (LoginShared.getAttendanceAddDataModel(getApplicationContext())
                .getResult().getLoginTime() != null) {

            String loginTime = LoginShared.getAttendanceAddDataModel(getApplicationContext())
                    .getResult().getLoginTime().toString();


            String[] separated = loginTime.split("T");

                /*String sep1 = separated[0];
                String sep2 = separated[1];*/
            //attendanceViewHolder.tvLoginValue.setText(separated[1]);

            String loginTimeFormat = separated[1];


            try {

                SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                Date date1 = format.parse(loginTimeFormat);
                Date date2 = format.parse(getCurrentTimeUsingDate());
                long mills = date2.getTime() - date1.getTime();
                Log.v("Data1", "" + date1.getTime());
                Log.v("Data2", "" + date2.getTime());
                int hours = (int) (mills / (1000 * 60 * 60));
                int mins = (int) (mills / (1000 * 60)) % 60;

                String diff = hours + ":" + mins; // updated value every1 second

                //Toast.makeText(getApplicationContext(), diff, Toast.LENGTH_SHORT).show();

                System.out.println("differencePeriodick: " + diff);


                /*if (hours < 16) {

                    System.out.println("difference: " + diff);
                    //callAttandance_editApi();
                } else {
                    System.out.println("difference: " + diff);
                    callLogoutApi();
                    //callAttendandanceLogout();
                }*/



                if (mins > 15) {

                    //System.out.println("difference: " + diff);
                    //callAttandance_editApi();
                } else {
                    System.out.println("differenceMin: " + diff);

                    Intent intent = new Intent("custom-event-name");
                    // You can also include some extra data.
                    intent.putExtra("message", "This is my message!");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                    //callLogoutApi();
                    //callAttendandanceLogout();
                }



                //txtCurrentTime.setText(diff);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //callLogoutApi();


        return Result.SUCCESS;
    }


    public String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }

    private void callLogoutApi() {

        JsonObject object = new JsonObject();
        object.addProperty("logout_time", getTodaysDate() + "T" + getCurrentTimeUsingDate());
        object.addProperty("logout_latitude", String.valueOf(BaseActivity.latPeriodic));
        object.addProperty("logout_longitude", String.valueOf(BaseActivity.lonperiodic));
        if (BaseActivity.addressPeriodic != null) {
            if (BaseActivity.addressPeriodic.size() > 0) {
                object.addProperty("logout_address", BaseActivity.addressPeriodic.get(0).getLocality() + "," + BaseActivity.addressPeriodic.get(0).getAdminArea());
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


    private String getTodaysDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }


    @Override
    public void onStopped() {
        super.onStopped();
    }

    private void navigateToLogin() {


        onStopped();

        LoginShared.destroySessionTypePreference();
        //stopService(new Intent(getApplicationContext(), BackgroundLocationService.class));
        BackgroundLocationService.stoplocationservice();
        Intent logIntent = new Intent(getApplicationContext(), LoginActivity.class);
        getApplicationContext().startActivity(logIntent);

    }


}
