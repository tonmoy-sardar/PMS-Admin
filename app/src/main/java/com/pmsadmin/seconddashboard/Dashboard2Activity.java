package com.pmsadmin.seconddashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.pmsadmin.GridSpanSizeLookUp.GridSpanSizeLookupForListDetailsAdapter;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.dashboard.adapter.ItemsAdapterTiles;
import com.pmsadmin.seconddashboard.adapter.ItemsAdapterDashboardTiles;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

public class Dashboard2Activity extends AppCompatActivity {

    /*TextView tv_tender,tv_archived,tv_app;*/
    RecyclerView rv_items;
    private GridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        setViewBind();
        setFont();
        setRecyclerView();
    }

    private void setRecyclerView() {
        ItemsAdapterDashboardTiles adapter = new ItemsAdapterDashboardTiles(Dashboard2Activity.this, MethodUtils.getSecondDashboardItems());
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
        rv_items=findViewById(R.id.rv_items);
    }
}
