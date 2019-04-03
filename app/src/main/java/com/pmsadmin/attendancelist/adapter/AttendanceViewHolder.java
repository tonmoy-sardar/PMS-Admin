package com.pmsadmin.attendancelist.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;

import androidx.recyclerview.widget.RecyclerView;

public class AttendanceViewHolder extends RecyclerView.ViewHolder {

    TextView tv_name, tv_login, tv_date, tv_logout, tv_justification;
    RelativeLayout rl_main;

    public AttendanceViewHolder(Activity activity, View itemView) {
        super(itemView);
        tv_name = itemView.findViewById(R.id.tv_name);
        tv_login = itemView.findViewById(R.id.tv_login);
        tv_date = itemView.findViewById(R.id.tv_date);
        tv_logout = itemView.findViewById(R.id.tv_logout);
        tv_justification = itemView.findViewById(R.id.tv_justification);
        rl_main = itemView.findViewById(R.id.rl_main);
        tv_name.setTypeface(MethodUtils.getNormalFont(activity));
        tv_login.setTypeface(MethodUtils.getNormalFont(activity));
        tv_date.setTypeface(MethodUtils.getNormalFont(activity));
        tv_logout.setTypeface(MethodUtils.getNormalFont(activity));
        tv_justification.setTypeface(MethodUtils.getNormalFont(activity));
    }

    public AttendanceViewHolder(View view, Activity activity, boolean status) {
        super(view);

    }
}
