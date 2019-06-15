package com.pmsadmin.survey.coordinates.coordinate_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.R;
import com.pmsadmin.survey.coordinates.CrusherDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExternalUserListAdapter extends RecyclerView.Adapter<ExternalUserListAdapter.ViewHolder> {

    Context context;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();
    OnItemClickListener itemClickListener;


    public ExternalUserListAdapter(FragmentActivity activity, ArrayList<JSONObject> arrayList) {
        this.context = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_external_user_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            holder.tv_external_user_name.setText(arrayList.get(position).getString("contact_person_name"));

            holder.tv_external_user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.OnItemClick(position);
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

        TextView tv_external_user_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_external_user_name = itemView.findViewById(R.id.tv_external_user_name);
        }
    }


    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
}
