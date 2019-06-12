package com.pmsadmin.survey.resource.adpater;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.R;
import com.pmsadmin.survey.resource.ContactDetailsActivity;
import com.pmsadmin.survey.resource.DesignationWiseContactListActivity;
import com.pmsadmin.survey.resource.EstablishmentDetails;
import com.pmsadmin.survey.resource.hydrological_data.HydrologicalDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ContactDesignationListAdapter extends RecyclerView.Adapter<ContactDesignationListAdapter.ViewHolder> {

    Context context;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

    public ContactDesignationListAdapter(ContactDetailsActivity context, ArrayList<JSONObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_designation_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            holder.tv_designation.setText(arrayList.get(position).getString("name"));

            holder.rl_total_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(context, DesignationWiseContactListActivity.class);
                        intent.putExtra("designation_id",arrayList.get(position).getString("id"));
                        intent.putExtra("designation",arrayList.get(position).getString("name"));
                        context.startActivity(intent);
                        ((ContactDetailsActivity)context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    } catch (JSONException e) {
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

        TextView tv_designation;
        RelativeLayout rl_total_cell;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_designation = itemView.findViewById(R.id.tv_designation);
            rl_total_cell = itemView.findViewById(R.id.rl_total_cell);
        }
    }
}
