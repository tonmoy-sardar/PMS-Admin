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

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    Activity activity;
    List<ProjectsItem> projectItems;
    public ProjectAdapter(Activity activity, List<ProjectsItem> projectItems){
        this.activity=activity;
        this.projectItems=projectItems;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_project_adapter, viewGroup, false);

        return new ProjectAdapter.ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder projectViewHolder, int i) {

        projectViewHolder.tv_text.setText(projectItems.get(i).getProjectName());
    }

    @Override
    public int getItemCount() {
        return projectItems.size();
    }

    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView tv_text;
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_text=itemView.findViewById(R.id.tv_text);
            tv_text.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
