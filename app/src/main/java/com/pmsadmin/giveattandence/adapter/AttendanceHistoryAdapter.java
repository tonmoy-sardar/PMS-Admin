package com.pmsadmin.giveattandence.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;

import java.util.List;

public class AttendanceHistoryAdapter extends RecyclerView.Adapter<AttendanceHistoryAdapter.AttendanceViewHolder> {
    Activity activity;
    List<com.pmsadmin.giveattandence.updatedattandenceListModel.Result> results;

    public AttendanceHistoryAdapter(Activity activity, List<com.pmsadmin.giveattandence.updatedattandenceListModel.Result> results) {
        this.activity = activity;
        this.results = results;
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

        if (results.get(i).getHoliday() == 1) {
            attendanceViewHolder.tv_date_value.setVisibility(View.GONE);
            attendanceViewHolder.tv_login.setVisibility(View.GONE);
            attendanceViewHolder.tv_logout.setVisibility(View.GONE);
            attendanceViewHolder.tv_holiday.setVisibility(View.VISIBLE);
            attendanceViewHolder.tv_holiday.setText("Status: Holiday");
            if (results.get(i).getDate() == null || results.get(i).getDate().equalsIgnoreCase("null") ||
                    results.get(i).getDate().equals("")) {
                attendanceViewHolder.tv_date.setText("");
            } else {
                attendanceViewHolder.tv_date.setText("Date: " + results.get(i).getDate());
            }

            if (results.get(i).getJustification() == null || results.get(i).getJustification().equalsIgnoreCase("null")
                    || results.get(i).getJustification().equals("")) {
                //attendanceViewHolder.view3.setVisibility(View.GONE);
                attendanceViewHolder.tv_logout.setVisibility(View.GONE);
                attendanceViewHolder.tv_logout.setText("");
            } else {
                //attendanceViewHolder.view3.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_logout.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_logout.setText("Justification: " + results.get(i).getJustification());
            }
        } else {
            if (results.get(i).getPresent() == 1) {
                attendanceViewHolder.tv_date_value.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_login.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_logout.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_holiday.setVisibility(View.GONE);
                if (results.get(i).getDate() == null || results.get(i).getDate().equalsIgnoreCase("null") ||
                        results.get(i).getDate().equals("")) {
                    attendanceViewHolder.tv_date.setText("");
                } else {
                    attendanceViewHolder.tv_date.setText("Date: " + results.get(i).getDate());
                }

                if (results.get(i).getLoginTime() == null || results.get(i).getLoginTime().equalsIgnoreCase("null")
                        || results.get(i).getLoginTime().equals("")) {
                    attendanceViewHolder.tv_date_value.setText("");
                } else {
                    attendanceViewHolder.tv_date_value.setText("Log In Time: " + MethodUtils.profileDate(results.get(i).getLoginTime()));
                }

                if (results.get(i).getLogoutTime() == null || results.get(i).getLogoutTime().equalsIgnoreCase("null")
                        || results.get(i).getLogoutTime().equals("")) {
                    attendanceViewHolder.tv_login.setText("");
                } else {
                    attendanceViewHolder.tv_login.setText("Log Out Time: " + MethodUtils.profileDate(results.get(i).getLogoutTime()));
                }

                if (results.get(i).getJustification() == null || results.get(i).getJustification().equalsIgnoreCase("null")
                        || results.get(i).getJustification().equals("")) {
                    //attendanceViewHolder.view3.setVisibility(View.GONE);
                    attendanceViewHolder.tv_logout.setVisibility(View.GONE);
                    attendanceViewHolder.tv_logout.setText("");
                } else {
                    //attendanceViewHolder.view3.setVisibility(View.VISIBLE);
                    attendanceViewHolder.tv_logout.setVisibility(View.VISIBLE);
                    attendanceViewHolder.tv_logout.setText("Justification: " + results.get(i).getJustification());
                }
            } else {
                attendanceViewHolder.tv_date_value.setVisibility(View.GONE);
                attendanceViewHolder.tv_login.setVisibility(View.GONE);
                attendanceViewHolder.tv_logout.setVisibility(View.GONE);
                attendanceViewHolder.tv_holiday.setVisibility(View.VISIBLE);
                attendanceViewHolder.tv_holiday.setText("Status: Absent");
                if (results.get(i).getDate() == null || results.get(i).getDate().equalsIgnoreCase("null") ||
                        results.get(i).getDate().equals("")) {
                    attendanceViewHolder.tv_date.setText("");
                } else {
                    attendanceViewHolder.tv_date.setText("Date: " + results.get(i).getDate());
                }

                if (results.get(i).getJustification() == null || results.get(i).getJustification().equalsIgnoreCase("null")
                        || results.get(i).getJustification().equals("")) {
                    //attendanceViewHolder.view3.setVisibility(View.GONE);
                    attendanceViewHolder.tv_logout.setVisibility(View.GONE);
                    attendanceViewHolder.tv_logout.setText("");
                } else {
                    //attendanceViewHolder.view3.setVisibility(View.VISIBLE);
                    attendanceViewHolder.tv_logout.setVisibility(View.VISIBLE);
                    attendanceViewHolder.tv_logout.setText("Justification: " + results.get(i).getJustification());
                }
            }
        }
        /*if(result.get(i).getDate()==null||result.get(i).getDate().equalsIgnoreCase("null")||
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

        if (result.get(i).getJustification()==null||result.get(i).getJustification().equalsIgnoreCase("null")
                ||result.get(i).getJustification().equals("")){
            attendanceViewHolder.view3.setVisibility(View.GONE);
            attendanceViewHolder.tv_justification.setVisibility(View.GONE);
            attendanceViewHolder.tv_justification.setText("");
        }else{
            attendanceViewHolder.view3.setVisibility(View.VISIBLE);
            attendanceViewHolder.tv_justification.setVisibility(View.VISIBLE);
            attendanceViewHolder.tv_justification.setText("Justification: "+result.get(i).getJustification());
        }*/
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date, tv_login, tv_logout, tv_date_value, tv_login_value, tv_logout_value, tv_justification, tv_holiday;
        View view3;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_login = itemView.findViewById(R.id.tv_login);
            tv_logout = itemView.findViewById(R.id.tv_logout);
            tv_date_value = itemView.findViewById(R.id.tv_date_value);
            tv_login_value = itemView.findViewById(R.id.tv_login_value);
            tv_logout_value = itemView.findViewById(R.id.tv_logout_value);
            tv_justification = itemView.findViewById(R.id.tv_justification);
            tv_holiday = itemView.findViewById(R.id.tv_holiday);
            view3 = itemView.findViewById(R.id.view3);
            tv_date.setTypeface(MethodUtils.getNormalFont(activity));
            tv_login.setTypeface(MethodUtils.getNormalFont(activity));
            tv_logout.setTypeface(MethodUtils.getNormalFont(activity));
            tv_date_value.setTypeface(MethodUtils.getNormalFont(activity));
            tv_login_value.setTypeface(MethodUtils.getNormalFont(activity));
            tv_logout_value.setTypeface(MethodUtils.getNormalFont(activity));
            tv_justification.setTypeface(MethodUtils.getNormalFont(activity));
            tv_holiday.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
