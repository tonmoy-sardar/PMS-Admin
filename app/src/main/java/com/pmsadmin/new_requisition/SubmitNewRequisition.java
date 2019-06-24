package com.pmsadmin.new_requisition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apply_local_conveyance.LocalConveyance;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.utils.progressloader.LoadingData;

public class SubmitNewRequisition extends BaseActivity {

    View view;
    TextView tv_universal_header;
    LoadingData loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_submit_new_requisition, null);
        addContentView(view);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());
        loader = new LoadingData(SubmitNewRequisition.this);

        tv_universal_header = findViewById(R.id.tv_universal_header);

        tv_universal_header.setText("New Requisition");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(SubmitNewRequisition.this));
    }
}
