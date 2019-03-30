package com.pmsadmin.leavesection;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.attendancelist.AttendanceListActivity;
import com.pmsadmin.attendancelist.reportlistmodel.ReportListModel;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.dialog.universalpopup.UniversalPopup;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.giveattandence.addattandencemodel.AttendanceAddModel;
import com.pmsadmin.leavesection.adapter.LeaveHistoryAdapter;
import com.pmsadmin.leavesection.leavehistorymodel.LeaveHistoryModel;
import com.pmsadmin.leavesection.leavehistorymodel.Result;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.SpacesItemDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_leave, null);
        addContentView(view);
        loader = new LoadingData(LeaveActivity.this);
        bindView();
        setClickEvent();
        addLeaveListAndCall();
        //setLeaveHistoryRecyclerView();
    }

    private void setLeaveHistoryApi() {
        list.clear();
        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final  Call<ResponseBody> register=apiInterface.call_leaveHistoryApi("Token "
                        + LoginShared.getLoginDataModel(LeaveActivity.this).getToken(),
                "application/json",LoginShared.getLoginDataModel(LeaveActivity.this).getUserId().toString());

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

                        if (jsonObject.optInt("request_status") == 1) {
                            loginModel = gson.fromJson(responseString, LeaveHistoryModel.class);
                            LoginShared.setLeaveHistoryDataModel(LeaveActivity.this, loginModel);
                            list.addAll(LoginShared.getLeaveHistoryDataModel(LeaveActivity.this).getResults());
                            setLeaveHistoryRecyclerView();
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
    }

    private void setLeaveHistoryRecyclerView() {
        adapter = new LeaveHistoryAdapter(LeaveActivity.this,list);
        rv_items.setItemAnimator(null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(LeaveActivity.this);
        rv_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        rv_items.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.et_from:
                ExpiryDialog(et_from);
                break;
            case R.id.et_to:
                ExpiryDialog(et_to);
                break;
            case R.id.btn_apply:
                leaveApplyApi();
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

    private void leaveApplyApi() {
        loader.show_with_label("Loading");
        JsonObject object = new JsonObject();
        object.addProperty("leave_type", et_type.getText().toString().trim());
        object.addProperty("start_date", et_from.getText().toString().trim()+ "T" + getCurrentTimeUsingDate());
        object.addProperty("end_date", et_to.getText().toString().trim()+ "T" + getCurrentTimeUsingDate());
        object.addProperty("reason", tv_reason.getText().toString().trim());
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
                        Gson gson = new Gson();
                        JSONObject jsonObject = new JSONObject(responseString);

                        //if (jsonObject.optInt("request_status") == 1) {
                            /*loginModel = gson.fromJson(responseString, AttendanceAddModel.class);
                            LoginShared.setAttendanceAddDataModel(DashBoardActivity.this, loginModel);*/
                            MethodUtils.errorMsg(LeaveActivity.this, jsonObject.optString("msg"));
                        //} else if (jsonObject.optInt("request_status") == 0) {
                           // MethodUtils.errorMsg(LeaveActivity.this, jsonObject.optString("msg"));
                       // } else {
                            //MethodUtils.errorMsg(LeaveActivity.this, LeaveActivity.this.getString(R.string.error_occurred));
                        //}
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
