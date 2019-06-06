package com.pmsadmin.survey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pmsadmin.GridSpanSizeLookUp.GridSpanSizeLookupForListDetailsAdapter;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.dashboard.adapter.ItemsAdapterTiles;
import com.pmsadmin.survey.adapter.StartSurveyStaticAdapter;
import com.pmsadmin.survey.resource.EstablishmentActivity;
import com.pmsadmin.survey.resource.adpater.EstablishmentAdapter;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

public class StartSurveyHome extends BaseActivity {

    private String tenderGID = "";
    private TextView tvTenderGID;
    private RecyclerView rv_items;
    StartSurveyStaticAdapter adapter;
    public View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_start_survey_home2, null);
        addContentView(view);
        //setContentView(R.layout.activity_start_survey_home2);

        tvTenderGID = findViewById(R.id.tvTenderGID);
        rv_items = (RecyclerView) findViewById(R.id.rvItems);

        Intent intent = getIntent();
        tenderGID = intent.getStringExtra("tenderGID");
        System.out.println("tenderGID======>>>"+tenderGID);
        MethodUtils.tender_id = intent.getIntExtra("tender_id",0);
        tvTenderGID.setText("Tender ID: "+tenderGID);
        System.out.println("tenderID: "+String.valueOf(MethodUtils.tender_id));

        adapter = new StartSurveyStaticAdapter(StartSurveyHome.this,MethodUtils.getItemsSurvey());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        rv_items.setLayoutManager(layoutManager);
        rv_items.setHasFixedSize(true);
        rv_items.setAdapter(adapter);


    }

    private void initLayout() {



        setAdapter();

    }

    private void setAdapter() {

        /*StartSurveyStaticAdapter adapter = new StartSurveyStaticAdapter(StartSurveyHome.this, MethodUtils.getItemsSurvey());
        rv_items.setAdapter(adapter);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rv_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(StartSurveyHome.this, 2);
        rv_items.addItemDecoration(itemOffset);
        *//*GridSpanSizeLookupForListDetailsAdapter headerSpanSizeLookup = new GridSpanSizeLookupForListDetailsAdapter(adapter, mLayoutManager);
        mLayoutManager.setSpanSizeLookup(headerSpanSizeLookup);*/



    }
}
