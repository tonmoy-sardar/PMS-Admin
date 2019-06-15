package com.pmsadmin.survey.coordinates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.coordinate_adapter.CrusherListAdapter;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CrusherActivity extends BaseActivity {

    public View view;
    private LoadingData loader;
    private TextView tv_universal_header;
    RecyclerView rv_crusher_list;
    ArrayList<JSONObject> arrayList;
    CrusherListAdapter crusherListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_crusher, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        tv_universal_header = findViewById(R.id.tv_universal_header);
        rv_crusher_list = findViewById(R.id.rv_crusher_list);

        tv_universal_header.setText("Crusher List");
        loader = new LoadingData(CrusherActivity.this);

        arrayList = new ArrayList<JSONObject>();
        crusherListAdapter = new CrusherListAdapter(CrusherActivity.this, arrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_crusher_list.setLayoutManager(layoutManager);
        rv_crusher_list.setHasFixedSize(true);
        rv_crusher_list.setAdapter(crusherListAdapter);

        getCrusherList();
    }

    private void getCrusherList() {

        loader.show_with_label("Loading");
        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register=apiInterface.call_crusher_list_api("Token "
                        + LoginShared.getLoginDataModel(CrusherActivity.this).getToken(),
                "application/json");

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (loader != null && loader.isShowing())
                    loader.dismiss();
                System.out.println("Service URL==>" + response.raw().request().url());
                if (response.code() == 201 || response.code() == 200) {
                    try {
                        String responseString = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseString);
                        System.out.println("crusher list============>>>"+jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            arrayList.add(jsonArray.getJSONObject(i));
                        }
                        crusherListAdapter.notifyDataSetChanged();
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
}
