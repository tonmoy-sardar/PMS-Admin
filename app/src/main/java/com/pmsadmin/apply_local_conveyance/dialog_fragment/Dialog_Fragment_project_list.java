package com.pmsadmin.apply_local_conveyance.dialog_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pmsadmin.R;
import com.pmsadmin.apply_local_conveyance.adapter.ProjectListAdapter;
import com.pmsadmin.survey.coordinates.coordinate_adapter.ExternalUserListAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by USER on 28-Oct-16.
 */

public class Dialog_Fragment_project_list extends DialogFragment implements ProjectListAdapter.OnItemClickListener {

    Dialog dialog;
    View v;
    Animation animation_zoom_in;
    Animation slide_out_buttom;
    OnItemClickDialog itemClickDialog;
    RecyclerView rv_project_list;
    ProjectListAdapter projectListAdapter;
    ArrayList<JSONObject> arrayList = new ArrayList<JSONObject>();


    public void setData(ArrayList<JSONObject> arrayList) {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.DialogCustomTheme_image);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogCustomTheme_image;
        System.out.println("Dialog ONcreate============>");

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.dialog_fragment_project_list, container, false);
        animation_zoom_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in);
        slide_out_buttom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_bottom);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        rv_project_list = v.findViewById(R.id.rv_project_list);


        projectListAdapter = new ProjectListAdapter(getActivity(), arrayList);
        projectListAdapter.setOnItemClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_project_list.setLayoutManager(layoutManager);
        rv_project_list.setHasFixedSize(true);
        rv_project_list.setAdapter(projectListAdapter);


        return v;
    }



    public void setOnDialogListener(OnItemClickDialog itemClickDialog) {
        this.itemClickDialog = itemClickDialog;
    }

    @Override
    public void OnItemClick(int position) {
        System.out.println("position============>>>"+position);
        if (itemClickDialog != null) {
            itemClickDialog.onItemClick(position);
        }
    }

    public interface OnItemClickDialog {
        void onItemClick(int position);
    }

}
