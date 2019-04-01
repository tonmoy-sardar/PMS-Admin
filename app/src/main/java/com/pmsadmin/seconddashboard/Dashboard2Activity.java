package com.pmsadmin.seconddashboard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.filter.FilterActivity;
import com.pmsadmin.filter.adapter.ProjectAdapter;
import com.pmsadmin.seconddashboard.adapter.ItemsAdapterDashboardTiles;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

public class Dashboard2Activity extends BaseActivity {
    public View view;
    /*TextView tv_tender,tv_archived,tv_app;*/
    RecyclerView rv_items,rv_items_checkbox;
    private GridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_dashboard2, null);
        addContentView(view);
        setViewBind();
        setFont();
        setRecyclerView();
        setCheckBoxRecyclerView();
    }

    private void setCheckBoxRecyclerView() {
        ProjectAdapter adapter = new ProjectAdapter(Dashboard2Activity.this);
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

    private void setRecyclerView() {
        ItemsAdapterDashboardTiles adapter = new ItemsAdapterDashboardTiles(Dashboard2Activity.this,
                MethodUtils.getSecond2DashboardItems());
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
        rv_items_checkbox=findViewById(R.id.rv_items_checkbox);
    }
}
