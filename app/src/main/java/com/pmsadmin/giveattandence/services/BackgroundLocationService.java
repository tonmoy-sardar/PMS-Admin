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
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.pmsadmin.R;


public class BackgroundLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected static final String TAG = "BackService";

    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 900000;
    public static GoogleApiClient mGoogleApiClient;
    public static LocationRequest mLocationRequest;
    private static PendingIntent mPendingIntent;
    IBinder mBinder = new LocalBinder();

    private class LocalBinder extends Binder{
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
                            .setContentTitle("Bcos")
                            .setContentText("Trip finding....")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .build();
            startForeground(1, notification);
        }else {
            Intent mIntentService = new Intent(this, LocationUpdates.class);
            mPendingIntent = PendingIntent.getService(this, 1, mIntentService, PendingIntent.FLAG_UPDATE_CURRENT);

        } buildGoogleApiClient();
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
        }else {
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
       System.out.println("background in oreo before trip "+"lat"+location.getLongitude()+"long"+location.getLongitude());
        String message = "Latitude : " + location.getLatitude() + "\n Longitude : " + location.getLongitude() +
                "\n location Accuracy: " + location.getAccuracy() + "\n speed: " + location.getSpeed();
        Log.d(TAG, "onLocationChanged: " + message);
        //new BackgroundLocationUpdateService().locationWork(this,location);
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
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
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

    public static void stoplocationservice(){
       // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, mPendingIntent);
        mGoogleApiClient.disconnect();
        mGoogleApiClient=null;
        mPendingIntent=null;
    }
}