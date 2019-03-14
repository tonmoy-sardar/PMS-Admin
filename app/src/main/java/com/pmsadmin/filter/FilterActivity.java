package com.pmsadmin.filter;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.filter.adapter.DesignationAdapter;
import com.pmsadmin.filter.adapter.ProjectAdapter;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.giveattandence.listattandencemodel.AttendanceListModel;
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

public class FilterActivity extends BaseActivity {

    public View view;
    RecyclerView rv_project,rv_designation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_filter, null);
        addContentView(view);
        MethodUtils.setStickyBar(FilterActivity.this);
        BindView();
        setProjectRecyclerView();
        setDesignationRecyclerView();
    }

    private void setDesignationRecyclerView() {
        DesignationAdapter adapter = new DesignationAdapter(FilterActivity.this);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(FilterActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_designation.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_designation.addItemDecoration(decoration);
        rv_designation.setAdapter(adapter);
    }

    private void setProjectRecyclerView() {
        ProjectAdapter adapter = new ProjectAdapter(FilterActivity.this);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(FilterActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_project.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_project.addItemDecoration(decoration);
        rv_project.setAdapter(adapter);
    }

    private void BindView() {
        tv_universal_header.setText("Filter-Attendance Report");
        iv_close.setVisibility(View.VISIBLE);
        rv_project=findViewById(R.id.rv_project);
        rv_designation=findViewById(R.id.rv_designation);
    }
}
