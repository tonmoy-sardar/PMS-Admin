package com.pmsadmin.giveattandence.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.netconnection.ConnectionDetector;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;


public class BackgroundLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected static final String TAG = "BackService";

    //Context context;

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 600000;
    //public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 60000;
    public static GoogleApiClient mGoogleApiClient;
    public static LocationRequest mLocationRequest;
    private static PendingIntent mPendingIntent;
    IBinder mBinder = new LocalBinder();
    List<Address> addresses1;
    Geocoder geocoder;



    private class LocalBinder extends Binder {
        public BackgroundLocationService getServerInstance() {
            return BackgroundLocationService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Context mContext = this.getApplicationContext();
            String CHANNEL_ID = "Background job";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);
            Notification notification = new Notification.Builder(mContext, CHANNEL_ID)
                    .setContentTitle("PMSAdmin")
                    .setContentText("Location finding....")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            startForeground(1, notification);
        } else {
            Intent mIntentService = new Intent(this, LocationUpdates.class);
            mPendingIntent = PendingIntent.getService(this, 1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT);
        }





        buildGoogleApiClient();


        System.out.println("check: "+ "checking"+" "
                +String.valueOf(BaseActivity.latPeriodic)+ " "+String.valueOf(BaseActivity.lonperiodic));






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


    private void navigateToLogin() {
        LoginShared.destroySessionTypePreference();
        stopService(new Intent(getApplicationContext(), BackgroundLocationService.class));
        BackgroundLocationService.stoplocationservice();
        Intent logIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(logIntent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (mGoogleApiClient.isConnected()) {
            Log.i(TAG + " onStartCmd", "Connected");
            return START_STICKY;
        }

        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
            Log.i(TAG + " onStartCmd", "GoogleApiClient not Connected");
            mGoogleApiClient.connect();
        }
       /* NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);

        notification.setContentTitle("BCOS ");
        notification.setContentText("\n Tracking BackGround Again ... ");
        notification.setSmallIcon(R.drawable.big_marker);
        notificationManager.notify(151458461, notification.build());*/

        return START_STICKY;
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        Log.i(TAG, "createLocationRequest()");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(900000);
        //mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        Log.i(TAG, "Started Location Updates");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requestLocationUpdates();
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mPendingIntent);
        }
    }

    /*public void stopLocationUpdates() {
        Log.i(TAG, "Stopped Location Updates");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           stopService(new Intent(this, BackgroundLocationUpdateService.class));
        }else {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mPendingIntent);
        }
    }*/

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        startLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("background in oreo before trip " + "lat" + location.getLatitude() + "long" + location.getLongitude());
        String message = "Latitude : " + location.getLatitude() + "\n Longitude : " + location.getLongitude() +
                "\n location Accuracy: " + location.getAccuracy() + "\n speed: " + location.getSpeed();
        Log.d(TAG, "onLocationChanged: " + message);


        //MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();

        //64985209
        updateLocationUI(location);


        call_sixteen_hrsLogout();

        //new BackgroundLocationUpdateService().locationWork(this,location);
    }

    private void call_sixteen_hrsLogout() {

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



                if (hours < 16) {

                    //System.out.println("LessMins: "+ String.valueOf(mins));
                    System.out.println("LessMins: "+ String.valueOf(hours));
                    //System.out.println("difference: " + diff);
                    //callAttandance_editApi();
                } else {

                    //System.out.println("Mins: "+ String.valueOf(mins));
                    System.out.println("Mins: "+ String.valueOf(hours));
                    callLogoutApi();
                    /*if(ConnectionDetector.isConnectingToInternet(getApplicationContext())){
                        callLogoutApi();
                    }else {
                        //callLogoutApi();
                    }*/
                    //callAttendandanceLogout();
                }



                //txtCurrentTime.setText(diff);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



    }

    public String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "HH:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }

    private String getTodaysDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    private void updateLocationUI(Location location) {
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        //if (mCurrentLocation != null) {
        //Toast.makeText(getApplicationContext(), "location", Toast.LENGTH_LONG).show();
        JsonObject object = new JsonObject();
        if (LoginShared.getAttendanceAddDataModel(getApplicationContext()).getResult() != null) {
            object.addProperty("attandance", LoginShared.getAttendanceAddDataModel(getApplicationContext()).getResult().getId());
        }
        object.addProperty("time", getTodaysDate() + "T" + getCurrentTimeUsingDate());
        object.addProperty("latitude", location.getLatitude());

        object.addProperty("longitude", location.getLongitude());
        try {
            addresses1 = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses1 != null) {
            if (addresses1.size() > 0) {
                object.addProperty("address", addresses1.get(0).getLocality() + "," + addresses1.get(0).getAdminArea());
            } else {
                object.addProperty("address", "");
            }
        } else {
            object.addProperty("address", "");
        }

        System.out.println("add_log: "+ object.toString());

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_attendanceLocationUpdateApi("Token "
                        + LoginShared.getLoginDataModel(getApplicationContext()).getToken(),
                object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                try {
                    System.out.println("Success: "+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),call.toString(),Toast.LENGTH_LONG).show();
            }
        });
    }







    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopLocationUpdates();

    }

    public void requestLocationUpdates() {
        try {
            Log.i(TAG, "Starting location updates");
            LocationRequestHelper.setRequesting(this, true);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            LocationRequestHelper.setRequesting(this, false);
            e.printStackTrace();
        }
    }

   /* private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, BackgroundLocationUpdateService.class);
        intent.setAction(BackgroundLocationUpdateService.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }*/

    public static void stoplocationservice() {
        // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mPendingIntent);
        mGoogleApiClient.disconnect();
        mGoogleApiClient = null;
        mPendingIntent = null;
    }
}