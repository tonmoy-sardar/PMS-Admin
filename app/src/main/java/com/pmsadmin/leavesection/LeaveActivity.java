package com.pmsadmin.leavesection;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.attendancelist.ApprovalListActivity;
import com.pmsadmin.attendancelist.AttendanceListActivity;
import com.pmsadmin.attendancelist.reportlistmodel.ReportListModel;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.dashboard.LogoutDialogue;
import com.pmsadmin.dialog.universalpopup.UniversalPopup;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.giveattandence.addattandencemodel.AttendanceAddModel;
import com.pmsadmin.giveattandence.services.BackgroundLocationService;
import com.pmsadmin.leavesection.adapter.LeaveHistoryAdapter;
import com.pmsadmin.leavesection.leavehistorymodel.LeaveHistoryModel;
import com.pmsadmin.leavesection.leavehistorymodel.Result;
import com.pmsadmin.location.GPSTracker;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.networking.NetworkCheck;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.SpacesItemDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LeaveActivity extends BaseActivity implements View.OnClickListener {

    public View view;
    private int month, year, day;
    EditText et_from,et_to;
    EditText et_type;
    Button btn_report,btn_approval,btn_apply;
    RelativeLayout rl_form,rl_to,rl_type,rl_bottom;
    LinearLayout ll_header;
    RecyclerView rv_items;
    EditText tv_reason;
    private List<String> leaveList = new ArrayList<>();
    private List<Result> list = new ArrayList<>();
    private UniversalPopup leavePopup;
    LeaveHistoryAdapter adapter;
    private LoadingData loader;
    DatePickerDialog picker;

    DatePickerDialog pickerTo;

    String fromDate = "";
    String toDate = "";
    private int page = 1;
    private int page1 = 1;

    private ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;

    private SimpleLocation location;
    public GPSTracker gpsTracker;
    public List<Address> addresses;

    Geocoder geocoder;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_leave, null);
        addContentView(view);
        loader = new LoadingData(LeaveActivity.this);
        bindView();
        fontSet();
        setClickEvent();
        addLeaveListAndCall();

        initializeDrawer();
        setLeaveHistoryRecyclerView();

        location = new SimpleLocation(this, false, false, 10000);

        gpsTracker = new GPSTracker(LeaveActivity.this);
        geocoder = new Geocoder(LeaveActivity.this, Locale.getDefault());

        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        if (LoginShared.getAttendanceFirstLoginTime(LeaveActivity.this).equals("1")){


            location.setListener(new SimpleLocation.Listener() {

                public void onPositionChanged() {
                    // new location data has been received and can be accessed

                    latPeriodic = location.getLatitude();
                    lonperiodic = location.getLongitude();

                }

            });

            if (latPeriodic == 0.0 && lonperiodic == 0.0) {
                int i = 0;
                while (i < 5) {
                    latPeriodic = location.getLatitude();
                    lonperiodic = location.getLongitude();
                    if (latPeriodic == 0.0 && lonperiodic == 0.0) {
                        i++;
                    } else {
                        break;
                    }
                }
            }

            try {
                addressPeriodic = geocoder.getFromLocation(latPeriodic, lonperiodic, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }



    }



    private void initializeDrawer() {
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name
        ) {
            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        initializeDrawerToggle(mDrawerToggle);
    }

    private boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    private void setLeaveHistoryApi() {
        //list.clear();
        //loader.show_with_label("Loading");
        if (!loading1) {
            loader.show_with_label("Loading");
        }
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        System.out.println("attendanceID: "+LoginShared.getLoginDataModel(LeaveActivity.this).getUserId().toString());

        final  Call<ResponseBody> register=apiInterface.call_leaveHistoryApi("Token "
                        + LoginShared.getLoginDataModel(LeaveActivity.this).getToken(),
                "application/json",LoginShared.getLoginDataModel(LeaveActivity.this).getUserId().toString(),
                String.valueOf(page1));

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 201 || response.code() == 200) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        LeaveHistoryModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        System.out.println("attendanceresponse: "+responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            loginModel = gson.fromJson(responseString, LeaveHistoryModel.class);
                            LoginShared.setLeaveHistoryDataModel(LeaveActivity.this, loginModel);
                            list.addAll(LoginShared.getLeaveHistoryDataModel(LeaveActivity.this).getResults());
                            adapter.notifyDataSetChanged();
                            //setLeaveHistoryRecyclerView();
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(LeaveActivity.this, jsonObject.optString("msg"));
                        } else {
                            MethodUtils.errorMsg(LeaveActivity.this, LeaveActivity.this.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(LeaveActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(LeaveActivity.this, LeaveActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setClickEvent() {
        et_from.setOnClickListener(this);
        et_to.setOnClickListener(this);
        et_type.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        btn_approval.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
    }

    private void addLeaveListAndCall() {
        leaveList.add("EL");
        leaveList.add("CL");
        leaveList.add("Absent");
        leavePopup = new UniversalPopup(LeaveActivity.this, leaveList, et_type);
    }

    private void bindView() {
        et_from=view.findViewById(R.id.et_from);
        et_to=view.findViewById(R.id.et_to);
        rl_form=view.findViewById(R.id.rl_form);
        rl_to=view.findViewById(R.id.rl_to);
        rl_type=view.findViewById(R.id.rl_type);
        et_type=view.findViewById(R.id.et_type);
        rv_items=view.findViewById(R.id.rv_items);
        btn_report=view.findViewById(R.id.btn_report);
        btn_approval=view.findViewById(R.id.btn_approval);
        ll_header=view.findViewById(R.id.ll_header);
        rl_bottom=view.findViewById(R.id.rl_bottom);
        btn_apply=view.findViewById(R.id.btn_apply);
        tv_reason=view.findViewById(R.id.tv_reason);
        tv_universal_header.setText("Leave");

        mDrawerLayout = findViewById(R.id.drawer_layout);

    }

    private void fontSet() {
        et_from.setTypeface(MethodUtils.getNormalFont(LeaveActivity.this));
        et_to.setTypeface(MethodUtils.getNormalFont(LeaveActivity.this));
        et_type.setTypeface(MethodUtils.getNormalFont(LeaveActivity.this));
        tv_reason.setTypeface(MethodUtils.getNormalFont(LeaveActivity.this));
        btn_report.setTypeface(MethodUtils.getBoldFont(LeaveActivity.this));
        btn_approval.setTypeface(MethodUtils.getBoldFont(LeaveActivity.this));
        btn_apply.setTypeface(MethodUtils.getBoldFont(LeaveActivity.this));
    }

    private int totalItemCount, lastVisibleCount, totalItemCount1, lastVisibleCount1;
    LinearLayoutManager mLayoutManager;
    private boolean loading1 = false;

    private void setLeaveHistoryRecyclerView() {
        adapter = new LeaveHistoryAdapter(LeaveActivity.this,list);
        rv_items.setItemAnimator(null);
        //LinearLayoutManager mLayoutManager = new LinearLayoutManager(LeaveActivity.this);
        mLayoutManager = new LinearLayoutManager(LeaveActivity.this);
        rv_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        rv_items.setAdapter(adapter);



        rv_items.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount1 = mLayoutManager.getItemCount();
                lastVisibleCount1 = mLayoutManager.findLastCompletelyVisibleItemPosition();


                if (lastVisibleCount1 == totalItemCount1 - 1) {
                    if (list.size() % 10 == 0) {
                        //   leaveList.add(null);
                        //        leaveListAdapter.notifyItemInserted(leaveList.size() - 1);
                        loading1 = true;
                        page1++;
                        if (NetworkCheck.getInstant(LeaveActivity.this).isConnectingToInternet()) {
                            setLeaveHistoryApi();
                        } else {
                            MethodUtils.errorMsg(LeaveActivity.this, "Please check your phone's network connection");
                        }

                    }
                }



            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_from:
                //ExpiryDialog(et_from);
                openCalendar(et_from);
                break;
            case R.id.et_to:
                //ExpiryDialog(et_to);
                openCalendarToDate(et_to);
                break;
            case R.id.btn_apply:
                checkValidation();
                //leaveApplyApi();
                break;
            case R.id.et_type:
                hideSoftKeyBoard();
                if (leavePopup != null && leavePopup.isShowing()) {
                    leavePopup.dismiss();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showAndDismissLeavePopup();
                        }
                    }, 100);
                }
                break;
            case R.id.btn_report:
                btn_report.setBackgroundColor(Color.parseColor("#2a4e68"));
                btn_approval.setBackgroundColor(Color.parseColor("#2daada"));
                rv_items.setVisibility(View.GONE);
                rl_bottom.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_approval:
                btn_report.setBackgroundColor(Color.parseColor("#2daada"));
                btn_approval.setBackgroundColor(Color.parseColor("#2a4e68"));
                rv_items.setVisibility(View.VISIBLE);
                rl_bottom.setVisibility(View.GONE);
                setLeaveHistoryApi();
                break;

            case R.id.img_topbar_menu:
                if (isDrawerOpen())
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;

            case R.id.tv_logout:
                //logoutApi();

                logoutCall();


                break;

            case R.id.tv_dashboard:
                if (isDrawerOpen()) {
                    mDrawerLayout.closeDrawers();
                }
                Intent intent = new Intent(LeaveActivity.this, DashBoardActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.iv_cross:
                if (isDrawerOpen()) {
                    mDrawerLayout.closeDrawers();
                }
                break;
        }
    }

    private double currentLat;
    private double currentLng;

    private void logoutCall() {


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



        System.out.println("loginTime: " + LoginShared.getAttendanceAddDataModel(LeaveActivity.this)
                .getResult().getLoginTime().toString());

        String loginTime = LoginShared.getAttendanceAddDataModel(LeaveActivity.this)
                .getResult().getLoginTime().toString();


        String[] separated = loginTime.split("T");

                /*String sep1 = separated[0];
                String sep2 = separated[1];*/
        //attendanceViewHolder.tvLoginValue.setText(separated[1]);

        String loginTimeFormat = separated[1];

        loginTimePeriodic = separated[1];


        //SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");


        try
        {

            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
            Date date1 = format.parse(loginTimeFormat);
            Date date2 = format.parse(getCurrentTimeUsingDate());
            long mills = date2.getTime() - date1.getTime();
            Log.v("Data1", ""+date1.getTime());
            Log.v("Data2", ""+date2.getTime());
            int hours = (int) (mills/(1000 * 60 * 60));
            int mins = (int) (mills/(1000*60)) % 60;

            String diff = hours + ":" + mins; // updated value every1 second

            Toast.makeText(getApplicationContext(), diff, Toast.LENGTH_SHORT);

            System.out.println("difference: "+diff);


            if (hours < 10) {

                callAttandanceEditApi();
            } else {
                callAttendandanceLogoutApi();
            }


            //txtCurrentTime.setText(diff);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }







    }

    private void callAttendandanceLogoutApi() {

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
                        + LoginShared.getLoginDataModel(LeaveActivity.this).getToken(),
                LoginShared.getAttendanceAddDataModel(LeaveActivity.this).getResult().getId().toString(),
                object);


        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 201 || response.code() == 200) {

                    try {
                        String responseString = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optString("msg").equals("Success")) {

                            navigateToLoginPage();
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

    private void navigateToLoginPage() {
        LoginShared.destroySessionTypePreference();
        stopService(new Intent(LeaveActivity.this, BackgroundLocationService.class));
        BackgroundLocationService.stoplocationservice();
        Intent logIntent = new Intent(LeaveActivity.this, LoginActivity.class);
        startActivity(logIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void callAttandanceEditApi() {

        Intent myintent = new Intent(getApplicationContext(), LogoutDialogue.class);
        startActivity(myintent);
    }

    private void openCalendarToDate(final EditText et_to) {

        picker = new DatePickerDialog(LeaveActivity.this,

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        et_to.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        toDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    }
                }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        picker.show();
    }


    private void openCalendar(final EditText et_from) {

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);


        picker = new DatePickerDialog(LeaveActivity.this,

                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        //picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        //tv.setText(year + "-" + monthInString + "-" + dayInString);
                        //et_from.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        et_from.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        fromDate = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    }
                }, year, month, day);
        picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        picker.show();

    }

    private void checkValidation() {

        if (et_type.getText().toString().equals("")){

            MethodUtils.errorMsg(LeaveActivity.this, "Please select type of leave");

        }else if (et_from.getText().toString().trim().equals("")){

            MethodUtils.errorMsg(LeaveActivity.this, "Please select From date");

        }else if (et_to.getText().toString().trim().equals("")){

            MethodUtils.errorMsg(LeaveActivity.this, "Please select To date");

        }else if(tv_reason.getText().toString().trim().equals("")){

            MethodUtils.errorMsg(LeaveActivity.this, "Please enter proper reason for leave");

        }else{
            leaveApplyApi();
        }
    }

    public String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss";
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



    private void leaveApplyApi() {
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("leave_type", et_type.getText().toString().trim());
        object.addProperty("start_date", et_from.getText().toString().trim()+ "T" + getCurrentTimeUsingDate());
        object.addProperty("end_date", et_to.getText().toString().trim()+ "T" + getCurrentTimeUsingDate());
        object.addProperty("reason", tv_reason.getText().toString().trim());

        System.out.println("LeaveApply: "+object.toString());


        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_leaveApplyApi("Token "
                + LoginShared.getLoginDataModel(LeaveActivity.this).getToken(),"application/json",object);
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 201 || response.code() == 200) {
                        String responseString = response.body().string();
                        System.out.println("leaveApplyString: "+responseString);
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(responseString);

                        et_from.setText("");
                        et_to.setText("");
                        et_type.setText("");
                        tv_reason.setText("");


                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(LeaveActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(LeaveActivity.this, LeaveActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                MethodUtils.errorMsg(LeaveActivity.this, LeaveActivity.this.getString(R.string.error_occurred));
            }
        });


    }

    private void showAndDismissLeavePopup() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                leavePopup.showAsDropDown(rl_type);
            }
        }, 100);
    }

    private void hideSoftKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void ExpiryDialog(final EditText tv) {
        Calendar mCalendar;
        mCalendar = Calendar.getInstance();
        System.out.println("Inside Dialog Box");
        final Dialog dialog = new Dialog(LeaveActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_date_picker);
        dialog.show();
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.date_picker);
        Button date_time_set = (Button) dialog.findViewById(R.id.date_time_set);
        datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), null);

        LinearLayout ll = (LinearLayout) datePicker.getChildAt(0);
        LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

       /* if (currentapiVersion > 23) {
            ll2.getChildAt(1).setVisibility(View.GONE);
        } else if (currentapiVersion == 23) {
            ll2.getChildAt(0).setVisibility(View.GONE);
        } else {
            ll2.getChildAt(1).setVisibility(View.GONE);
        }*/

        date_time_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                month = datePicker.getMonth() + 1;
                year = datePicker.getYear();
                day = datePicker.getDayOfMonth();
                String monthInString = "" + month;
                String dayInString = "" + day;
                if (monthInString.length() == 1)
                    monthInString = "0" + monthInString;
                if (dayInString.length() == 1) {
                    dayInString = "0" + dayInString;
                }
                //tv.setText(dayInString + "-" + monthInString + "-" + year);
                tv.setText(year + "-" + monthInString + "-" + dayInString);
            }
        });
    }
}
