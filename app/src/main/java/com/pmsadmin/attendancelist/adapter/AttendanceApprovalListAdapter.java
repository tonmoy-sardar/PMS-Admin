package com.pmsadmin.attendancelist.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.attendancelist.approvallistmodel.Result;

import java.util.List;

public class AttendanceApprovalListAdapter extends RecyclerView.Adapter<ApprovalViewHolder> {
    Activity activity;
    List<Result> approvalList;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;

    public AttendanceApprovalListAdapter(Activity activity, List<Result> approvalList) {
        this.activity = activity;
        this.approvalList = approvalList;
    }

    @NonNull
    @Override
    public ApprovalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ApprovalViewHolder vh = null;
        if (i == VIEW_ITEM) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.adapter_approval_item, viewGroup, false);

            vh = new ApprovalViewHolder(activity, itemView);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.progress_item, viewGroup, false);

            vh = new ProgressViewHolder(v, activity, true);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ApprovalViewHolder approvalViewHolder, int i) {
        if (approvalViewHolder instanceof ProgressViewHolder) {
            return;
        }
        if (approvalList.get(i).getEmployeeDetails().size() > 0) {

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
        }

        approvalViewHolder.btn_modification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MethodUtils.errorMsg(activity,"Under Development");
            }
        });

        approvalViewHolder.btn_approval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MethodUtils.errorMsg(activity,"Under Development");
            }
        });

        approvalViewHolder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MethodUtils.errorMsg(activity,"Under Development");
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return approvalList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return approvalList.size();
    }

    public static class ProgressViewHolder extends ApprovalViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View view, Activity activity, boolean tempStatus) {
            super(view, activity, tempStatus);
            progressBar = view.findViewById(R.id.progressBar1);
        }
    }
}
