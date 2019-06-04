package com.pmsadmin.add_daily_report;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.fragment.LabourFragment;
import com.pmsadmin.fragment.PandMfragment;
import com.pmsadmin.fragment.Progressfrag;
import com.pmsadmin.netconnection.ConnectionDetector;
import com.pmsadmin.utils.progressloader.LoadingData;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static com.pmsadmin.MethodUtils.errorMsg;

public class AddDailyData extends BaseActivity {
    public View view;
    private LoadingData loader;
    TabItem item1, item2, item3;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TabLayout tabLayout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(AddDailyData.this, R.layout.adddailydata, null);
        addContentView(view);
        bindView();
        //setClickEvent();
        loader = new LoadingData(AddDailyData.this);

        if (!ConnectionDetector.isConnectingToInternet(AddDailyData.this)) {
            errorMsg(AddDailyData.this, AddDailyData.this.getString(R.string.no_internet));
        } else {
            //callSiteListApi();
        }
        loadpage();

    }

    private void loadpage() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        Progressfrag progressfrag = new Progressfrag();
        fragmentTransaction.replace(R.id.frame, progressfrag, "progressfrag");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadpage2() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        LabourFragment labourFragment = new LabourFragment();
        fragmentTransaction.replace(R.id.frame, labourFragment, "labourFragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadpage3() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        PandMfragment pandMfragment = new PandMfragment();
        fragmentTransaction.replace(R.id.frame, pandMfragment, "pandMfragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void bindView() {


        item1 = (TabItem) findViewById(R.id.tabItem1);
        item2 = (TabItem) findViewById(R.id.tabItem2);
        item3 = (TabItem) findViewById(R.id.tabItem3);
        tabLayout1 = (TabLayout) findViewById(R.id.tabLayout1);
        tabLayout1.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("Block", "1");
                if (tab.getPosition() == 0) {
                    loadpage();
                } else if (tab.getPosition() == 1) {

                    loadpage2();
                } else if (tab.getPosition() == 2) {

                    loadpage3();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.d("Block", "2");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
