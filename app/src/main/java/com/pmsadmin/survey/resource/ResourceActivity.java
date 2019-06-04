package com.pmsadmin.survey.resource;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.survey.StartSurveyHome;
import com.pmsadmin.survey.adapter.StartSurveyStaticAdapter;
import com.pmsadmin.tenders_list.TendorsListing;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

public class ResourceActivity extends BaseActivity {

    public View view;
    private TextView tv_universal_header;
    private RecyclerView rv_items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_resource, null);
        addContentView(view);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Resource");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(ResourceActivity.this));
        //setContentView(R.layout.activity_resource);

        initLayout();
    }

    private void initLayout() {

        rv_items = (RecyclerView) findViewById(R.id.rvItems);

        setAdapter();
    }

    private void setAdapter() {

        StartSurveyStaticAdapter adapter = new StartSurveyStaticAdapter(ResourceActivity.this, MethodUtils.getResourceItems());
        rv_items.setAdapter(adapter);
        rv_items.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rv_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(ResourceActivity.this, 2);
        rv_items.addItemDecoration(itemOffset);
    }
}
