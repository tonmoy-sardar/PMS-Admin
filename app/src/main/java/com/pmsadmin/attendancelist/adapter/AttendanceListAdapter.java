package com.pmsadmin.attendancelist.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;

public class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.AttendanceViewHolder> {
    Activity activity;

    public AttendanceListAdapter(Activity activity){
        this.activity=activity;
    }
    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_item_attandence, viewGroup, false);

        return new AttendanceListAdapter.AttendanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder attendanceViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_contact,tv_desig,tv_project,tv_attendance;
        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_contact=itemView.findViewById(R.id.tv_contact);
            tv_desig=itemView.findViewById(R.id.tv_desig);
            tv_project=itemView.findViewById(R.id.tv_project);
            tv_attendance=itemView.findViewById(R.id.tv_attendance);

            tv_name.setTypeface(MethodUtils.getNormalFont(activity));
            tv_contact.setTypeface(MethodUtils.getNormalFont(activity));
            tv_desig.setTypeface(MethodUtils.getNormalFont(activity));
            tv_project.setTypeface(MethodUtils.getNormalFont(activity));
            tv_attendance.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
