package com.pmsadmin.attendancelist.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.R;

public class AttendanceApprovalListAdapter extends RecyclerView.Adapter<AttendanceApprovalListAdapter.ApprovalViewHolder> {
    Activity activity;
    public AttendanceApprovalListAdapter(Activity activity){
        this.activity=activity;
    }

    @NonNull
    @Override
    public AttendanceApprovalListAdapter.ApprovalViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_approval_item, viewGroup, false);

        return new AttendanceApprovalListAdapter.ApprovalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceApprovalListAdapter.ApprovalViewHolder approvalViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ApprovalViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_form,tv_reason;
        Button btn_approval,btn_reject,btn_modification;
        RelativeLayout rl_search;
        EditText et_search;
        public ApprovalViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_form=itemView.findViewById(R.id.tv_form);
            tv_reason=itemView.findViewById(R.id.tv_reason);
            btn_approval=itemView.findViewById(R.id.btn_approval);
            btn_reject=itemView.findViewById(R.id.btn_reject);
            btn_modification=itemView.findViewById(R.id.btn_modification);
            rl_search=itemView.findViewById(R.id.rl_search);
            et_search=itemView.findViewById(R.id.et_search);
        }
    }
}
