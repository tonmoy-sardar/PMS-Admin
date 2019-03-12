package com.pmsadmin.attendancelist;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
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
import com.pmsadmin.attendancelist.adapter.AttendanceListAdapter;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dashboard.DashBoardActivity;
import com.pmsadmin.dashboard.adapter.ItemsAdapter;
import com.pmsadmin.dashboard.adapter.ItemsAdapterTiles;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

public class AttendanceListActivity extends BaseActivity {
    public View view;
    RecyclerView rv_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_attendance_list, null);
        addContentView(view);
        MethodUtils.setStickyBar(AttendanceListActivity.this);
        bindView();
        setRecyclerView();
    }

    private void setRecyclerView() {
        AttendanceListAdapter adapter = new AttendanceListAdapter(AttendanceListActivity.this);
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(AttendanceListActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_items.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        rv_items.setAdapter(adapter);
    }
    private void bindView() {
        rv_items=findViewById(R.id.rv_items);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
