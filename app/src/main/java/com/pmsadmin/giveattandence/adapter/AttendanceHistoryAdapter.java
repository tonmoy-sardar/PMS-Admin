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
import com.pmsadmin.giveattandence.listattandencemodel.Result;

import java.util.List;

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.AttendanceViewHolder> {
    Activity activity;
    List<Result> result;
    public AttendanceHistoryAdapter(Activity activity, List<Result> result){
        this.activity=activity;
        this.result=result;
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
        if(result.get(i).getDate()==null||result.get(i).getDate().equalsIgnoreCase("null")||
                result.get(i).getDate().equals("")){
            attendanceViewHolder.tv_date_value.setText("");
        }else{
            attendanceViewHolder.tv_date_value.setText(result.get(i).getDate());
        }

        if (result.get(i).getLoginTime()==null||result.get(i).getLoginTime().equalsIgnoreCase("null")
                ||result.get(i).getLoginTime().equals("")){
            attendanceViewHolder.tv_login_value.setText("");
        }else{
            attendanceViewHolder.tv_login_value.setText(result.get(i).getLoginTime());
        }

        if (result.get(i).getLogoutTime()==null||result.get(i).getLogoutTime().equalsIgnoreCase("null")
                ||result.get(i).getLogoutTime().equals("")){
            attendanceViewHolder.tv_logout_value.setText("");
        }else{
            attendanceViewHolder.tv_logout_value.setText(result.get(i).getLogoutTime());
        }
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date,tv_login,tv_logout,tv_date_value,tv_login_value,tv_logout_value;
        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date=itemView.findViewById(R.id.tv_date);
            tv_login=itemView.findViewById(R.id.tv_login);
            tv_logout=itemView.findViewById(R.id.tv_logout);
            tv_date_value=itemView.findViewById(R.id.tv_date_value);
            tv_login_value=itemView.findViewById(R.id.tv_login_value);
            tv_logout_value=itemView.findViewById(R.id.tv_logout_value);
            tv_date.setTypeface(MethodUtils.getNormalFont(activity));
            tv_login.setTypeface(MethodUtils.getNormalFont(activity));
            tv_logout.setTypeface(MethodUtils.getNormalFont(activity));
            tv_date_value.setTypeface(MethodUtils.getNormalFont(activity));
            tv_login_value.setTypeface(MethodUtils.getNormalFont(activity));
            tv_logout_value.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
