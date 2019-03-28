package com.pmsadmin.dashboard.adapter;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.attendancelist.AttendanceListActivity;
import com.pmsadmin.dashboard.model.DashboardItemsModel;
import com.pmsadmin.seconddashboard.Dashboard2Activity;
import com.pmsadmin.sharedhandler.LoginShared;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {
    Activity activity;
    public List<DashboardItemsModel> list;

    public ItemsAdapter(Activity activity, List<DashboardItemsModel> list) {
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_items, viewGroup, false);

        return new ItemsViewHolder(itemView, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder itemsViewHolder, final int i) {
        itemsViewHolder.tv_item.setText(list.get(i).getItem());
        itemsViewHolder.iv_item.setImageResource(list.get(i).getImageId());

        itemsViewHolder.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(i).getItem().equals("Attendance")) {
                    Intent profileIntent = new Intent(activity, Dashboard2Activity.class);
                    activity.startActivity(profileIntent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_item;
        TextView tv_item;
        RelativeLayout rl_main;

        public ItemsViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            iv_item = itemView.findViewById(R.id.iv_item);
            tv_item = itemView.findViewById(R.id.tv_item);
            rl_main = itemView.findViewById(R.id.rl_main);
            tv_item.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
