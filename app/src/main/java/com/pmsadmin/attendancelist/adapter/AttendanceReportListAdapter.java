package com.pmsadmin.attendancelist.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.attendancelist.reportlistmodel.Result;
import com.pmsadmin.showgeofence.GeoFenceActivity;

import java.util.List;

public class AttendanceReportListAdapter extends RecyclerView.Adapter<AttendanceReportListAdapter.AttendanceViewHolder> {
    Activity activity;
    List<Result> result;

    public AttendanceReportListAdapter(Activity activity, List<Result> result) {
        this.activity = activity;
        this.result = result;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_item_attandence, viewGroup, false);

        return new AttendanceReportListAdapter.AttendanceViewHolder(activity, itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder attendanceViewHolder, final int position) {

        /*if (result.get(i).getEmployeeDetails().size() > 0) {
            attendanceViewHolder.tv_name.setText(result.get(i).getEmployeeDetails().get(0).getCuUser().getFirstName() + " " +
                    result.get(i).getEmployeeDetails().get(0).getCuUser().getLastName());
        } else {
            attendanceViewHolder.tv_name.setText("No Name");
        }*/

        attendanceViewHolder.tv_date.setText(result.get(position).getDate());
        if (result.get(position).getLoginTime() == null || result.get(position).getLoginTime().equals("") ||
                result.get(position).getLoginTime().equalsIgnoreCase("null")) {
            attendanceViewHolder.tv_login.setText("");
        } else {
            attendanceViewHolder.tv_login.setText("Log In: " + result.get(position).getLoginTime());
        }
        if (result.get(position).getLogoutTime() == null || result.get(position).getLogoutTime().equals("") ||
                result.get(position).getLogoutTime().equalsIgnoreCase("null")) {
            attendanceViewHolder.tv_logout.setText("");
        } else {
            attendanceViewHolder.tv_logout.setText("Log Out: " + result.get(position).getLogoutTime());
        }
        if (result.get(position).getJustification() == null || result.get(position).getJustification().equals("") ||
                result.get(position).getJustification().equalsIgnoreCase("null")) {
            attendanceViewHolder.tv_justification.setVisibility(View.INVISIBLE);
        } else {
            attendanceViewHolder.tv_justification.setVisibility(View.VISIBLE);
            attendanceViewHolder.tv_justification.setText(result.get(position).getJustification());
        }

        attendanceViewHolder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, GeoFenceActivity.class);
                i.putExtra("position", position+"");
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

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
    }
}
