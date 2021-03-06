package com.pmsadmin.showgeofence;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.application.MyApplication;
import com.pmsadmin.attendancelist.reportlistmodel.Result;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dialog.deviationdialog.DeviationDialog;
import com.pmsadmin.location.GPSTracker;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.showgeofence.googlemap.AvailableUserMarker;
import com.pmsadmin.showgeofence.googlemap.CustomInfoWindowAdapter;

import java.util.ArrayList;
import java.util.List;

public class GeoFenceActivity extends BaseActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        ResultCallback<Status>, View.OnClickListener {

    private static final String TAG = GeoFenceActivity.class.getSimpleName();

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    public View view;
    RelativeLayout rl_bottom;
    TextView tv_name, tv_form, tv_deviation, tv_reason;
    EditText et_search;
    GPSTracker gpsTracker;
    int position = 0;
    private Marker geoFenceMarker;
    Button btn_all;

    private MapFragment mapFragment;
    //private RelativeLayout rl_bottom;

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent(context, GeoFenceActivity.class);
        intent.putExtra(NOTIFICATION_MSG, msg);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_geo_fence, null);
        addContentView(view);
        // initialize GoogleMaps
        gpsTracker = new GPSTracker(GeoFenceActivity.this);
        initGMaps();
        bindView();
        if (getIntent().getExtras() != null) {
            position = Integer.parseInt(getIntent().getStringExtra("position"));
        }

        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);

        setData();
        fontSet();

        // create GoogleApiClient
        createGoogleApi();
        setClickEvents();
    }

    private void fontSet() {
        tv_name.setTypeface(MethodUtils.getNormalFont(GeoFenceActivity.this));
        tv_form.setTypeface(MethodUtils.getNormalFont(GeoFenceActivity.this));
        tv_reason.setTypeface(MethodUtils.getNormalFont(GeoFenceActivity.this));
        tv_deviation.setTypeface(MethodUtils.getNormalFont(GeoFenceActivity.this));
        et_search.setTypeface(MethodUtils.getNormalFont(GeoFenceActivity.this));
    }

    private void setData() {


        if (LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getDeviationDetails().size()>0) {

            rl_bottom.setVisibility(View.VISIBLE);

            tv_name.setText(LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getEmployeeDetails().get(0).getCuUser().getFirstName() + " " +
                    LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getEmployeeDetails().get(0).getCuUser().getLastName());
            tv_reason.setText("Justification:" + LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getJustification());
            if (LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getDeviationDetails().size() > 0) {
                tv_deviation.setText("Location Deviation: " + MethodUtils.deviationTime(LoginShared.getResultList(GeoFenceActivity.this, "result").
                        get(position).getDeviationDetails().get(0).getFromTime()) + " - " + MethodUtils.deviationTime(LoginShared.getResultList(GeoFenceActivity.this, "result").
                        get(position).getDeviationDetails().get(0).getToTime()));

                tv_form.setText("Log In: " + MethodUtils.deviationDate(LoginShared.getResultList(GeoFenceActivity.this, "result").
                        get(position).getDeviationDetails().get(0).getFromTime()) + "  |  " + "Log Out: " + MethodUtils.deviationDate(LoginShared.getResultList(GeoFenceActivity.this, "result").
                        get(position).getDeviationDetails().get(0).getToTime()));
            } else {
                tv_deviation.setVisibility(View.GONE);
                tv_form.setVisibility(View.GONE);
            }
        }else {
            rl_bottom.setVisibility(View.GONE);
        }
    }

        /*tv_name.setText("Santanu Pal");
        tv_reason.setText("for late update");*/

    private void bindView() {
        rl_bottom = view.findViewById(R.id.rl_bottom);
        btn_all = view.findViewById(R.id.btn_all);
        tv_name = view.findViewById(R.id.tv_name);
        tv_form = view.findViewById(R.id.tv_form);
        tv_reason = view.findViewById(R.id.tv_reason);
        tv_deviation = view.findViewById(R.id.tv_deviation);
        et_search = view.findViewById(R.id.et_search);
        tv_universal_header.setText("Location Tracker");
    }

    private void setClickEvents() {
        rl_bottom.setOnClickListener(this);
        btn_all.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_bottom:
                // new DeviationDialog(GeoFenceActivity.this).show();
                break;
            case R.id.btn_all:

                if (LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getDeviationDetails().size() > 0) {

                    new DeviationDialog(GeoFenceActivity.this,
                            LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getDeviationDetails(),
                            LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getEmployeeDetails().get(0).getCuUser().getFirstName() + " " +
                                    LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getEmployeeDetails().get(0).getCuUser().getLastName()).show();
                }else {
                    Toast.makeText(getApplicationContext(), "No deviation found", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main_menu, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.geofence: {
                startGeofence();
                return true;
            }
            case R.id.clear: {
                clearGeofence();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }*/

    private final int REQ_PERMISSION = 999;

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLastKnownLocation();
                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    // Initialize GoogleMaps
    private void initGMaps() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Callback called when Map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng;
        Log.d(TAG, "onMapReady()");
        MyApplication.getInstance().googleMap = googleMap;
        MyApplication.getInstance().googleMap.setInfoWindowAdapter(
                new CustomInfoWindowAdapter(GeoFenceActivity.this));
        MyApplication.getInstance().googleMap.setOnMapClickListener(this);
        MyApplication.getInstance().googleMap.setOnMarkerClickListener(this);

        if (LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getUserProject().getSiteLocation() != null) {
            MyApplication.getInstance().googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getUserProject().getSiteLocation().getLatitude()),
                    Double.parseDouble(LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getUserProject().getSiteLocation().getLongitude())), 12.8f));
        } else {
            MyApplication.getInstance().googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()), 12.8f));
        }
        /*if(LoginShared.getResultList(GeoFenceActivity.this,"result").get(position).getUserProject().getSiteLocation()!=null){
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(LoginShared.getResultList(GeoFenceActivity.this,"result").get(position).getUserProject().getSiteLocation().getLatitude()),
                            Double.parseDouble(LoginShared.getResultList(GeoFenceActivity.this,"result").get(position).getUserProject().getSiteLocation().getLongitude())))
                    .zoom(12.8f)
                    .build();

            CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

            MyApplication.getInstance().googleMap.moveCamera(camUpdate);

        }else{
            CameraPosition camPos = new CameraPosition.Builder()
                    .target(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude()))
                    .zoom(12.8f)
                    .build();

            CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);

            MyApplication.getInstance().googleMap.moveCamera(camUpdate);
        }*/
        if (LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getUserProject().getSiteLocation() != null) {
            new AvailableUserMarker(GeoFenceActivity.this, LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getUserProject().getSiteLocation().getLatitude(),
                    LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getUserProject().getSiteLocation().getLongitude());
        } else {
            new AvailableUserMarker(GeoFenceActivity.this, String.valueOf(gpsTracker.getLatitude()),
                    String.valueOf(gpsTracker.getLongitude()));
        }
        if (LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getUserProject().getSiteLocation() != null) {
            latLng = new LatLng(Double.parseDouble(LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getUserProject().getSiteLocation().getLatitude()),
                    Double.parseDouble(LoginShared.getResultList(GeoFenceActivity.this, "result").get(position).getUserProject().getSiteLocation().getLongitude()));
        } else {
            latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        }
        LatLng coordinate = new LatLng(latLng.latitude, latLng.longitude); //Store these lat lng values somewhere. These should be constant.
        MyApplication.getInstance().googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 15));
        markerForGeofence(latLng);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick(" + latLng + ")");
        //markerForGeofence(latLng);
        startGeofence();

    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
        //markerForGeofence(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClickListener: " + marker.getPosition());
        return false;
    }

    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;

    // Start location Updates
    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;
        writeActualLocation(location);
    }

    // GoogleApiClient.ConnectionCallbacks connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
        recoverGeofenceMarker();
    }

    // GoogleApiClient.ConnectionCallbacks suspended
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();
    }

    private void writeActualLocation(Location location) {
        //textLat.setText( "Lat: " + location.getLatitude() );
        //textLong.setText( "Long: " + location.getLongitude() );

        //markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    private Marker locationMarker;

    private void markerLocation(LatLng latLng) {

        Log.i(TAG, "markerLocation(" + latLng + ")");
        String title = latLng.latitude + ", " + latLng.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        if (MyApplication.getInstance().googleMap != null) {
            if (locationMarker != null)
                locationMarker.remove();
            locationMarker = MyApplication.getInstance().googleMap.addMarker(markerOptions);
            float zoom = 14f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            MyApplication.getInstance().googleMap.animateCamera(cameraUpdate);
        }
    }


    private void markerForGeofence(LatLng latLng) {
        Log.i(TAG, "markerForGeofence(" + latLng + ")");
        String title = latLng.latitude + ", " + latLng.longitude;
        // Define marker options
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title(title);

        if (MyApplication.getInstance().googleMap != null) {
            // Remove last geoFenceMarker
            if (geoFenceMarker != null)
                geoFenceMarker.remove();

            geoFenceMarker = MyApplication.getInstance().googleMap.addMarker(markerOptions);
        }
    }

    // Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");
        if (geoFenceMarker != null) {
            Geofence geofence = createGeofence(geoFenceMarker.getPosition(), GEOFENCE_RADIUS);
            GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
            addGeofence(geofenceRequest);
        } else {
            Log.e(TAG, "Geofence marker is null");
        }
    }

    private static final long GEO_DURATION = 60 * 60 * 1000;
    private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 500.0f; // in meters

    // Create a Geofence
    private Geofence createGeofence(LatLng latLng, float radius) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setExpirationDuration(GEO_DURATION)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest(Geofence geofence) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();
    }

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;

    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if (geoFencePendingIntent != null)
            return geoFencePendingIntent;

        Intent intent = new Intent(this, GeofenceTrasitionService.class);
        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if (status.isSuccess()) {
            saveGeofence();
            drawGeofence();
        } else {
            // inform about fail
        }
    }

    // Draw Geofence circle on GoogleMap
    private Circle geoFenceLimits;

    private void drawGeofence() {
        Log.d(TAG, "drawGeofence()");

        if (geoFenceLimits != null)
            geoFenceLimits.remove();

        CircleOptions circleOptions = new CircleOptions()
                .center(geoFenceMarker.getPosition())
                .strokeColor(Color.argb(200, 255, 0, 0))
                //.fillColor( Color.argb(100, 150,150,150) )
                .radius(GEOFENCE_RADIUS);
        geoFenceLimits = MyApplication.getInstance().googleMap.addCircle(circleOptions);
    }

    private final String KEY_GEOFENCE_LAT = "GEOFENCE LATITUDE";
    private final String KEY_GEOFENCE_LON = "GEOFENCE LONGITUDE";

    // Saving GeoFence marker with prefs mng
    private void saveGeofence() {
        Log.d(TAG, "saveGeofence()");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putLong(KEY_GEOFENCE_LAT, Double.doubleToRawLongBits(geoFenceMarker.getPosition().latitude));
        editor.putLong(KEY_GEOFENCE_LON, Double.doubleToRawLongBits(geoFenceMarker.getPosition().longitude));
        editor.apply();
    }

    // Recovering last Geofence marker
    private void recoverGeofenceMarker() {
        Log.d(TAG, "recoverGeofenceMarker");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        if (sharedPref.contains(KEY_GEOFENCE_LAT) && sharedPref.contains(KEY_GEOFENCE_LON)) {
            double lat = Double.longBitsToDouble(sharedPref.getLong(KEY_GEOFENCE_LAT, -1));
            double lon = Double.longBitsToDouble(sharedPref.getLong(KEY_GEOFENCE_LON, -1));
            LatLng latLng = new LatLng(lat, lon);
            //markerForGeofence(latLng);
            drawGeofence();
        }
    }

    /*// Clear Geofence
    private void clearGeofence() {
        Log.d(TAG, "clearGeofence()");
        LocationServices.GeofencingApi.removeGeofences(
                googleApiClient,
                createGeofencePendingIntent()
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if ( status.isSuccess() ) {
                    // remove drawing
                    removeGeofenceDraw();
                }
            }
        });
    }*/

    private void removeGeofenceDraw() {
        Log.d(TAG, "removeGeofenceDraw()");
        if (geoFenceMarker != null)
            geoFenceMarker.remove();
        if (geoFenceLimits != null)
            geoFenceLimits.remove();
    }

}
