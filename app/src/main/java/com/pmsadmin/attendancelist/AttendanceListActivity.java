package com.pmsadmin.attendancelist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.attendancelist.adapter.AttendanceApprovalListAdapter;
import com.pmsadmin.attendancelist.adapter.AttendanceReportListAdapter;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.filter.FilterActivity;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.giveattandence.listattandencemodel.AttendanceListModel;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.SpacesItemDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AttendanceListActivity extends BaseActivity implements View.OnClickListener {
    public View view;
    RecyclerView rv_items_report,rv_items_approval;
    Button btn_report,btn_approval;
    RelativeLayout rl_pending,rl_filter;
    LinearLayout ll_second_header;
    private LoadingData loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_attendance_list, null);
        addContentView(view);
        loader = new LoadingData(AttendanceListActivity.this);
        MethodUtils.setStickyBar(AttendanceListActivity.this);
        bindView();
        setClickEvent();
        getAttandenceListing();
        setApprovalRecyclerView();
    }

    private void getAttandenceListing() {
        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_attendanceListingApi("Token "
                + LoginShared.getLoginDataModel(AttendanceListActivity.this).getToken());

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 201 || response.code()==200) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        AttendanceListModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            loginModel = gson.fromJson(responseString, AttendanceListModel.class);
                            LoginShared.setAttendanceListDataModel(AttendanceListActivity.this, loginModel);
                            setReportsRecyclerView();
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(AttendanceListActivity.this, jsonObject.optString("msg"));
                        } else {
                            MethodUtils.errorMsg(AttendanceListActivity.this, AttendanceListActivity.this.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(AttendanceListActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(AttendanceListActivity.this, AttendanceListActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
            }
        });

    }

    private void setApprovalRecyclerView() {
        AttendanceApprovalListAdapter adapter = new AttendanceApprovalListAdapter(AttendanceListActivity.this);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(AttendanceListActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_items_approval.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items_approval.addItemDecoration(decoration);
        rv_items_approval.setAdapter(adapter);
    }

    private void setClickEvent() {
        btn_report.setOnClickListener(this);
        btn_approval.setOnClickListener(this);
        rl_filter.setOnClickListener(this);
    }

    private void setReportsRecyclerView() {
        AttendanceReportListAdapter adapter = new AttendanceReportListAdapter(AttendanceListActivity.this,
                LoginShared.getAttendanceListDataModel(AttendanceListActivity.this).getResult());
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(AttendanceListActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_items_report.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items_report.addItemDecoration(decoration);
        rv_items_report.setAdapter(adapter);
    }
    private void bindView() {
        tv_universal_header.setText("Attendance");
        rv_items_report=findViewById(R.id.rv_items_report);
        rv_items_approval=findViewById(R.id.rv_items_approval);
        btn_report=findViewById(R.id.btn_report);
        btn_approval=findViewById(R.id.btn_approval);
        rl_pending=findViewById(R.id.rl_pending);
        ll_second_header=findViewById(R.id.ll_second_header);
        rl_filter=findViewById(R.id.rl_filter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.btn_report:
                btn_report.setBackgroundColor(Color.parseColor("#2a4e68"));
                btn_approval.setBackgroundColor(Color.parseColor("#2daada"));
                rl_pending.setVisibility(View.GONE);
                rv_items_report.setVisibility(View.VISIBLE);
                rv_items_approval.setVisibility(View.GONE);
                ll_second_header.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_approval:
                btn_report.setBackgroundColor(Color.parseColor("#2daada"));
                btn_approval.setBackgroundColor(Color.parseColor("#2a4e68"));
                rl_pending.setVisibility(View.VISIBLE);
                rv_items_report.setVisibility(View.GONE);
                rv_items_approval.setVisibility(View.VISIBLE);
                ll_second_header.setVisibility(View.GONE);
                break;

            case R.id.rl_filter:
                Intent profileIntent = new Intent(AttendanceListActivity.this, FilterActivity.class);
                startActivity(profileIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }
}
