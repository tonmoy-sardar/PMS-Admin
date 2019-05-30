package com.pmsadmin.survey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.pmsadmin.GridSpanSizeLookUp.GridSpanSizeLookupForListDetailsAdapter;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.dashboard.adapter.ItemsAdapterTiles;
import com.pmsadmin.survey.adapter.StartSurveyStaticAdapter;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

public class StartSurveyHome extends AppCompatActivity {

    private String tenderGID = "";
    private TextView tvTenderID;
    private RecyclerView rv_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_survey_home2);

        Intent intent = getIntent();
        tenderGID = intent.getStringExtra("tenderGID");
        MethodUtils.tender_id = intent.getIntExtra("tender_id",0);

        System.out.println("tenderID: "+String.valueOf(MethodUtils.tender_id));


        initLayout();
    }

    private void initLayout() {

        tvTenderID = (TextView) findViewById(R.id.tvTenderID);
        tvTenderID.setText("Tender ID: "+tenderGID);

        rv_items = (RecyclerView) findViewById(R.id.rvItems);

        setAdapter();

    }

    private void setAdapter() {

        StartSurveyStaticAdapter adapter = new StartSurveyStaticAdapter(StartSurveyHome.this, MethodUtils.getItemsSurvey());
        rv_items.setAdapter(adapter);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rv_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(StartSurveyHome.this, 2);
        rv_items.addItemDecoration(itemOffset);
        /*GridSpanSizeLookupForListDetailsAdapter headerSpanSizeLookup = new GridSpanSizeLookupForListDetailsAdapter(adapter, mLayoutManager);
        mLayoutManager.setSpanSizeLookup(headerSpanSizeLookup);*/
    }
}
