package com.pmsadmin.apply_local_conveyance;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.tenders_list.TendorsListing;

public class LocalConveyance extends BaseActivity {

    private View view;
    private TextView tv_universal_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_local_conveyance, null);
        addContentView(view);
        //setContentView(R.layout.activity_local_conveyance);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("New Conveyance");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(LocalConveyance.this));
    }
}
