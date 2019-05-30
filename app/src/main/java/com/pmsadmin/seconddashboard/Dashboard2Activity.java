package com.pmsadmin.seconddashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.attendancelist.ApprovalListActivity;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.filter.FilterActivity;
import com.pmsadmin.filter.adapter.ProjectAdapter;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.seconddashboard.adapter.ItemsAdapterDashboardTiles;
import com.pmsadmin.seconddashboard.adapter.ProjectListAdapter;
import com.pmsadmin.seconddashboard.project_list_model.ProjectListModel;
import com.pmsadmin.seconddashboard.project_list_model.Result;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Dashboard2Activity extends BaseActivity {
    public View view;
    /*TextView tv_tender,tv_archived,tv_app;*/
    RecyclerView rv_items, rv_items_checkbox;
    private GridLayoutManager mLayoutManager;

    private LoadingData loader;
    public TextView tv_universal_header;


    private List<Result> projectList = new ArrayList<>();
    private ProjectListModel projectListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_dashboard2, null);
        addContentView(view);

        loader = new LoadingData(Dashboard2Activity.this);

        setViewBind();
        setFont();
        //setRecyclerView();
        //setCheckBoxRecyclerView();

        getProjectList();
    }

    private void getProjectList() {

        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);


        final Call<ResponseBody> register = apiInterface.call_projectList("Token "
                + LoginShared.getLoginDataModel(Dashboard2Activity.this).getToken());

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (loader != null && loader.isShowing())
                    loader.dismiss();

                if (response.code() == 201 || response.code() == 200) {

                    try {
                        String responseString = response.body().string();
                        //JSONObject jsonObject = new JSONObject(responseString);

                        Gson gson = new Gson();
                        projectListModel = gson.fromJson(responseString, ProjectListModel.class);
                        projectList = projectListModel.getResult();


                        System.out.println("responseProject: " + projectList.size());

                        setAdapterForChkBox(projectList);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void setAdapterForChkBox(List<Result> projectList) {

        ProjectListAdapter projectListAdapter = new ProjectListAdapter(Dashboard2Activity.this, projectList);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(Dashboard2Activity.this, RecyclerView.VERTICAL, false);
        rv_items_checkbox.setHasFixedSize(true);
        rv_items_checkbox.setLayoutManager(linearLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items_checkbox.addItemDecoration(decoration);
        rv_items_checkbox.setAdapter(projectListAdapter);

        setRecyclerView(projectList);
    }

    private void setCheckBoxRecyclerView() {
        ProjectAdapter adapter = new ProjectAdapter(Dashboard2Activity.this, MethodUtils.getProjectItems());
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(Dashboard2Activity.this, RecyclerView.VERTICAL, false);
        rv_items_checkbox.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items_checkbox.addItemDecoration(decoration);
        rv_items_checkbox.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void setRecyclerView(List<Result> projectList) {
        ItemsAdapterDashboardTiles adapter = new ItemsAdapterDashboardTiles(Dashboard2Activity.this,
                MethodUtils.getSecond2DashboardItems(), projectList);
        rv_items.setAdapter(adapter);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new GridLayoutManager(this, 2);
        rv_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(Dashboard2Activity.this, 2);
        rv_items.addItemDecoration(itemOffset);
       /* GridSpanSizeLookupForListDetailsAdapter headerSpanSizeLookup = new GridSpanSizeLookupForListDetailsAdapter(adapter, mLayoutManager);
        mLayoutManager.setSpanSizeLookup(headerSpanSizeLookup);*/
    }

    private void setFont() {

    }

    private void setViewBind() {
        rv_items = findViewById(R.id.rv_items);
        rv_items_checkbox = findViewById(R.id.rv_items_checkbox);
        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Attendance");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(Dashboard2Activity.this));

    }
}
