package com.pmsadmin.survey.coordinates;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import im.delight.android.location.SimpleLocation;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dialog.AddChkInDialogue;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.leavesection.LeaveActivity;
import com.pmsadmin.location.GPSTracker;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.coordinate_adapter.CheckInAdapter;
import com.pmsadmin.survey.coordinates.survey_location_model.Result;
import com.pmsadmin.survey.coordinates.survey_location_model.SurveyLocationListPojo;
import com.pmsadmin.tenders_list.TendorsAdapter;
import com.pmsadmin.tenders_list.TendorsListing;
import com.pmsadmin.tenders_list.tendors_pojo.TendorsListingPojo;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckInActivity extends BaseActivity {

    public View view;
    private TextView tv_universal_header, tvAdd;
    private RecyclerView rvProjects;

    List<Result> surveyResultResponse = new ArrayList<>();

    private CheckInAdapter checkInAdapter;
    private LinearLayoutManager mLayoutManager;

    public List<Address> addresses;
    Geocoder geocoder;

    private double currentLat;
    private double currentLng;

    private SimpleLocation location;

    private ImageView ivAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_check_in, null);
        addContentView(view);
        //setContentView(R.layout.activity_check_in);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Check In");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(CheckInActivity.this));

        System.out.println("tenderID: " + String.valueOf(MethodUtils.tender_id));

        rvProjects = (RecyclerView) findViewById(R.id.rvProjects);


        //gpsTracker = new GPSTracker(BaseActivity.this);
        geocoder = new Geocoder(CheckInActivity.this, Locale.getDefault());

        location = new SimpleLocation(this, false, false, 10000);

        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);


        }


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


        System.out.println("adress: " + addresses.get(0).getAddressLine(0));

        initLayout();

        /*callTenderSurveyLocationList();

        setAdapter();*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        location.beginUpdates();
        callTenderSurveyLocationList();

        setAdapter();

    }

    private void setAdapter() {

        checkInAdapter = new CheckInAdapter(CheckInActivity.this, surveyResultResponse);
        rvProjects.setAdapter(checkInAdapter);
        rvProjects.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(CheckInActivity.this, RecyclerView.VERTICAL, false);

        rvProjects.setLayoutManager(horizontalLayoutManager);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(CheckInActivity.this, 2);
        rvProjects.addItemDecoration(itemOffset);
    }

    private void callTenderSurveyLocationList() {

        Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        final Call<ResponseBody> register = apiInterface.call_tender_survey_location_list("Token "
                        + LoginShared.getLoginDataModel(CheckInActivity.this).getToken(),
                String.valueOf(MethodUtils.tender_id));

        register.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (surveyResultResponse != null) {
                    surveyResultResponse.clear();
                }

                if (response.code() == 201 || response.code() == 200) {
                    try {
                        String responseString = response.body().string();

                        JSONObject jsonObject = new JSONObject(responseString);

                        if (jsonObject.optInt("request_status") == 1) {

                            Gson gson = new Gson();
                            SurveyLocationListPojo surveyLocationListPojo;
                            surveyLocationListPojo = gson.fromJson(responseString, SurveyLocationListPojo.class);
                            //tendorsResultList = tendorsListingPojo.getResult();
                            surveyResultResponse.addAll(surveyLocationListPojo.getResult());

                            System.out.println("tendorsResultList: " + surveyResultResponse.size());
                            checkInAdapter.notifyDataSetChanged();

                            //tendorsAdapter.notifyDataSetChanged();
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

    private void initLayout() {
        //tvAdd = (TextView) findViewById(R.id.tvAdd);
        ivAdd = findViewById(R.id.ivAdd);
        //tvAdd.setTypeface(MethodUtils.getNormalFont(CheckInActivity.this));


        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AddChkInDialogue.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });
    }
}
