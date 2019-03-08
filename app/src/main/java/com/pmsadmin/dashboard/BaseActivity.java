package com.pmsadmin.dashboard;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout base_rl_contentview;
    private ActionBarDrawerToggle mDrawerToggle;
    public DrawerLayout mDrawerLayout;
    public ImageButton img_topbar_menu;
    ImageView iv_cross;
    TextView tv_user_name,tv_tender_list,tv_help,tv_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        viewBind();
        clickEvent();
        setFont();
        initializeDrawer();
    }

    private void setFont() {
        tv_user_name.setTypeface(MethodUtils.getBoldFont(BaseActivity.this));
        tv_tender_list.setTypeface(MethodUtils.getNormalFont(BaseActivity.this));
        tv_help.setTypeface(MethodUtils.getNormalFont(BaseActivity.this));
        tv_logout.setTypeface(MethodUtils.getNormalFont(BaseActivity.this));
    }

    private void viewBind() {
        base_rl_contentview = findViewById(R.id.base_rl_contentview);
        img_topbar_menu = findViewById(R.id.img_topbar_menu);
        iv_cross = findViewById(R.id.iv_cross);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_tender_list = findViewById(R.id.tv_tender_list);
        tv_help = findViewById(R.id.tv_help);
        tv_logout = findViewById(R.id.tv_logout);
    }

    private void clickEvent() {
        img_topbar_menu.setOnClickListener(this);
        iv_cross.setOnClickListener(this);
    }

    public void addContentView(View view) {
        base_rl_contentview.removeAllViews();
        base_rl_contentview.addView(view,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
    }

    /**
     * Initialize side menu
     */
    private void initializeDrawer() {
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.string.app_name, R.string.app_name
        ) {
            @Override
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        initializeDrawerToggle(mDrawerToggle);
    }

    public void initializeDrawerToggle(ActionBarDrawerToggle mDrawerToggle) {
        this.mDrawerToggle = mDrawerToggle;
    }

    /**
     * To check whether the side menu is open or not
     */
    private boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null)
            mDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_topbar_menu:
                if (isDrawerOpen())
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.iv_cross:
                if (isDrawerOpen()) {
                    mDrawerLayout.closeDrawers();
                }
                break;
        }
    }
}
