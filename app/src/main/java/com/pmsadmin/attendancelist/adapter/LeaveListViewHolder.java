package com.pmsadmin.attendancelist.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeaveListViewHolder extends RecyclerView.ViewHolder {
    TextView tv_name,tv_form,tv_reason,tv_leave;
    Button btn_approval,btn_reject,btn_modification;
    RelativeLayout rl_search;
    EditText et_search;
    public LeaveListViewHolder(View itemView, Activity activity) {
        super(itemView);
        tv_name=itemView.findViewById(R.id.tv_name);
        tv_leave=itemView.findViewById(R.id.tv_leave);
        tv_form=itemView.findViewById(R.id.tv_form);
        tv_reason=itemView.findViewById(R.id.tv_reason);
        btn_approval=itemView.findViewById(R.id.btn_approval);
        btn_reject=itemView.findViewById(R.id.btn_reject);
        btn_modification=itemView.findViewById(R.id.btn_modification);
        rl_search=itemView.findViewById(R.id.rl_search);
        et_search=itemView.findViewById(R.id.et_search);
        tv_name.setTypeface(MethodUtils.getNormalFont(activity));
        tv_form.setTypeface(MethodUtils.getNormalFont(activity));
        tv_reason.setTypeface(MethodUtils.getNormalFont(activity));
        btn_approval.setTypeface(MethodUtils.getNormalFont(activity));
        btn_reject.setTypeface(MethodUtils.getNormalFont(activity));
        btn_modification.setTypeface(MethodUtils.getNormalFont(activity));
        et_search.setTypeface(MethodUtils.getNormalFont(activity));
        tv_leave.setTypeface(MethodUtils.getBoldFont(activity));
    }

    public LeaveListViewHolder(View view, Activity activity, boolean status) {
        super(view);

    }
}
