package com.pmsadmin.giveattandence.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.AttendanceViewHolder> {
    Activity activity;
    public AttendanceHistoryAdapter(Activity activity){
        this.activity=activity;
    }
    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_attendance_list, viewGroup, false);

        return new AttendanceHistoryAdapter.AttendanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceViewHolder attendanceViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date,tv_login,tv_logout;
        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_login=itemView.findViewById(R.id.tv_login);
            tv_logout=itemView.findViewById(R.id.tv_logout);
            tv_date.setTypeface(MethodUtils.getNormalFont(activity));
            tv_login.setTypeface(MethodUtils.getNormalFont(activity));
            tv_logout.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
