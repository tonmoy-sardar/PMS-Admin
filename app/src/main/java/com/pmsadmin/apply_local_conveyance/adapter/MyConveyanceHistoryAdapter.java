package com.pmsadmin.apply_local_conveyance.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.apply_local_conveyance.MyConveyanceHistory;
import com.pmsadmin.apply_local_conveyance.my_conveyance_pojo.Result;
import com.pmsadmin.dashboard.BaseActivity;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.resource.adpater.MachineryAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyConveyanceHistoryAdapter extends RecyclerView.Adapter<MyConveyanceHistoryAdapter.MyViewHolder> {

    Activity activity;
    List<Result> resultList;

    public MyConveyanceHistoryAdapter(Activity activity, List<Result> resultList) {

        this.activity = activity;
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_my_convetance_history, parent, false);

        return new MyConveyanceHistoryAdapter.MyViewHolder(itemView,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (resultList.get(position).getFromPlace()!= null){

            holder.tvFrom.setText("From:  "+resultList.get(position).getFromPlace());
        }

        if (resultList.get(position).getToPlace()!= null){

            holder.tvTo.setText("To:  "+resultList.get(position).getToPlace());
        }

        if (resultList.get(position).getDate()!= null){

            String date = resultList.get(position).getDate();


            String[] separated = date.split("T");
            holder.tvDate.setText("Date:  "+separated[0]);


        }


        if (resultList.get(position).getVechicleType()!= null){

            holder.tvVehicleType.setText("Vehicle Type:  "+resultList.get(position).getVechicleType());
        }

        if (resultList.get(position).getPurpose()!= null){

            holder.tvPurpose.setText("Purpose:  "+resultList.get(position).getPurpose());
        }

        if (resultList.get(position).getAmmount()!= null){

            //String amount = resultList.get(position).getAmmount();

            holder.tvAmount.setText("Amount:  " + "INR " +resultList.get(position).getAmmount());

        }


        if (resultList.get(position).getApprovedStatus()!= null){

            if (resultList.get(position).getApprovedStatus() == 1){

                holder.btStatus.setBackgroundResource(R.drawable.survey_pending);
                holder.btStatus.setText("Pending");

            }else if (resultList.get(position).getApprovedStatus() == 2){

                holder.btStatus.setBackgroundResource(R.drawable.survey_btn);
                holder.btStatus.setText("Approved");

            }else if (resultList.get(position).getApprovedStatus() == 3){

                holder.btStatus.setBackgroundResource(R.drawable.rejected);
                holder.btStatus.setText("Rejected");
            }
        }


    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvDate,tvFrom,tvTo,tvVehicleType,tvPurpose,tvAmount;
        Button btStatus;

        public MyViewHolder(@NonNull View itemView, Activity activity) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDate.setTypeface(MethodUtils.getNormalFont(activity));
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvFrom.setTypeface(MethodUtils.getNormalFont(activity));

            tvTo = itemView.findViewById(R.id.tvTo);
            tvTo.setTypeface(MethodUtils.getNormalFont(activity));

            tvVehicleType = itemView.findViewById(R.id.tvVehicleType);
            tvVehicleType.setTypeface(MethodUtils.getNormalFont(activity));

            tvPurpose = itemView.findViewById(R.id.tvPurpose);
            tvPurpose.setTypeface(MethodUtils.getNormalFont(activity));

            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvAmount.setTypeface(MethodUtils.getNormalFont(activity));

            btStatus = itemView.findViewById(R.id.btStatus);
            btStatus.setTypeface(MethodUtils.getNormalFont(activity));
        }
    }
}
