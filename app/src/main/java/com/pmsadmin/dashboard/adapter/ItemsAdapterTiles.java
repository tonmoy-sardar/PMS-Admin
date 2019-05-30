package com.pmsadmin.dashboard.adapter;

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

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.dashboard.model.DashBoardModelImage;
import com.pmsadmin.giveattandence.GiveAttendanceActivity;
import com.pmsadmin.tenderdashboard.TenderDashboardActivity;
import com.pmsadmin.tenders_list.TendorsListing;

import java.util.List;

public class ItemsAdapterTiles extends RecyclerView.Adapter<ItemsAdapterTiles.ItemsAdapterTilesViewHolder> {
    Activity activity;
    public List<DashBoardModelImage> items;
    public ItemsAdapterTiles(Activity activity, List<DashBoardModelImage> items){
        this.activity=activity;
        this.items=items;
    }
    @NonNull
    @Override
    public ItemsAdapterTilesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.adapter_item_second_tiles, viewGroup, false);

        return new ItemsAdapterTilesViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapterTilesViewHolder itemsAdapterTiles, final int i) {
        itemsAdapterTiles.tv_item.setText(items.get(i).getItem());
        itemsAdapterTiles.iv_item.setImageResource(items.get(i).getImageId());
        itemsAdapterTiles.rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.get(i).getItem().equals("ATTENDANCE")){
                    /*Intent intent=new Intent(activity, TenderDashboardActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/

                    Intent intent = new Intent(activity, GiveAttendanceActivity.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }else if (items.get(i).getItem().equals("SURVEY")){

                    Intent intent = new Intent(activity, TendorsListing.class);
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    //MethodUtils.errorMsg(activity, "This section is under development");
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
            tv_item.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
