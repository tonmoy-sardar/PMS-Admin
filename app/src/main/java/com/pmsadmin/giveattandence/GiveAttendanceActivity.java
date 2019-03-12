package com.pmsadmin.giveattandence;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.giveattandence.adapter.AttendanceHistoryAdapter;
import com.pmsadmin.utils.SpacesItemDecoration;

public class GiveAttendanceActivity extends BaseActivity {
    public View view;
    RecyclerView rv_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_give_attadence, null);
        addContentView(view);
        MethodUtils.setStickyBar(GiveAttendanceActivity.this);
        BindView();
        setRecyclerView();
    }

    private void BindView() {
        rv_items=findViewById(R.id.rv_items);
    }

    private void setRecyclerView() {
        AttendanceHistoryAdapter adapter = new AttendanceHistoryAdapter(GiveAttendanceActivity.this);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(GiveAttendanceActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_items.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        rv_items.setAdapter(adapter);
    }
}
