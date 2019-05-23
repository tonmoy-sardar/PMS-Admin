package com.pmsadmin.planreport;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.adapter.SiteListAdapter;
import com.pmsadmin.addsite.AddSiteActivity;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.attendancelist.ApprovalListActivity;
import com.pmsadmin.attendancelist.approvallistmodel.ApprovalListModel;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.login.LoginActivity;
import com.pmsadmin.login.model.SiteList;
import com.pmsadmin.netconnection.ConnectionDetector;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.pmsadmin.MethodUtils.errorMsg;

public class PlanningReports extends BaseActivity {
    public View view;
    private LoadingData loader;
    ArrayList<SiteList> siteListList=new ArrayList<SiteList>();
    RecyclerView recyclerview;
    SiteListAdapter siteListAdapter;
    LinearLayout block3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_planning_report, null);
        addContentView(view);
        bindView();
        //setClickEvent();
        loader = new LoadingData(PlanningReports.this);

        if (!ConnectionDetector.isConnectingToInternet(PlanningReports.this)) {
            errorMsg(PlanningReports.this, PlanningReports.this.getString(R.string.no_internet));
        }else {
            callSiteListApi();
        }

    }
    private void bindView() {

        recyclerview=(RecyclerView)findViewById(R.id.recyclerview);
        block3=(LinearLayout)findViewById(R.id.block3);

        recyclerview.setNestedScrollingEnabled(false);
        recyclerview.setHasFixedSize(true);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        block3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PlanningReports.this,AddDailyData.class);
                startActivity(intent);

            }
        });
    }
    private void callSiteListApi()
    {
      //  approvalList.clear();
       // if (!loading) {
            loader.show_with_label("Loading");
      //  }
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_projectlistApi("Token "
                + LoginShared.getLoginDataModel(PlanningReports.this).getToken());

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                    try{
                        if (response.code() == 200)
                        {

                            String responseString = response.body().string();
                            Log.d("responsestring",responseString);

                            JSONObject jsonObject=new JSONObject(responseString);
                           int request_status= jsonObject.getInt("request_status");
                           if(request_status==1)
                           {
                            JSONArray result= jsonObject.getJSONArray("result");
                            if(result.length()>0)
                            {

                                for(int i=0;i<result.length();i++)
                                {
                                    JSONObject jsonObject1=result.getJSONObject(i);
                                    int id=jsonObject1.getInt("id");
                                    String name=jsonObject1.getString("name");
                                    int type=jsonObject1.getInt("type");
                                    SiteList siteList=new SiteList(id,name,type);
                                    siteListList.add(siteList);

                                }
                                setApdater();

                            }

                           }

                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        errorMsg(PlanningReports.this, PlanningReports.this.getString(R.string.error_occurred));
                    }



            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
            }
        });
    }

    private void setApdater() {
        Log.d("ListSize", String.valueOf(siteListList.size()));
        siteListAdapter=new SiteListAdapter(PlanningReports.this,siteListList);
        recyclerview.setItemViewCacheSize(siteListList.size());
        recyclerview.setAdapter(siteListAdapter);
    }
}
