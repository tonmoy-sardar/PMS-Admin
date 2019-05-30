package com.pmsadmin.survey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.tenders_list.TendorsListing;

public class SitePhotoSurvey extends BaseActivity {

    public View view;
    private TextView tv_universal_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_site_photo_survey, null);
        addContentView(view);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Site Photos");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(SitePhotoSurvey.this));

        bindView();
        //setContentView(R.layout.activity_site_photo_survey);
    }

    private void bindView() {


    }
}
