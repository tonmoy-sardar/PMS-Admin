package com.pmsadmin.survey.resource.adpater;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DesignationWiseContactLisEdittAdapter extends RecyclerView.Adapter<DesignationWiseContactLisEdittAdapter.ViewHolder> {

    Context context;
    ArrayList<JSONObject> arrayList_info = new ArrayList<JSONObject>();
    OnItemClickListener itemClickListener;


    public DesignationWiseContactLisEdittAdapter(Context context, ArrayList<JSONObject> arrayList_info) {
        this.context = context;
        this.arrayList_info = arrayList_info;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_designation_contact_list_edit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            holder.tv_field_info.setText(arrayList_info.get(position).getString("field_label"));
            holder.et_field.setText(arrayList_info.get(position).getString("field_value"));

            holder.et_field.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    try {
                        arrayList_info.get(position).put("field_value",holder.et_field.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return arrayList_info.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_field_info;
        EditText et_field;
        LinearLayout ll_adcl_total_cell;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_field_info = itemView.findViewById(R.id.tv_field_info);
            ll_adcl_total_cell = itemView.findViewById(R.id.ll_adcl_total_cell);
            et_field = itemView.findViewById(R.id.et_field);
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void OnItemClick(int position);
    }
}
