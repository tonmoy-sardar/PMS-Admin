package com.pmsadmin.seconddashboard.adapter;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pmsadmin.R;
import com.pmsadmin.attendancelist.ApprovalListActivity;
import com.pmsadmin.attendancelist.AttendanceListActivity;
import com.pmsadmin.dashboard.model.DashBoardModelImage;
import com.pmsadmin.seconddashboard.Dashboard2Activity;
import com.pmsadmin.seconddashboard.project_list_model.Result;

import java.util.List;

public class ItemsAdapterDashboardTiles extends RecyclerView.Adapter<ItemsAdapterDashboardTiles.ItemsAdapterTilesViewHolder> {
    Activity activity;
    public List<DashBoardModelImage> items;
    List<Result> projectList;
    public ItemsAdapterDashboardTiles(Activity activity, List<DashBoardModelImage> items, List<Result> projectList){
        this.activity=activity;
        this.items=items;
        this.projectList=projectList;
    }
    @NonNull
    @Override
    public ItemsAdapterDashboardTiles.ItemsAdapterTilesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_item_second_tiles, viewGroup, false);

        return new ItemsAdapterTilesViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapterDashboardTiles.ItemsAdapterTilesViewHolder itemsAdapterTiles,final int i) {
        itemsAdapterTiles.tv_item.setText(items.get(i).getItem());
        itemsAdapterTiles.iv_item.setImageResource(items.get(i).getImageId());

        itemsAdapterTiles.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = "";
                if(i==0){


                    System.out.println("chi: "+projectList.size());
                    for (int i = 0; i < projectList.size(); i++){
                        Result result = projectList.get(i);

                        if (result.isSelected() == true){

                            data = data + "," + result.getId();
                        }
                    }

                    String niceString = "";

                    niceString = data.replaceFirst("^,", "");

                    Toast.makeText(activity,niceString,Toast.LENGTH_SHORT).show();



                    Intent profileIntent = new Intent(activity, AttendanceListActivity.class);
                    profileIntent.putExtra("niceString", niceString);
                    activity.startActivity(profileIntent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else if(i==1){
                    Intent profileIntent = new Intent(activity, ApprovalListActivity.class);
                    activity.startActivity(profileIntent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemsAdapterTilesViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_item;
        TextView tv_item;
        RelativeLayout rl_main;
        public ItemsAdapterTilesViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            iv_item=itemView.findViewById(R.id.iv_item);
            tv_item=itemView.findViewById(R.id.tv_item);
            rl_main=itemView.findViewById(R.id.rl_main);
        }
    }
}

