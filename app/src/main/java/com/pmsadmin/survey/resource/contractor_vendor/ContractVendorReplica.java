package com.pmsadmin.survey.resource.contractor_vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;

public class ContractVendorReplica extends BaseActivity {

    public View view;
    private TextView tv_universal_header,tvAdd,tvPM,tvContractors;
    private RecyclerView rvItems;

    public static int contractor = 1;
    public static int pAndm = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = View.inflate(this, R.layout.activity_contract_vendor_replica, null);
        addContentView(view);

        tv_universal_header = findViewById(R.id.tv_universal_header);
        tv_universal_header.setText("Contractors/Vendor");
        tv_universal_header.setTypeface(MethodUtils.getNormalFont(ContractVendorReplica.this));
        //setContentView(R.layout.activity_contract_vendor_replica);

        initLayout();
    }

    private void initLayout() {

        tvAdd = findViewById(R.id.tvAdd);
        rvItems = findViewById(R.id.rvItems);
        tvPM = findViewById(R.id.tvPM);
        tvContractors = findViewById(R.id.tvContractors);

        clickListners();
    }

    private void clickListners() {

        tvContractors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*tvPM.setBackgroundColor(getResources().getColor(R.color.link_color));
                tvContractors.setBackgroundColor(getResources().getColor(R.color.inactive_button_color));*/
                finish();

            }
        });
    }
}
