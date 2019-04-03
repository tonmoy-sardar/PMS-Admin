package com.pmsadmin.filter;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.attendancelist.AttendanceListActivity;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.filter.adapter.DesignationAdapter;
import com.pmsadmin.filter.adapter.ProjectAdapter;
import com.pmsadmin.leavesection.LeaveActivity;
import com.pmsadmin.utils.SpacesItemDecoration;

import java.util.Calendar;

public class FilterActivity extends BaseActivity implements View.OnClickListener {

    public View view;
    RecyclerView rv_project,rv_designation;
    private int month, year, day;
    RelativeLayout rl_form,rl_to;
    TextView tv_form,tv_to,tv_date_range,tv_project,tv_designation,tvSelectAll,tvSelectAlldesig;
    Button btn_apply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_filter, null);
        addContentView(view);
        MethodUtils.setStickyBar(FilterActivity.this);
        BindView();
        setClickEvent();
        setFontValue();
        setProjectRecyclerView();
        setDesignationRecyclerView();
    }

    private void setFontValue() {
        tv_form.setTypeface(MethodUtils.getNormalFont(FilterActivity.this));
        tv_to.setTypeface(MethodUtils.getNormalFont(FilterActivity.this));
        tv_date_range.setTypeface(MethodUtils.getBoldFont(FilterActivity.this));
        tv_project.setTypeface(MethodUtils.getBoldFont(FilterActivity.this));
        tv_designation.setTypeface(MethodUtils.getBoldFont(FilterActivity.this));
        tvSelectAll.setTypeface(MethodUtils.getNormalFont(FilterActivity.this));
        tvSelectAlldesig.setTypeface(MethodUtils.getNormalFont(FilterActivity.this));
    }

    private void setClickEvent() {
        rl_form.setOnClickListener(this);
        rl_to.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
    }

    private void setDesignationRecyclerView() {
        DesignationAdapter adapter = new DesignationAdapter(FilterActivity.this,MethodUtils.getFilterDesignationItems());
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(FilterActivity.this, RecyclerView.VERTICAL, false);
        rv_designation.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_designation.addItemDecoration(decoration);
        rv_designation.setAdapter(adapter);
    }

    private void setProjectRecyclerView() {
        ProjectAdapter adapter = new ProjectAdapter(FilterActivity.this, MethodUtils.getFilterProjectItems());
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(FilterActivity.this, RecyclerView.VERTICAL, false);
        rv_project.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_project.addItemDecoration(decoration);
        rv_project.setAdapter(adapter);
    }

    private void BindView() {
        tv_universal_header.setText("Filter-Attendance Report");
        iv_close.setVisibility(View.VISIBLE);
        rv_project=findViewById(R.id.rv_project);
        rv_designation=findViewById(R.id.rv_designation);
        rl_form=findViewById(R.id.rl_form);
        rl_to=findViewById(R.id.rl_to);
        tv_form=findViewById(R.id.tv_form);
        tv_to=findViewById(R.id.tv_to);
        tv_date_range=findViewById(R.id.tv_date_range);
        tv_project=findViewById(R.id.tv_project);
        tv_designation=findViewById(R.id.tv_designation);
        tvSelectAll=findViewById(R.id.tvSelectAll);
        tvSelectAlldesig=findViewById(R.id.tvSelectAlldesig);
        btn_apply=findViewById(R.id.btn_apply);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.rl_form:
                ExpiryDialog(tv_form);
                break;
            case R.id.rl_to:
                ExpiryDialog(tv_to);
                break;
            case R.id.btn_apply:
                Intent profileIntent = new Intent(FilterActivity.this, AttendanceListActivity.class);
                startActivity(profileIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                break;
        }
    }

    private void ExpiryDialog(final TextView tv) {
        Calendar mCalendar;
        mCalendar = Calendar.getInstance();
        System.out.println("Inside Dialog Box");
        final Dialog dialog = new Dialog(FilterActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_date_picker);
        dialog.show();
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.date_picker);
        Button date_time_set = (Button) dialog.findViewById(R.id.date_time_set);
        datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), null);

        LinearLayout ll = (LinearLayout) datePicker.getChildAt(0);
        LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

       /* if (currentapiVersion > 23) {
            ll2.getChildAt(1).setVisibility(View.GONE);
        } else if (currentapiVersion == 23) {
            ll2.getChildAt(0).setVisibility(View.GONE);
        } else {
            ll2.getChildAt(1).setVisibility(View.GONE);
        }*/

        date_time_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                month = datePicker.getMonth() + 1;
                year = datePicker.getYear();
                day = datePicker.getDayOfMonth();
                String monthInString = "" + month;
                String dayInString = "" + day;
                if (monthInString.length() == 1)
                    monthInString = "0" + monthInString;
                if (dayInString.length() == 1) {
                    dayInString = "0" + dayInString;
                }
                //tv.setText(dayInString + "-" + monthInString + "-" + year);
                tv.setText(year + "-" + monthInString + "-" + dayInString);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
