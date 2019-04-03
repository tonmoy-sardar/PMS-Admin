package com.pmsadmin.attendancelist.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.attendancelist.reportlistmodel.Result;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.showgeofence.GeoFenceActivity;

import java.io.Serializable;
import java.util.List;

public class AttendanceReportListAdapter extends RecyclerView.Adapter<AttendanceViewHolder> {
    Activity activity;
    List<Result> result;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;

    public AttendanceReportListAdapter(Activity activity, List<Result> result) {
        this.activity = activity;
        this.result = result;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        /*View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_item_attandence, viewGroup, false);

        return new AttendanceReportListAdapter.AttendanceViewHolder(activity, itemView);*/
        AttendanceViewHolder vh = null;
        if (i == VIEW_ITEM) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_item_attandence, viewGroup, false);

            vh = new AttendanceViewHolder(activity, itemView);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progress_item, viewGroup, false);

            vh = new ProgressViewHolder(v, activity, true);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder attendanceViewHolder, final int position) {

        if (attendanceViewHolder instanceof AttendanceReportListAdapter.ProgressViewHolder) {
            return;
        }
        if (result.get(position).getEmployeeDetails().size() > 0) {
            attendanceViewHolder.tv_name.setText(result.get(position).getEmployeeDetails().get(0).getCuUser().getFirstName() + " " +
                    result.get(position).getEmployeeDetails().get(0).getCuUser().getLastName());
        } else {
            attendanceViewHolder.tv_name.setText("No Name");
        }
        if(result.get(position)!=null) {
            if (!result.get(position).getJustification().isEmpty() || result.get(position).getJustification() != null ||
                    !result.get(position).getJustification().equals("") ||
                    !result.get(position).getJustification().equalsIgnoreCase("null")) {
                attendanceViewHolder.tv_justification.setText("Justification: " + result.get(position).getJustification());
            } else {
                attendanceViewHolder.tv_justification.setText("Justification: ");
            }
        }else{
            attendanceViewHolder.tv_justification.setText("Justification: ");
        }

        if(result.get(position)!=null) {
            if (result.get(position).getDate() != null || !result.get(position).getDate().equals("") ||
                    !result.get(position).getDate().equalsIgnoreCase("null")) {
                attendanceViewHolder.tv_date.setText(MethodUtils.profileDate(result.get(position).getDate()));
            } else {
                attendanceViewHolder.tv_date.setText("");
            }
        }else{
            attendanceViewHolder.tv_date.setText("");
        }

        if(result.get(position)!=null) {
            if (result.get(position).getLoginTime() == null || result.get(position).getLoginTime().equals("") ||
                    result.get(position).getLoginTime().equalsIgnoreCase("null")) {
                attendanceViewHolder.tv_login.setText("");
            } else {
                attendanceViewHolder.tv_login.setText("Log In: " + result.get(position).getLoginTime());
            }
        }else{
            attendanceViewHolder.tv_login.setText("");
        }
        if(result.get(position)!=null) {
            if (result.get(position).getLogoutTime() == null || result.get(position).getLogoutTime().equals("") ||
                    result.get(position).getLogoutTime().equalsIgnoreCase("null")) {
                attendanceViewHolder.tv_logout.setText("");
            } else {
                attendanceViewHolder.tv_logout.setText("Log Out: " + result.get(position).getLogoutTime());
            }
        }else{
            attendanceViewHolder.tv_logout.setText("");
        }

        if(result.get(position)!=null) {
            if (result.get(position).getJustification() == null || result.get(position).getJustification().equals("") ||
                    result.get(position).getJustification().equalsIgnoreCase("null")) {
                attendanceViewHolder.tv_justification.setVisibility(View.GONE);
            } else {
                attendanceViewHolder.tv_justification.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_justification.setText(result.get(position).getJustification());
            }
        }else{
            attendanceViewHolder.tv_justification.setVisibility(View.GONE);
        }
        attendanceViewHolder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Intent i = new Intent(activity, GeoFenceActivity.class);
                i.putExtra("position", position+"");
                LoginShared.saveResultList(activity,result,"result");
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    @Override
    public int getItemViewType(int position) {
        return result.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class ProgressViewHolder extends AttendanceViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View view, Activity activity, boolean tempStatus) {
            super(view, activity, tempStatus);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }
}
