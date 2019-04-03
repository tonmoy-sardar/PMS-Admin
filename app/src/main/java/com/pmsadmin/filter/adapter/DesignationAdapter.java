package com.pmsadmin.filter.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.seconddashboard.adapter.ProjectsItem;

import java.util.List;

public class DesignationAdapter extends RecyclerView.Adapter<DesignationAdapter.DesignationViewHolder> {
    Activity activity;
    List<ProjectsItem> filterDesignationItems;
    public DesignationAdapter(Activity activity, List<ProjectsItem> filterDesignationItems){
        this.activity=activity;
        this.filterDesignationItems=filterDesignationItems;
    }
    @NonNull
    @Override
    public DesignationViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_project_adapter, viewGroup, false);

        return new DesignationAdapter.DesignationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DesignationViewHolder designationViewHolder, int i) {
        designationViewHolder.tv_text.setText(filterDesignationItems.get(i).getProjectName());
    }

    @Override
    public int getItemCount() {
        return filterDesignationItems.size();
    }

    public class DesignationViewHolder extends RecyclerView.ViewHolder {
        TextView tv_text;

        public DesignationViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_text=itemView.findViewById(R.id.tv_text);
            tv_text.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
