package com.pmsadmin.dashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.GridSpanSizeLookUp.GridSpanSizeLookupForListDetailsAdapter;
import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.adapter.ItemsAdapter;
import com.pmsadmin.dashboard.adapter.ItemsAdapterTiles;
import com.pmsadmin.forgot.ForgotPasswordActivity;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

public class DashBoardActivity extends BaseActivity {
    public View view;
    RecyclerView rv_items;
    /*RelativeLayout rl_tender,rl_pre_execution,rl_execution,rl_post_execution;
    TextView tv_tender,tv_pre,tv_execution,tv_post;*/
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

    }

    private void fontSet() {
        /*tv_tender.setTypeface(MethodUtils.getNormalFont(DashBoardActivity.this));
        tv_pre.setTypeface(MethodUtils.getNormalFont(DashBoardActivity.this));
        tv_execution.setTypeface(MethodUtils.getNormalFont(DashBoardActivity.this));
        tv_post.setTypeface(MethodUtils.getNormalFont(DashBoardActivity.this));*/
    }

    private void bindView() {
        rv_items=findViewById(R.id.rv_items);
        /*rl_tender=findViewById(R.id.rl_tender);
        rl_pre_execution=findViewById(R.id.rl_pre_execution);
        rl_execution=findViewById(R.id.rl_execution);
        rl_post_execution=findViewById(R.id.rl_post_execution);
        tv_tender=findViewById(R.id.tv_tender);
        tv_pre=findViewById(R.id.tv_pre);
        tv_execution=findViewById(R.id.tv_execution);
        tv_post=findViewById(R.id.tv_post);*/
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
