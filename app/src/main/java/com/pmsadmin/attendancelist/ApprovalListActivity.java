package com.pmsadmin.attendancelist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Bundle;
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
import com.pmsadmin.attendancelist.approvallistmodel.ApprovalListModel;
import com.pmsadmin.attendancelist.approvallistmodel.Result;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.networking.NetworkCheck;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.SpacesItemDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApprovalListActivity extends BaseActivity {
    public View view;
    RecyclerView rv_items_report, rv_items_approval;
    Button btn_report, btn_approval;
    RelativeLayout rl_pending, rl_filter;
    LinearLayout ll_second_header;
    TextView tv_filter, tv_search, tv_pending;
    EditText et_search;
    private boolean loading = false;
    AttendanceApprovalListAdapter attendanceApprovalListAdapter;
    private int page = 1;
    private int lastVisibleItem = 0;
    private int totalItemCount, lastVisibleCount;
    List<Result> approvalList = new ArrayList<>();
    private LoadingData loader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_approval_list, null);
        addContentView(view);
        loader = new LoadingData(ApprovalListActivity.this);
        bindView();
        fontSet();
        setApprovalRecyclerView();
        getApprovalListing();

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

    private void fontSet() {
        tv_filter.setTypeface(MethodUtils.getBoldFont(ApprovalListActivity.this));
        tv_search.setTypeface(MethodUtils.getBoldFont(ApprovalListActivity.this));
        tv_pending.setTypeface(MethodUtils.getBoldFont(ApprovalListActivity.this));
        et_search.setTypeface(MethodUtils.getNormalFont(ApprovalListActivity.this));
        et_search.setTypeface(MethodUtils.getNormalFont(ApprovalListActivity.this));
        btn_report.setTypeface(MethodUtils.getBoldFont(ApprovalListActivity.this));
        btn_approval.setTypeface(MethodUtils.getBoldFont(ApprovalListActivity.this));
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
                ApprovalListActivity.this, approvalList);
        rv_items_approval.setAdapter(attendanceApprovalListAdapter);
        rv_items_approval.setItemAnimator(new DefaultItemAnimator());
        final GridLayoutManager mLayoutManager = new GridLayoutManager(ApprovalListActivity.this, 1);
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
                        if (NetworkCheck.getInstant(ApprovalListActivity.this).isConnectingToInternet()) {
                            getApprovalListing();
                        } else {
                            MethodUtils.errorMsg(ApprovalListActivity.this, "Please check your phone's network connection");
                        }

                    }
                }
            }
        });
    }

    private void getApprovalListing() {
        if (!loading) {
            loader.show_with_label("Loading");
        }
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_approvalListingApi("Token "
                + LoginShared.getLoginDataModel(ApprovalListActivity.this).getToken(), String.valueOf(page));

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (page > 1) {
                    try {
                        approvalList.remove(approvalList.size() - 1);
                        attendanceApprovalListAdapter.notifyItemRemoved(approvalList.size());
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
                            LoginShared.setApprovalListModel(ApprovalListActivity.this, loginModel);
                            approvalList.addAll(LoginShared.getApprovalListModel(ApprovalListActivity.this).getResults());
                            attendanceApprovalListAdapter.notifyDataSetChanged();
                        } else if (jsonObject.optInt("request_status") == 0) {
                            MethodUtils.errorMsg(ApprovalListActivity.this, jsonObject.optString("msg"));
                        } else {
                            MethodUtils.errorMsg(ApprovalListActivity.this, ApprovalListActivity.this.getString(R.string.error_occurred));
                        }
                    } else {
                        String responseString = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(responseString);
                        MethodUtils.errorMsg(ApprovalListActivity.this, jsonObject.optString("msg"));
                    }
                } catch (Exception e) {
                    MethodUtils.errorMsg(ApprovalListActivity.this, ApprovalListActivity.this.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
            }
        });
    }
}
