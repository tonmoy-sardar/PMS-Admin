package com.pmsadmin.survey.coordinates.dialog_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.survey.coordinates.coordinate_adapter.ExternalUserListAdapter;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by USER on 28-Oct-16.
 */

public class Dialog_Fragment_external_user_list extends DialogFragment implements ExternalUserListAdapter.OnItemClickListener {

    Dialog dialog;
    View v;
    Animation animation_zoom_in;
    Animation slide_out_buttom;
    OnItemClickDialog itemClickDialog;
    RecyclerView rv_external_user_list;
    ExternalUserListAdapter externalUserListAdapter;
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
        v = inflater.inflate(R.layout.dialog_external_user_list, container, false);
        animation_zoom_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in);
        slide_out_buttom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_bottom);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        rv_external_user_list = v.findViewById(R.id.rv_external_user_list);


        externalUserListAdapter = new ExternalUserListAdapter(getActivity(), arrayList);
        externalUserListAdapter.setOnItemClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_external_user_list.setLayoutManager(layoutManager);
        rv_external_user_list.setHasFixedSize(true);
        rv_external_user_list.setAdapter(externalUserListAdapter);


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
