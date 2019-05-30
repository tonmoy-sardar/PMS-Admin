package com.pmsadmin.seconddashboard.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.filter.adapter.ProjectAdapter;
import com.pmsadmin.seconddashboard.Dashboard2Activity;
import com.pmsadmin.seconddashboard.project_list_model.Result;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.MyViewHolder> {

    Activity activity;
    List<Result> projectList;

    public ProjectListAdapter(Activity activity, List<Result> projectList) {

        this.activity = activity;
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project_adapter, parent, false);

        return new ProjectListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.tv_text.setText(projectList.get(position).getSiteLocationName());



        holder.chkbxSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (holder.chkbxSelectAll.isChecked()){
                    projectList.get(position).setSelected(true);
                }else {
                    projectList.get(position).setSelected(false);
                }

                /*if (holder.chkbxSelectAll.isSelected()) {
                    projectList.get(position).setSelected(false);
                }
                else
                {
                    projectList.get(position).setSelected(false);
                    projectList.get(position).setSelected(true);
                }*/
//                CheckBox cb = (CheckBox) v;
//                Result contact = (Result) cb.getTag();
//
//                contact.setSelected(cb.isChecked());
//                projectList.get(position).setSelected(cb.isChecked());
//
//
//                Toast.makeText(
//                        v.getContext(),
//                        "Clicked on Checkbox: " + cb.getText() + " is "
//                                + cb.isChecked(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
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
