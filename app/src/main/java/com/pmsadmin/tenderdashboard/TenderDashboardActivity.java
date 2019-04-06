package com.pmsadmin.tenderdashboard;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.tenderdashboard.adapter.TenderItemAdapter;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

public class TenderDashboardActivity extends BaseActivity {
    public View view;
    RecyclerView rv_items;
    private GridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_tender_dashboard, null);
        addContentView(view);
        bindView();
        setRecyclerView();
    }

    private void bindView() {
        rv_items=view.findViewById(R.id.rv_items);
    }

    private void setRecyclerView() {
        TenderItemAdapter adapter = new TenderItemAdapter(TenderDashboardActivity.this, MethodUtils.getTenderItems());
        rv_items.setAdapter(adapter);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new GridLayoutManager(this, 2);
        rv_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(TenderDashboardActivity.this, 2);
        rv_items.addItemDecoration(itemOffset);
    }
}
