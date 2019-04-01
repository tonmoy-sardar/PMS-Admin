package com.pmsadmin.dialog.deviationdialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.attendancelist.reportlistmodel.LogDetail;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeviationDialogAdapter extends RecyclerView.Adapter<DeviationDialogAdapter.DeviationDialogViewHolder> {
    Activity activity;
    List<LogDetail> logDetails;
    String name;
    public DeviationDialogAdapter(Activity activity, List<LogDetail> logDetails, String name){
        this.activity=activity;
        this.logDetails=logDetails;
        this.name=name;
    }
    @NonNull
    @Override
    public DeviationDialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_deviation, parent, false);

        return new DeviationDialogAdapter.DeviationDialogViewHolder(itemView, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviationDialogViewHolder holder, int position) {
        holder.tv_name.setText(name);
    }

    @Override
    public int getItemCount() {
        return logDetails.size();
    }

    public class DeviationDialogViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_form,tv_deviation;
        EditText et_search;
        public DeviationDialogViewHolder(@NonNull View itemView,Activity activity) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_form=itemView.findViewById(R.id.tv_form);
            tv_deviation=itemView.findViewById(R.id.tv_deviation);
            et_search=itemView.findViewById(R.id.et_search);
            tv_name.setTypeface(MethodUtils.getNormalFont(activity));
            tv_form.setTypeface(MethodUtils.getNormalFont(activity));
            tv_deviation.setTypeface(MethodUtils.getNormalFont(activity));
            et_search.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
