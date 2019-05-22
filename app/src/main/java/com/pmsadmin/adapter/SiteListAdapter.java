package com.pmsadmin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.pmsadmin.R;
import com.pmsadmin.login.model.SiteList;
import com.pmsadmin.planreport.PlanningReports;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SiteListAdapter extends RecyclerView.Adapter<SiteListAdapter.ViewHolder> {
    Context context;
    ArrayList<SiteList> list;

//    public SiteListAdapter(Context context, ArrayList<SiteList> list) {
//        this.context = context;
//        this.list = list;
//    }

//    public SiteListAdapter(PlanningReports context, ArrayList<SiteList> siteListList) {
//        this.context = context;
//        this.list = siteListList;
//    }

    public SiteListAdapter(PlanningReports context, ArrayList<SiteList> siteListList) {
        this.context = context;
       this.list = siteListList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_list, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)

    {

        holder.name.setText(list.get(position).getSitename());
        Log.d("Data","::::"+list.get(position).getSitename()+":::::"+list.get(position).getId());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // TextView submitted,carregnotxtview;
        //Button edit,viewcar;

        //ImageView img;
        TextView name;
        CheckBox checkBox;
        //CardView cardview_vehicletype;

        public ViewHolder(View itemView) {
            super(itemView);
          // img=(ImageView)itemView.findViewById(R.id.imageview);
            name=(TextView)itemView.findViewById(R.id.sitename);
            checkBox=(CheckBox) itemView.findViewById(R.id.simpleCheckBox);
           // cardview_vehicletype=(CardView)itemView.findViewById(R.id.cardview_vehicletype);



        }


    }
}
