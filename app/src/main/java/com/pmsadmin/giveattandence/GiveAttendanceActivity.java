package com.pmsadmin.giveattandence;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dialog.GiveReasonDialog;
import com.pmsadmin.giveattandence.adapter.AttendanceHistoryAdapter;
import com.pmsadmin.giveattandence.addattandencemodel.AttendanceAddModel;
import com.pmsadmin.giveattandence.updatedattandenceListModel.UpdatedAttendanceListModel;
import com.pmsadmin.location.GPSTracker;
import com.pmsadmin.location.TrackerGPS;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.MarshMallowPermissions;
import com.pmsadmin.utils.SpacesItemDecoration;
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

public class GiveAttendanceActivity extends BaseActivity implements View.OnClickListener {
    public View view;
    RecyclerView rv_items;
    Button btn_login, btn_logout;
    private LoadingData loader;
    public List<Address> addresses;
    List<Address> addresses1;
    Geocoder geocoder;
    public GPSTracker gpsTracker;
    private LocationManager manager = null;
    private Location mCurrentLocation;
    private GoogleApiClient googleApiClient;
    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 900000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;
    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_give_attadence, null);
        addContentView(view);
        MethodUtils.setStickyBar(GiveAttendanceActivity.this);
        gpsTracker = new GPSTracker(GiveAttendanceActivity.this);
        mRequestingLocationUpdates = false;
        updateValuesFromBundle(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);
        final MarshMallowPermissions mmPermission = new MarshMallowPermissions(GiveAttendanceActivity.this);
        if (mmPermission.isAllGpsPermissionAllowed()) {
            //buildAlertMessageNoGps();
        }
        mLastUpdateTime = "";
        loader = new LoadingData(GiveAttendanceActivity.this);
        geocoder = new Geocoder(GiveAttendanceActivity.this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BindView();
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        //callservice();
        setClickEvent();
        //getAttandenceListing();
        getAttendanceListingUpdated();
    }

    private void callservice() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, TrackerGPS.class));
        } else {
            startService(new Intent(this, TrackerGPS.class));
        }
    }

    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };
    }

    private void setClickEvent() {
        btn_login.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
    }

    private String getTodaysDate() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss aa";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }

    public String getLogoutTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }

    private void BindView() {
        rv_items = findViewById(R.id.rv_items);
        btn_login = findViewById(R.id.btn_login);
        btn_logout = findViewById(R.id.btn_logout);
    }

    private void setRecyclerView() {
        AttendanceHistoryAdapter adapter = new AttendanceHistoryAdapter(GiveAttendanceActivity.this,
                LoginShared.getUpdatedAttendanceListDataModel(GiveAttendanceActivity.this).getResults());
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(GiveAttendanceActivity.this, RecyclerView.VERTICAL, false);
        rv_items.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        rv_items.setAdapter(adapter);
    }

    public void startUpdatesButtonHandler(View view) {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            /*setButtonsEnabledState();*/
            startLocationUpdates();
        }
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i("TAG", "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("TAG", "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates.
     */
    public void stopUpdatesButtonHandler(View view) {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        stopLocationUpdates();
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    public void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d("TAG", "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;
                        //setButtonsEnabledState();
                    }
                });
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i("TAG", "All location settings are satisfied.");

                        //noinspection MissingPermission
                        /*if (android.support.v4.app.ActivityCompat.
                                checkSelfPermission(MainActivity.this,
                                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && android.support.v4.app.ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }*/
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i("TAG", "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(GiveAttendanceActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i("TAG", "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e("TAG", errorMessage);
                                Toast.makeText(GiveAttendanceActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Remove location updates to save battery.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_login:
                //startUpdatesButtonHandler(view);
                LoginShared.setLoginTime(GiveAttendanceActivity.this, getCurrentTimeUsingDate());
                if (LoginShared.getAttendanceFirstLoginTime(GiveAttendanceActivity.this).equals("1")) {
                    MethodUtils.errorMsg(GiveAttendanceActivity.this,"You are already Logged in");
                } else {
                    addAttendance();
                }
                break;
            case R.id.btn_logout:
                //Toast.makeText(GiveAttendanceActivity.this,String.valueOf(diffTime()),Toast.LENGTH_LONG).show();
                if (diffTime() > 600) {
                    logoutApi();
                } else {
                    new GiveReasonDialog(GiveAttendanceActivity.this).show();
                }
                //logoutApi();
                break;
        }
    }

    private void logoutApi() {
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("logout_time", getCurrentTimeUsingDate());
        object.addProperty("logout_latitude", gpsTracker.getLatitude());
        object.addProperty("logout_longitude", gpsTracker.getLongitude());
        if (addresses.size() > 0) {
            object.addProperty("logout_address", addresses.get(0).getLocality() + "," + addresses.get(0).getAdminArea());
        }else{
            object.addProperty("logout_address", "");
        }

        object.addProperty("approved_status",4);
        object.addProperty("justification","");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_attendanceLogoutApi("Token "
                        + LoginShared.getLoginDataModel(GiveAttendanceActivity.this).getToken(),
                LoginShared.getAttendanceAddDataModel(GiveAttendanceActivity.this).getResult().getId().toString(),
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
                            LoginShared.setAttendanceFirstLoginTime(GiveAttendanceActivity.this, "0");
                            stopLocationUpdates();
                            Intent i=new Intent(GiveAttendanceActivity.this,LoginActivity.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(GiveAttendanceActivity.this, jsonObject.optString("msg"));
                        } else {
                            MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(GiveAttendanceActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
            }
        });

    }

    public long diffTime() {
        long min = 0;
        long difference;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss aa"); // for 12-hour system, hh should be used instead of HH
            // There is no minute different between the two, only 8 hours difference. We are not considering Date, So minute will always remain 0
            int length = getCurrentTimeUsingDate().length();
            String current = getCurrentTimeUsingDate().substring(0, length);
            Date date1 = simpleDateFormat.parse(current);
            Date date2 = simpleDateFormat.parse(LoginShared.getLoginTime(GiveAttendanceActivity.this));

            difference = (date1.getTime() - date2.getTime()) / 1000;
            long hours = difference % (24 * 3600) / 3600; // Calculating Hours
            long minute = difference % 3600 / 60; // Calculating minutes if there is any minutes difference
            min = minute + (hours * 60); // This will be our final minutes. Multiplying by 60 as 1 hour contains 60 mins
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return min;
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    /*private void getAttandenceListing() {
        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_attendanceListingApi("Token "
                + LoginShared.getLoginDataModel(GiveAttendanceActivity.this).getToken());

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 201 || response.code()==200) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        UpdatedAttendanceListModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            loginModel = gson.fromJson(responseString, UpdatedAttendanceListModel.class);
                            LoginShared.setUpdatedAttendanceListDataModel(GiveAttendanceActivity.this, loginModel);
                            //setRecyclerView();
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(GiveAttendanceActivity.this, jsonObject.optString("msg"));
                        } else {
                            MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(GiveAttendanceActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
            }
        });

    }*/

    private void getAttendanceListingUpdated() {
        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_employeeListApi("Token "
                + LoginShared.getLoginDataModel(GiveAttendanceActivity.this).getToken(),
                LoginShared.getLoginDataModel(GiveAttendanceActivity.this).getUserId().toString());

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 201 || response.code()==200) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        UpdatedAttendanceListModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            loginModel = gson.fromJson(responseString, UpdatedAttendanceListModel.class);
                            LoginShared.setUpdatedAttendanceListDataModel(GiveAttendanceActivity.this, loginModel);
                            setRecyclerView();
                        } else if (jsonObject.optInt("request_status") == 0) {
                            //MethodUtils.errorMsg(GiveAttendanceActivity.this, jsonObject.optString("msg"));
                            loginModel = gson.fromJson(responseString, UpdatedAttendanceListModel.class);
                            LoginShared.setUpdatedAttendanceListDataModel(GiveAttendanceActivity.this, loginModel);
                            setRecyclerView();
                        } else {
                            MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(GiveAttendanceActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
            }
        });

    }


    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            Toast.makeText(getApplicationContext(), "location", Toast.LENGTH_LONG).show();
            JsonObject object = new JsonObject();
            object.addProperty("attandance",1);
            object.addProperty("time",getCurrentTimeUsingDate());
            object.addProperty("latitude",mCurrentLocation.getLatitude());
            object.addProperty("longitude",mCurrentLocation.getLongitude());
            object.addProperty("longitude",mCurrentLocation.getLongitude());
            try {
                addresses1 = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
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
                            + LoginShared.getLoginDataModel(GiveAttendanceActivity.this).getToken(),
                    object);

            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(GiveAttendanceActivity.this,"Success",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(GiveAttendanceActivity.this,"Failure",Toast.LENGTH_LONG).show();
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

    private void addAttendance() {
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("type", 1);
        object.addProperty("employee", LoginShared.getLoginDataModel(GiveAttendanceActivity.this).getUserId());
        object.addProperty("date", getTodaysDate());
        object.addProperty("login_time", getCurrentTimeUsingDate());
        object.addProperty("login_latitude", gpsTracker.getLatitude());
        object.addProperty("login_longitude", gpsTracker.getLongitude());
        if (addresses.size() > 0) {
            object.addProperty("login_address", addresses.get(0).getLocality() + "," + addresses.get(0).getAdminArea());
        } else {
            object.addProperty("login_address", "");
        }
        object.addProperty("justification", "");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_attendanceAddApi("Token "
                        + LoginShared.getLoginDataModel(GiveAttendanceActivity.this).getToken(),
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
                            loginModel = gson.fromJson(responseString, AttendanceAddModel.class);
                            LoginShared.setAttendanceAddDataModel(GiveAttendanceActivity.this, loginModel);
                            LoginShared.setAttendanceFirstLoginTime(GiveAttendanceActivity.this, "1");
                            MethodUtils.errorMsg(GiveAttendanceActivity.this, jsonObject.optString("msg"));
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(GiveAttendanceActivity.this, jsonObject.optString("msg"));
                        } else {
                            MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(GiveAttendanceActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(GiveAttendanceActivity.this, GiveAttendanceActivity.this.getString(R.string.error_occurred));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
