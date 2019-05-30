package com.pmsadmin.attendancelist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.attendancelist.adapter.AttendanceApprovalListAdapter;
import com.pmsadmin.attendancelist.adapter.AttendanceReportListAdapter;
import com.pmsadmin.attendancelist.approvallistmodel.ApprovalListModel;
import com.pmsadmin.attendancelist.markergetmodel.MarkerAddModel;
import com.pmsadmin.attendancelist.reportlistmodel.ReportListModel;
import com.pmsadmin.attendancelist.reportlistmodel.Result;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.filter.FilterActivity;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.giveattandence.listattandencemodel.AttendanceListModel;
import com.pmsadmin.giveattandence.updatedattandenceListModel.UpdatedAttendanceListModel;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.login.model.LoginModel;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.networking.NetworkCheck;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.SpacesItemDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AttendanceListActivity extends BaseActivity implements View.OnClickListener {
    public View view;
    RecyclerView rv_items_report, rv_items_approval;
    Button btn_report, btn_approval;
    RelativeLayout rl_pending, rl_filter;
    LinearLayout ll_second_header;
    TextView tv_filter, tv_search, tv_pending;
    EditText et_search;
    private LoadingData loader;
    AttendanceReportListAdapter adapter;
    AttendanceApprovalListAdapter attendanceApprovalListAdapter;
    private int page = 1;
    private int lastVisibleItem = 0;
    private int totalItemCount, lastVisibleCount;
    private boolean loading = false;
    List<Result> list = new ArrayList<>();
    List<com.pmsadmin.attendancelist.approvallistmodel.Result> approvalList = new ArrayList<>();

    String user_project = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = View.inflate(this, R.layout.activity_attendance_list, null);
        addContentView(view);
        loader = new LoadingData(AttendanceListActivity.this);
        MethodUtils.setStickyBar(AttendanceListActivity.this);
        approvalList.clear();
        bindView();
        fontSet();
        setClickEvent();
        list.clear();
        approvalList.clear();


        Intent intent = getIntent();

// Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("niceString")) {
                user_project  = extras.getString("niceString");

                // TODO: Do something with the value of isNew.
            }

            System.out.println("project_id: "+ user_project );
        }


        try {
            setReportsRecyclerView();
            getAttandenceListing();
            callMakerGetApi();
            //setApprovalRecyclerView();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void callMakerGetApi() {
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_markerGetApi("Token "
                + LoginShared.getLoginDataModel(getApplicationContext()).getToken(),
                LoginShared.getAttendanceAddDataModel(AttendanceListActivity.this).getResult().getId().toString());
        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                try {
                    if (response.code() == 200 || response.code()==201) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        MarkerAddModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            loginModel = gson.fromJson(responseString, MarkerAddModel.class);
                            LoginShared.setMarkerListDataModel(AttendanceListActivity.this, loginModel);
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
                MethodUtils.errorMsg(AttendanceListActivity.this, AttendanceListActivity.this.getString(R.string.error_occurred));
            }
        });
    }

    private void fontSet() {
        tv_filter.setTypeface(MethodUtils.getBoldFont(AttendanceListActivity.this));
        tv_search.setTypeface(MethodUtils.getBoldFont(AttendanceListActivity.this));
        tv_pending.setTypeface(MethodUtils.getBoldFont(AttendanceListActivity.this));
        et_search.setTypeface(MethodUtils.getNormalFont(AttendanceListActivity.this));
        et_search.setTypeface(MethodUtils.getNormalFont(AttendanceListActivity.this));
        btn_report.setTypeface(MethodUtils.getBoldFont(AttendanceListActivity.this));
        btn_approval.setTypeface(MethodUtils.getBoldFont(AttendanceListActivity.this));
    }

    private void getApprovalListing() {
        if (!loading) {
            loader.show_with_label("Loading");
        }
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_approvalListingApi("Token "
                + LoginShared.getLoginDataModel(AttendanceListActivity.this).getToken(), String.valueOf(page));

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (page > 1) {
                    try {
                        approvalList.remove(approvalList.size() - 1);
                        adapter.notifyItemRemoved(approvalList.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (response.code() == 201 || response.code() == 200) {
                        String responseString = response.body().string();
                        Gson gson = new Gson();
                        ApprovalListModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {
                            loginModel = gson.fromJson(responseString, ApprovalListModel.class);
                            LoginShared.setApprovalListModel(AttendanceListActivity.this, loginModel);
                            approvalList.addAll(LoginShared.getApprovalListModel(AttendanceListActivity.this).getResults());
                            attendanceApprovalListAdapter.notifyDataSetChanged();
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


    private void getAttandenceListing() {
        if (!loading) {
            loader.show_with_label("Loading");
        }
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        /*final Call<ResponseBody> register = apiInterface.call_reportListingApi("Token "
                + LoginShared.getLoginDataModel(AttendanceListActivity.this).getToken(),
                "application/json",  String.valueOf(page));*/

        final Call<ResponseBody> register = apiInterface.call_reportListingApi("Token "
                + LoginShared.getLoginDataModel(AttendanceListActivity.this).getToken(),
                "application/json", user_project, String.valueOf(page));

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (page > 1) {
                    try {
                        list.remove(list.size() - 1);
                        adapter.notifyItemRemoved(list.size());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (response.code() == 201 || response.code() == 200) {
                        String responseString = response.body().string();

                        System.out.println("reportResponse: "+response.body().string());

                        Gson gson = new Gson();
                        ReportListModel loginModel;
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (jsonObject.optInt("request_status") == 1) {
                            loginModel = gson.fromJson(responseString, ReportListModel.class);
                            LoginShared.setReportListDataModel(AttendanceListActivity.this, loginModel);
                            list.addAll(LoginShared.getReportListDataModel(AttendanceListActivity.this).getResults());
                            adapter.notifyDataSetChanged();
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
               // MethodUtils.errorMsg(AttendanceListActivity.this, AttendanceListActivity.this.getString(R.string.error_occurred));
            }
        });

    }

    private void setApprovalRecyclerView() {
        /*attendanceApprovalListAdapter = new AttendanceApprovalListAdapter(
                AttendanceListActivity.this,
                LoginShared.getApprovalListModel(AttendanceListActivity.this));
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(AttendanceListActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_items_approval.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items_approval.addItemDecoration(decoration);
        rv_items_approval.setAdapter(adapter);*/
        attendanceApprovalListAdapter = new AttendanceApprovalListAdapter(
                AttendanceListActivity.this, approvalList);
        rv_items_approval.setAdapter(attendanceApprovalListAdapter);
        rv_items_approval.setItemAnimator(new DefaultItemAnimator());
        final GridLayoutManager mLayoutManager = new GridLayoutManager(AttendanceListActivity.this, 1);
        rv_items_approval.setLayoutManager(mLayoutManager);

        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items_approval.addItemDecoration(decoration);

        rv_items_approval.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleCount = mLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisibleCount == totalItemCount - 1) {
                    if (approvalList.size() % 10 == 0) {
                        approvalList.add(null);
                        attendanceApprovalListAdapter.notifyItemInserted(approvalList.size() - 1);
                        loading = true;
                        page++;
                        if (NetworkCheck.getInstant(AttendanceListActivity.this).isConnectingToInternet()) {
                            getApprovalListing();
                        } else {
                            MethodUtils.errorMsg(AttendanceListActivity.this, "Please check your phone's network connection");
                        }

                    }
                }
            }
        });
    }

    private void setClickEvent() {
        //btn_report.setOnClickListener(this);
        //btn_approval.setOnClickListener(this);
        rl_filter.setOnClickListener(this);
    }

    private void setReportsRecyclerView() {
       /* adapter = new AttendanceReportListAdapter(AttendanceListActivity.this,
                list);
        rv_items_report.setItemAnimator(null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(AttendanceListActivity.this);
        rv_items_report.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items_report.addItemDecoration(decoration);
        rv_items_report.setAdapter(adapter);*/

        adapter = new AttendanceReportListAdapter(
                AttendanceListActivity.this, list);
        rv_items_report.setAdapter(adapter);
        rv_items_report.setItemAnimator(new DefaultItemAnimator());
        final GridLayoutManager mLayoutManager = new GridLayoutManager(AttendanceListActivity.this, 1);
        rv_items_report.setLayoutManager(mLayoutManager);

        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items_report.addItemDecoration(decoration);

        rv_items_report.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleCount = mLayoutManager.findLastCompletelyVisibleItemPosition();

                if (lastVisibleCount == totalItemCount - 1) {
                    if (list.size() % 10 == 0) {
                        list.add(null);
                        adapter.notifyItemInserted(list.size() - 1);
                        loading = true;
                        page++;
                        if (NetworkCheck.getInstant(AttendanceListActivity.this).isConnectingToInternet()) {
                            //getApprovalListing();
                            getAttandenceListing();
                        } else {
                            MethodUtils.errorMsg(AttendanceListActivity.this, "Please check your phone's network connection");
                        }

                    }
                }
            }
        });
    }

    private void bindView() {
        tv_universal_header.setText("Attendance");
        rv_items_report = findViewById(R.id.rv_items_report);
        rv_items_approval = findViewById(R.id.rv_items_approval);
        btn_report = findViewById(R.id.btn_report);
        btn_approval = findViewById(R.id.btn_approval);
        rl_pending = findViewById(R.id.rl_pending);
        ll_second_header = findViewById(R.id.ll_second_header);
        rl_filter = findViewById(R.id.rl_filter);
        tv_filter = findViewById(R.id.tv_filter);
        tv_search = findViewById(R.id.tv_search);
        tv_pending = findViewById(R.id.tv_pending);
        et_search = findViewById(R.id.et_search);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_report:
                btn_report.setBackgroundColor(Color.parseColor("#2a4e68"));
                btn_approval.setBackgroundColor(Color.parseColor("#2daada"));
                rl_pending.setVisibility(View.GONE);
                rv_items_report.setVisibility(View.VISIBLE);
                rv_items_approval.setVisibility(View.GONE);
                ll_second_header.setVisibility(View.VISIBLE);
                //setReportsRecyclerView();
                getAttandenceListing();
                break;
            case R.id.btn_approval:
                btn_report.setBackgroundColor(Color.parseColor("#2daada"));
                btn_approval.setBackgroundColor(Color.parseColor("#2a4e68"));
                rl_pending.setVisibility(View.VISIBLE);
                rv_items_report.setVisibility(View.GONE);
                rv_items_approval.setVisibility(View.VISIBLE);
                ll_second_header.setVisibility(View.GONE);
                getApprovalListing();
                break;

            case R.id.rl_filter:
                Intent profileIntent = new Intent(AttendanceListActivity.this, FilterActivity.class);
                startActivity(profileIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }
}
