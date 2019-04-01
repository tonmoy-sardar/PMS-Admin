package com.pmsadmin.giveattandence.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationResult;
import com.google.gson.JsonObject;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LocationUpdates extends IntentService {

    List<Address> addresses1;
    Geocoder geocoder;
    private String TAG = this.getClass().getSimpleName();
    public LocationUpdates() {
        super("Fused Location");
    }

    public LocationUpdates(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent");
        Location location = intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);
        LocationResult result = LocationResult.extractResult(intent);
        if (location != null) {
            updateLocationUI(location);
        }

    }

   /* private void callapifortripcsvpload() {
        getApplicationContext().startService(new Intent(getApplicationContext(), TripUploadService.class));
        //new AppPreference(getApplicationContext()).setTripstatus("1");
    }*/

    public String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }

    private void updateLocationUI(Location location) {
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        //if (mCurrentLocation != null) {
        //Toast.makeText(getApplicationContext(), "location", Toast.LENGTH_LONG).show();
        JsonObject object = new JsonObject();
        object.addProperty("attandance",1);
        object.addProperty("time",getCurrentTimeUsingDate());
        object.addProperty("latitude",location.getLatitude());
        object.addProperty("longitude",location.getLongitude());
        try {
            addresses1 = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses1.size() > 0) {
            object.addProperty("login_address", addresses1.get(0).getLocality() + "," + addresses1.get(0).getAdminArea());
        } else {
            object.addProperty("login_address", "");
        }

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_attendanceLocationUpdateApi("Token "
                        + LoginShared.getLoginDataModel(getApplicationContext()).getToken(),
                object);

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_LONG).show();
            }
        });


            /*mLatitudeTextView.setText(String.format(Locale.ENGLISH, "%s: %f", mLatitudeLabel,
                    mCurrentLocation.getLatitude()));
            mLongitudeTextView.setText(String.format(Locale.ENGLISH, "%s: %f", mLongitudeLabel,
                    mCurrentLocation.getLongitude()));
            mLastUpdateTimeTextView.setText(String.format(Locale.ENGLISH, "%s: %s",
                    mLastUpdateTimeLabel, mLastUpdateTime));*/
    }
}
