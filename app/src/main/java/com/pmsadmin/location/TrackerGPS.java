package com.pmsadmin.location;

/**
 * Created by koushikdeb on 25/04/17.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.pmsadmin.R;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;

import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;


/**
 * Created by Koushik Deb on 8/8/2016.
 */

public class TrackerGPS extends Service implements LocationListener {

    //    private  Context this;
//global variables
    public static Location mCurrentLocation;
    String employeeID;
    boolean checkGPS = false;
    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int minimumdist = 200;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 1;
    protected LocationManager locationManager;
    String LOG_TAG = "Visit";
    Notification notification2;
    MediaPlayer mp;
    String intentet_status;
    double current_lat,current_long;

    @Override
    public void onDestroy() {

        Toast.makeText(getApplicationContext(), "destroying ", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent();
//        intent.setAction(START_ALERM);
//        sendBroadcast(intent);

    }


    /*public void registerreceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(START_ALERM);
        filter.addAction("android.location.PROVIDERS_CHANGED");


        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, filter);
    }*/


    /*public void startAlert(int songid) {
        Effects ef = new Effects(this);
        ef.customplay(songid);
        if (songid == R.raw.crash) {
            ef.customplay(songid);
        }

    }*/


    private void createNotificationChannel() {


        startForeground(1,
                mynotification("Tracking your location while you are at field","Online"));


    }


    public Notification mynotification(String contenttext, String internetstatus) {
        Intent notificationIntent = new Intent(this, GiveAttendanceActivity.class);
       /* notificationIntent.setAction(Constants2.ACTION.MAIN_ACTION);*/
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = createChannel();
        } else {
            channel = "";
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel)
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setContentTitle("Location Service " + internetstatus)
                .setTicker("Retail CRM is connected")
                .setContentText(contenttext)//"Tracking your location while you are at field")
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(contenttext))
                .setSound(null)
                .setOngoing(true);
        notification2 = mBuilder
                .setPriority(PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSound(null)
                .build();
        return notification2;
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "retail notification channel ";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel mChannel = new NotificationChannel("retail notification channel", name, importance);

        mChannel.enableLights(true);
        mChannel.setSound(null,null);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "retail notification channel";
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


    public void createnetworklistner() {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

// This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {


                if (isInternetAvailable()) {
                    intentet_status = "Online";
                    updateNotification(current_accuracy + "\n" + lastupdatedstatus);
                } else {
                    intentet_status = "Offline";
                    updateNotification(current_accuracy + "\n" + lastupdatedstatus);

                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createnetworklistner();
        if (intent != null) {

            if (intent.getAction().equals("1")) {
                Log.i(LOG_TAG, "Received Start Foreground Intent ");

                createNotificationChannel();


            } /*else if (intent.getAction().equals(Constants2.ACTION.STOPFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                startAlert(R.raw.log_out);
                Effects ef = new Effects(this);

                MediaPlayer mm = ef.logout();
                mm.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        mp.reset();
                        mp.release();
                        mp = null;
                        if (myReceiver != null) {
                            unregisterReceiver(myReceiver);

                        }
                        stopForeground(true);
                        stopSelf();
                    }

                });


            }*/
        }
        return START_STICKY;
    }


    protected Handler handler = new Handler();

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        locationManager = (LocationManager) this
                .getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                60000,
                15000, this);
        Log.i("myservice", "onCreate");
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                60000,
                15000, this);
        Log.i("myservice", "onCreate");
        getLocation();
    }


    boolean canGetLocation = false;

    Location loc;
    double latitude;
    double longitude;
//    String carno;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

    public void showmsg(String msg) {
        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
    }


//    public TrackerGPS(Context this, String carno) {
//        this.this = this;
//
//        this.carno=carno;
//        getLocation();
//    }

    public Location getLocation() {

        try {
            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status


            if (!checkGPS) {
                Toast.makeText(this, "No Service Provider Available", Toast.LENGTH_SHORT).show();
                // showSettingsAlert();
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider

            }
            // if GPS Enabled get lat/long using GPS Services
            if (checkGPS) {
                // Toast.makeText(this, "GPS", Toast.LENGTH_SHORT).show();

                try {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                            mCurrentLocation = loc;
                            Log.e("got your location", loc.getLatitude() + "");
                        }
                    }
                } catch (SecurityException e) {
                    Log.e("location exeption", e.getMessage() + "");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
    }


    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();

        }
        return longitude;
    }

    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();


        }
        return latitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }


    public void stopUsingGPS() {
        if (locationManager != null) {


            locationManager.removeUpdates(TrackerGPS.this);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public IBinder onBind(Intent intent) {
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        Log.d("binding", "asdas");
        return null;
    }


    private void updateNotification(String contenttext) {


        Notification notification2 = mynotification(contenttext.trim()+"\n"+"Pending send locations", intentet_status);
        if (notification2 != null) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(1, notification2);
        }
    }


    static String lastupdatedstatus = "";
    static String current_accuracy = "";
    @Override
    public void onLocationChanged(Location location) {
        /*employeeID = CGlobal.getInstance().getPersistentPreference(TrackerGPS.this)
                .getString(Constants.PREFS_EMPLOYEE_ID, "");*/
        Toast.makeText(getApplicationContext(),"Success Tracker",Toast.LENGTH_LONG).show();

        if (mCurrentLocation == null) {
            mCurrentLocation = location;
        }
//            stophandler.sendEmptyMessage(MSG_START_TIMER);
//        }
// if(currenttime==0)
// {
//     stophandler.sendEmptyMessage(MSG_START_TIMER);
//     mystop.start();
// }
        current_lat = location.getLatitude();
        current_long = location.getLongitude();

        current_accuracy = "Current Accuracy is: " + location.getAccuracy();


//        updateNotification(current_accuracy+"\n"+lastupdatedstatus);


        if (location.distanceTo(mCurrentLocation) > minimumdist) {


            //insetrintofirebase(location);// enter into 100 meater radius

        }
        if (location.getAccuracy() < 30) {

//            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
//            DatabaseReference usersreflive = myRef.child("userslive");
//            DatabaseReference userreflive = usersreflive.child(employeeID);
//            userreflive.child("lati").setValue(location.getLatitude());
//            userreflive.child("userid").setValue("" + employeeID);
//            userreflive.child("longi").setValue(location.getLongitude());
//            userreflive.child("unixtime").setValue(unixTime);
        }

    }

   /* private void sendNotification(Context context, String accountname) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(context, todayspath.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(todayspath.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Check the transition type to display the relevant icon image

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle("Checked out with: " + accountname);


        // Continue building the notification
        builder.setContentText("Click here to go to today's visit page");
        builder.setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }*/


    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1000);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {


    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    //**************************************************************************************** back ground data sync*************************************************************************************************************************

}
