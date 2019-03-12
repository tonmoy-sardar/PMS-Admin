package com.pmsadmin.dashboard;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pmsadmin.GridSpanSizeLookUp.GridSpanSizeLookupForListDetailsAdapter;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.adapter.ItemsAdapter;
import com.pmsadmin.dashboard.adapter.ItemsAdapterTiles;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

public class DashBoardActivity extends BaseActivity {
    public View view;
    RecyclerView rv_items,rv_child_items;
    private GridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.dashboard_second_option, null);
        addContentView(view);
        bindView();
        fontSet();

        MethodUtils.setStickyBar(DashBoardActivity.this);
        setRecyclerView();
        setRecyclerViewChild();
    }

    private void setRecyclerViewChild() {
        ItemsAdapter adapter = new ItemsAdapter(DashBoardActivity.this, MethodUtils.addDataDashboard());
        rv_child_items.addItemDecoration(new DividerItemDecoration(DashBoardActivity.this, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(DashBoardActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv_child_items.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 2);
        rv_child_items.addItemDecoration(decoration);
        rv_child_items.setAdapter(adapter);
        /*rv_child_items.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new GridLayoutManager(this, 2);
        rv_child_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 2);
        rv_child_items.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(DashBoardActivity.this, 2);
        rv_child_items.addItemDecoration(itemOffset);*/
    }

    private void fontSet() {
        /*tv_tender.setTypeface(MethodUtils.getNormalFont(DashBoardActivity.this));
        tv_pre.setTypeface(MethodUtils.getNormalFont(DashBoardActivity.this));
        tv_execution.setTypeface(MethodUtils.getNormalFont(DashBoardActivity.this));
        tv_post.setTypeface(MethodUtils.getNormalFont(DashBoardActivity.this));*/
    }

    private void bindView() {
        rv_items=findViewById(R.id.rv_items);
        rv_child_items=findViewById(R.id.rv_child_items);
    }

    private void setRecyclerView() {
        ItemsAdapterTiles adapter = new ItemsAdapterTiles(DashBoardActivity.this, MethodUtils.getItems());
        rv_items.setAdapter(adapter);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new GridLayoutManager(this, 2);
        rv_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(DashBoardActivity.this, 2);
        rv_items.addItemDecoration(itemOffset);
        GridSpanSizeLookupForListDetailsAdapter headerSpanSizeLookup = new GridSpanSizeLookupForListDetailsAdapter(adapter, mLayoutManager);
        mLayoutManager.setSpanSizeLookup(headerSpanSizeLookup);
    }
}
