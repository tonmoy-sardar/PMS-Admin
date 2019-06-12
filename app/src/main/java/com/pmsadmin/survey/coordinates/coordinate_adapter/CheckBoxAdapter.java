package com.pmsadmin.survey.coordinates.coordinate_adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.seconddashboard.adapter.ProjectListAdapter;
import com.pmsadmin.survey.coordinates.AddMaterialActivity;
import com.pmsadmin.survey.unit_pojo.UnitPojo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<UnitPojo> unitPojoList;


    public CheckBoxAdapter(Activity activity, ArrayList<UnitPojo> unitPojoList) {
        this.activity = activity;
        this.unitPojoList = unitPojoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.unit_items, parent, false);

        return new CheckBoxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.tv_text.setText(unitPojoList.get(position).getCName());

        holder.chkbxSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.chkbxSelectAll.isChecked()){
                    unitPojoList.get(position).setSelected(true);
                }else {
                    unitPojoList.get(position).setSelected(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return unitPojoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_text;

        CheckBox chkbxSelectAll;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_text = itemView.findViewById(R.id.tv_text);
            tv_text.setTypeface(MethodUtils.getNormalFont(activity));

            chkbxSelectAll = (CheckBox) itemView.findViewById(R.id.chkbxSelectAll);

        }
    }
}
