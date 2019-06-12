package com.pmsadmin.survey.resource.adpater;

import android.content.Context;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DesignationWiseContactListAdapter extends RecyclerView.Adapter<DesignationWiseContactListAdapter.ViewHolder> {

    Context context;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();

    public DesignationWiseContactListAdapter(DesignationWiseContactListActivity context, ArrayList<JSONObject> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_designation_contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            holder.tv_contact_name.setText("Name : "+arrayList.get(position).getString("field_label"));
            holder.tv_contact_number.setText("Contact Number : "+arrayList.get(position).getString("field_value"));
            holder.tv_contact_email.setText("Email : "+arrayList.get(position).getString("field_type"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_contact_name,tv_contact_number,tv_contact_email;
        LinearLayout ll_adcl_total_cell;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_contact_name = itemView.findViewById(R.id.tv_contact_name);
            ll_adcl_total_cell = itemView.findViewById(R.id.ll_adcl_total_cell);
            tv_contact_number = itemView.findViewById(R.id.tv_contact_number);
            tv_contact_email = itemView.findViewById(R.id.tv_contact_email);
        }
    }
}
