package com.pmsadmin.attendancelist.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pmsadmin.R;
import com.pmsadmin.attendancelist.leavelistmodel.Result;
import com.pmsadmin.utils.progressloader.LoadingData;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaveListAdapter extends RecyclerView.Adapter<LeaveListViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    private LoadingData loader;
    Activity activity;
    List<Result> leaveList;

    public LeaveListAdapter(Activity activity, List<Result> leaveList){
        this.activity=activity;
        this.leaveList=leaveList;
        loader = new LoadingData(activity);
    }
    @NonNull
    @Override
    public LeaveListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LeaveListViewHolder vh = null;
        if (i == VIEW_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_approval_item, parent, false);

            vh = new LeaveListViewHolder(itemView,activity);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new ProgressViewHolder(v, activity, true);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveListViewHolder holder, int i) {
        if (holder instanceof LeaveListAdapter.ProgressViewHolder) {
            return;
        }

        if(leaveList.get(i).getEmployee()!=null||!leaveList.get(i).getEmployee().equalsIgnoreCase("null")||
                !leaveList.get(i).getEmployee().equals("")){
            holder.tv_name.setText(leaveList.get(i).getEmployee());
        }else{
            holder.tv_name.setText("No name");
        }

        if (leaveList.get(i).getStartDate() != null || !leaveList.get(i).getStartDate().equals("") ||
                !leaveList.get(i).getStartDate().equalsIgnoreCase("null") ||
                leaveList.get(i).getEndDate() != null || !leaveList.get(i).getEndDate().equals("") ||
                !leaveList.get(i).getEndDate().equalsIgnoreCase("null")) {
            holder.tv_form.setText("Form: " + leaveList.get(i).getStartDate() + " To: " +
                    leaveList.get(i).getEndDate());
        } else {
            holder.tv_form.setText("");
        }

        if (!leaveList.isEmpty()||leaveList.get(i).getReason() != null || !leaveList.get(i).getReason().equals("") ||
                !leaveList.get(i).getReason().equalsIgnoreCase("null")) {
            holder.tv_reason.setText("Reason: " + leaveList.get(i).getReason());
        } else {
            holder.tv_reason.setText("");
        }
        /*if (leaveList.get(i).getEmployeeDetails().size() > 0) {

            if (approvalList.get(i).getEmployeeDetails().get(0).getCuUser().getFirstName() != null ||
                    !approvalList.get(i).getEmployeeDetails().get(0).getCuUser().getFirstName().equalsIgnoreCase("null") ||
                    !approvalList.get(i).getEmployeeDetails().get(0).getCuUser().getFirstName().equals("")) {
                approvalViewHolder.tv_name.setText(approvalList.get(i).getEmployeeDetails().get(0).getCuUser().getFirstName() + " " +
                        approvalList.get(i).getEmployeeDetails().get(0).getCuUser().getLastName());
            } else {
                approvalViewHolder.tv_name.setText("No Name");
            }

            if (approvalList.get(i).getLoginTime() != null || !approvalList.get(i).getLoginTime().equals("") ||
                    !approvalList.get(i).getLoginTime().equalsIgnoreCase("null") || approvalList.get(i).getLogoutTime() != null || !approvalList.get(i).getLogoutTime().equals("") ||
                    !approvalList.get(i).getLogoutTime().equalsIgnoreCase("null")) {
                approvalViewHolder.tv_form.setText("Form: " + approvalList.get(i).getLoginTime() + " To: " +
                        approvalList.get(i).getLogoutTime());
            } else {
                approvalViewHolder.tv_form.setText("");
            }

            if (!approvalList.isEmpty()||approvalList.get(i).getJustification() != null || !approvalList.get(i).getJustification().equals("") ||
                    !approvalList.get(i).getJustification().equalsIgnoreCase("null")) {
                approvalViewHolder.tv_reason.setText("Reason: " + approvalList.get(i).getJustification());
            } else {
                approvalViewHolder.tv_reason.setText("");
            }
        }*/

        /*approvalViewHolder.btn_approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionDetector.isConnectingToInternet(activity)) {
                    MethodUtils.errorMsg(activity, activity.getString(R.string.no_internet));
                }else {
                    attendanceEditApi(1);
                }
            }
        });

        approvalViewHolder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionDetector.isConnectingToInternet(activity)) {
                    MethodUtils.errorMsg(activity, activity.getString(R.string.no_internet));
                }else {
                    attendanceEditApi(2);
                }
            }
        });

        approvalViewHolder.btn_modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConnectionDetector.isConnectingToInternet(activity)) {
                    MethodUtils.errorMsg(activity, activity.getString(R.string.no_internet));
                }else {
                    attendanceEditApi(3);
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return leaveList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public static class ProgressViewHolder extends LeaveListViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View view, Activity activity, boolean tempStatus) {
            super(view, activity, tempStatus);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }
}
