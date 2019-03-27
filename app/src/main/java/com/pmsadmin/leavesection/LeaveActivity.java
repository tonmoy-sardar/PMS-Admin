package com.pmsadmin.leavesection;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dialog.universalpopup.UniversalPopup;
import com.pmsadmin.leavesection.adapter.LeaveHistoryAdapter;
import com.pmsadmin.utils.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LeaveActivity extends BaseActivity implements View.OnClickListener {

    public View view;
    private int month, year, day;
    TextView tv_from,tv_to;
    EditText et_type;
    Button btn_report,btn_approval;
    RelativeLayout rl_form,rl_to,rl_type,rl_bottom;
    LinearLayout ll_header;
    RecyclerView rv_items;
    private List<String> leaveList = new ArrayList<>();
    private UniversalPopup leavePopup;
    LeaveHistoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = View.inflate(this, R.layout.activity_leave, null);
        addContentView(view);
        bindView();
        setClickEvent();
        addLeaveListAndCall();
        setLeaveHistoryRecyclerView();
    }

    private void setClickEvent() {
        rl_form.setOnClickListener(this);
        rl_to.setOnClickListener(this);
        rl_type.setOnClickListener(this);
        btn_report.setOnClickListener(this);
        btn_approval.setOnClickListener(this);
    }

    private void addLeaveListAndCall() {
        leaveList.add("Full Day");
        leaveList.add("Half Day");
        leaveList.add("Out Duty");
        leavePopup = new UniversalPopup(LeaveActivity.this, leaveList, et_type);
    }

    private void bindView() {
        tv_from=view.findViewById(R.id.tv_from);
        tv_to=view.findViewById(R.id.tv_to);
        rl_form=view.findViewById(R.id.rl_form);
        rl_to=view.findViewById(R.id.rl_to);
        rl_type=view.findViewById(R.id.rl_type);
        et_type=view.findViewById(R.id.et_type);
        rv_items=view.findViewById(R.id.rv_items);
        btn_report=view.findViewById(R.id.btn_report);
        btn_approval=view.findViewById(R.id.btn_approval);
        ll_header=view.findViewById(R.id.ll_header);
        rl_bottom=view.findViewById(R.id.rl_bottom);
        tv_universal_header.setText("Leave");
    }

    private void setLeaveHistoryRecyclerView() {
        adapter = new LeaveHistoryAdapter(LeaveActivity.this);
        rv_items.setItemAnimator(null);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(LeaveActivity.this);
        rv_items.setLayoutManager(mLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 10);
        rv_items.addItemDecoration(decoration);
        rv_items.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_form:
                ExpiryDialog(tv_from);
                break;
            case R.id.rl_to:
                ExpiryDialog(tv_to);
                break;
            case R.id.rl_type:
                hideSoftKeyBoard();
                if (leavePopup != null && leavePopup.isShowing()) {
                    leavePopup.dismiss();
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showAndDismissLeavePopup();
                        }
                    }, 100);
                }
                break;
            case R.id.btn_report:
                btn_report.setBackgroundColor(Color.parseColor("#2a4e68"));
                btn_approval.setBackgroundColor(Color.parseColor("#2daada"));
                rv_items.setVisibility(View.GONE);
                rl_bottom.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_approval:
                btn_report.setBackgroundColor(Color.parseColor("#2daada"));
                btn_approval.setBackgroundColor(Color.parseColor("#2a4e68"));
                rv_items.setVisibility(View.VISIBLE);
                rl_bottom.setVisibility(View.GONE);
                break;
        }
    }

    private void showAndDismissLeavePopup() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                leavePopup.showAsDropDown(rl_type);
            }
        }, 100);
    }

    private void hideSoftKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void ExpiryDialog(final TextView tv) {
        Calendar mCalendar;
        mCalendar = Calendar.getInstance();
        System.out.println("Inside Dialog Box");
        final Dialog dialog = new Dialog(LeaveActivity.this);
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
                tv.setText(dayInString + "-" + monthInString + "-" + year);
            }
        });
    }
}
