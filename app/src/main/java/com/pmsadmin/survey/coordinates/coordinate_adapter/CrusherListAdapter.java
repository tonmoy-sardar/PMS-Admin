package com.pmsadmin.survey.coordinates.coordinate_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.R;
import com.pmsadmin.survey.coordinates.CrusherActivity;
import com.pmsadmin.survey.coordinates.CrusherDetailsActivity;
import com.pmsadmin.survey.resource.ContactDetailsActivity;
import com.pmsadmin.survey.resource.DesignationWiseContactListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CrusherListAdapter extends RecyclerView.Adapter<CrusherListAdapter.ViewHolder> {

    Context context;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

    public CrusherListAdapter(CrusherActivity context, ArrayList<JSONObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_crusher_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            holder.tv_meterial_name.setText(arrayList.get(position).getString("name"));

            holder.rl_total_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, CrusherDetailsActivity.class);
                        intent.putExtra("name",arrayList.get(position).getString("name"));
                        intent.putExtra("id",arrayList.get(position).getString("id"));
                        context.startActivity(intent);
                        ((CrusherActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_meterial_name;
        RelativeLayout rl_total_cell;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_meterial_name = itemView.findViewById(R.id.tv_meterial_name);
            rl_total_cell = itemView.findViewById(R.id.rl_total_cell);
        }
    }
}
