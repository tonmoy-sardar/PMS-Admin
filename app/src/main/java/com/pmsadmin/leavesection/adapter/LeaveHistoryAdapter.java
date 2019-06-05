package com.pmsadmin.leavesection.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.leavesection.leavehistorymodel.Result;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaveHistoryAdapter extends RecyclerView.Adapter<LeaveHistoryAdapter.LeaveHistoryViewHolder> {
    Activity activity;
    List<Result> list;

    public LeaveHistoryAdapter(Activity activity, List<Result> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public LeaveHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leave_history, parent, false);

        return new LeaveHistoryAdapter.LeaveHistoryViewHolder(activity, itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveHistoryViewHolder holder, int i) {
        if (list.get(i).getStartDate() != null || !list.get(i).getStartDate().equals("") ||
                !list.get(i).getStartDate().equalsIgnoreCase("null") ||
                list.get(i).getEndDate() != null || !list.get(i).getEndDate().equals("") ||
                !list.get(i).getEndDate().equalsIgnoreCase("null")) {
            holder.tv_form.setText("Form: " + MethodUtils.profileDate(list.get(i).getStartDate()) + "  To: " +
                    MethodUtils.profileDate(list.get(i).getEndDate()));
        } else {
            holder.tv_form.setText("");
        }

        if (!list.isEmpty() || list.get(i).getReason() != null || !list.get(i).getReason().equals("") ||
                !list.get(i).getReason().equalsIgnoreCase("null")) {
            holder.tv_reason.setText("Reason: " + list.get(i).getReason());
        } else {
            holder.tv_reason.setText("");
        }

        if(list.get(i).getApprovedStatus()==2){
            holder.btn_status.setBackgroundResource(R.drawable.survey_btn);
            holder.btn_status.setText("Approved");
        }else if (list.get(i).getApprovedStatus()==3){
            holder.btn_status.setBackgroundResource(R.drawable.survey_pending);
            holder.btn_status.setText("Rejected");
        }
        else{
            holder.btn_status.setBackgroundResource(R.drawable.survey_pending);
            holder.btn_status.setText("Pending");
        }

        if(list.get(i).getLeaveType() != null || !list.get(i).getLeaveType().equals("") ||
                !list.get(i).getLeaveType().equalsIgnoreCase("null")){
            holder.tv_leave.setText(list.get(i).getLeaveType());
        }else{
            holder.tv_leave.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LeaveHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_leave, tv_form, tv_reason;
        Button btn_status;

        public LeaveHistoryViewHolder(Activity activity, View itemView) {
            super(itemView);
            tv_leave = itemView.findViewById(R.id.tv_leave);
            tv_form = itemView.findViewById(R.id.tv_form);
            tv_reason = itemView.findViewById(R.id.tv_reason);
            btn_status = itemView.findViewById(R.id.btn_status);
            tv_form.setTypeface(MethodUtils.getNormalFont(activity));
            tv_leave.setTypeface(MethodUtils.getNormalFont(activity));
            tv_reason.setTypeface(MethodUtils.getNormalFont(activity));
            btn_status.setTypeface(MethodUtils.getBoldFont(activity));
        }
    }
}
