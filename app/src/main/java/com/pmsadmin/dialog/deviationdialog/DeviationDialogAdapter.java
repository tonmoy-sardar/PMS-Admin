package com.pmsadmin.dialog.deviationdialog;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.attendancelist.reportlistmodel.DeviationDetail;
import com.pmsadmin.attendancelist.reportlistmodel.LogDetail;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.showgeofence.GeoFenceActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DeviationDialogAdapter extends RecyclerView.Adapter<DeviationDialogAdapter.DeviationDialogViewHolder> {
    Activity activity;
    List<DeviationDetail> getDeviationDetails;
    String name;
    public DeviationDialogAdapter(Activity activity, List<DeviationDetail> getDeviationDetails, String name){
        this.activity=activity;
        this.getDeviationDetails=getDeviationDetails;
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

        holder.tv_deviation.setText("Location Deviation: " + MethodUtils.deviationTime(getDeviationDetails.get(position).getFromTime())
                +" - "+ MethodUtils.deviationTime(getDeviationDetails.get(position).getToTime()));

        holder.tv_form.setText("Log In: "+ MethodUtils.deviationDate(getDeviationDetails.get(position).getFromTime())
                +"  |  "+"Log Out: "+MethodUtils.deviationDate(getDeviationDetails.get(position).getToTime()));

        holder.tv_reason.setText("Justification: "+getDeviationDetails.get(position).getJustification());
    }

    @Override
    public int getItemCount() {
        return getDeviationDetails.size();
    }

    public class DeviationDialogViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_form,tv_deviation,tv_reason;
        EditText et_search;
        public DeviationDialogViewHolder(@NonNull View itemView,Activity activity) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_form=itemView.findViewById(R.id.tv_form);
            tv_deviation=itemView.findViewById(R.id.tv_deviation);
            et_search=itemView.findViewById(R.id.et_search);
            tv_reason=itemView.findViewById(R.id.tv_reason);
            tv_name.setTypeface(MethodUtils.getNormalFont(activity));
            tv_form.setTypeface(MethodUtils.getNormalFont(activity));
            tv_deviation.setTypeface(MethodUtils.getNormalFont(activity));
            tv_reason.setTypeface(MethodUtils.getNormalFont(activity));
            et_search.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
