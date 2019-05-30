package com.pmsadmin.survey.coordinates;

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
import com.pmsadmin.survey.SitePhotoSurvey;
import com.pmsadmin.survey.StartSurveyHome;
import com.pmsadmin.survey.adapter.StartSurveyStaticAdapter;
import com.pmsadmin.survey.coordinates.coordinate_adapter.CoordinateStaticAdapter;
import com.pmsadmin.utils.ItemOffsetDecoration;
import com.pmsadmin.utils.SpacesItemDecoration;

public class CoordinatesActivity extends BaseActivity {

    public View view;
    private TextView tv_universal_header;
    private RecyclerView rvCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_coordinates, null);
        addContentView(view);
        //setContentView(R.layout.activity_coordinates);
        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Coordinates");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(CoordinatesActivity.this));

        System.out.println("tenderCoordinate: "+String.valueOf(MethodUtils.tender_id));

        initLayout();
    }

    private void initLayout() {

        rvCoordinates = (RecyclerView) findViewById(R.id.rvCoordinates);

        setAdapter();
    }

    private void setAdapter() {


        CoordinateStaticAdapter adapter = new CoordinateStaticAdapter(CoordinatesActivity.this, MethodUtils.getItemCoordinates());
        rvCoordinates.setAdapter(adapter);
        rvCoordinates.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvCoordinates.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rvCoordinates.addItemDecoration(decoration);
        ItemOffsetDecoration itemOffset = new ItemOffsetDecoration(CoordinatesActivity.this, 2);
        rvCoordinates.addItemDecoration(itemOffset);

    }
}
