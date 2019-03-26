package com.pmsadmin.giveattandence.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Location;
import android.util.Log;


public class LocationUpdates extends IntentService {

    private String TAG = this.getClass().getSimpleName();
    public LocationUpdates() {
        super("Fused Location");
    }

    public LocationUpdates(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        /*AppPreference appPreference=new AppPreference(getApplicationContext());
        Log.i(TAG, "onHandleIntent");
        Location location = intent.getParcelableExtra(FusedLocationProviderApi.KEY_LOCATION_CHANGED);
        LocationResult result = LocationResult.extractResult(intent);
        if (location != null) {
            LocDatabaseHelper database = LocDatabaseHelper.getInstance(this);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            System.out.println(timestamp);
           // System.out.println("Fake location lat long back"+location.getLatitude()+" "+location.getLongitude());
            //Updating the location fetched detail to shared. It will be used when trip tracking will be started.
            new AppPreference(this).setTimestamp(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
            new AppPreference(this).setLattitude(location.getLatitude());
            new AppPreference(this).setLongitude(location.getLongitude());

            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_TIME, String.valueOf(timestamp));
            contentValues.put(COLUMN_LATITUDE, location.getLatitude());
            contentValues.put(COLUMN_LONGITUDE, location.getLongitude());
            contentValues.put(COLUMN_TRIP, "BCOS");

            //Adding LatLng details in sqlite after every 2 minutes
            database.insert(String.valueOf(timestamp.getTime()), location.getLatitude(), location.getLongitude());


            //Get Distance between two points
            final ArrayList<TripDistanceModels> trip_list_values = database.getDistanceInTwoPoints();
            System.out.println("background in oreo after trip"+"lat"+location.getLongitude()+"long"+location.getLongitude());

            Log.d("List Size is", "" + trip_list_values.size() +
                    "\t Latitude : " + location.getLatitude() + "\t Longitude : " + location.getLongitude() +
                    "\n Speed : " + location.getSpeed());
            new DistanceCalculation(this, trip_list_values);
            if (ConnectionDetector.isConnectingToInternet(getApplicationContext())){
                System.out.println("internet on");
                ArrayList<String> arrayList=new ArrayList<>();
                arrayList=appPreference.getcsvArrayList(ConstantGeneral.TRIPCSV_PREFERENCE);
                if (arrayList!=null) {

                   if( arrayList.size() > 0 && new AppPreference(getApplicationContext()).getTripstatus().equals("")){
                      // callapifortripcsvpload();
                       Intent serviceIntent = new Intent(getApplicationContext(), TripUploadService.class);
                       getApplicationContext().startService(serviceIntent);
                      // PendingIntent pendingIntent = PendingIntent.getService(this,0, serviceIntent, 0);
                       //getApplicationContext().startService(new Intent(getApplicationContext(), TripUploadService.class));

                   }
               }
            }
        }*/

    }

   /* private void callapifortripcsvpload() {
        getApplicationContext().startService(new Intent(getApplicationContext(), TripUploadService.class));
        //new AppPreference(getApplicationContext()).setTripstatus("1");
    }*/

}
