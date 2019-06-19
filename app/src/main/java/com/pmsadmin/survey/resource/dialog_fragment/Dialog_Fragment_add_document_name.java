package com.pmsadmin.survey.resource.dialog_fragment;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.pmsadmin.R;
import com.pmsadmin.apilist.ApiList;
import com.pmsadmin.networkUtils.ApiInterface;
import com.pmsadmin.networkUtils.AppConfig;
import com.pmsadmin.sharedhandler.LoginShared;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONObject;

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

public class Dialog_Fragment_add_document_name extends DialogFragment {

    Dialog dialog;
    View v;
    Animation animation_zoom_in;
    Animation slide_out_buttom;
    EditText et_document_name;
    private LoadingData loader;
    OnItemClickDialog itemClickDialog;
    TextView tv_save_name;


    public void setData(String id,String tender_id,String contact_id) {

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
        v = inflater.inflate(R.layout.dialog_add_document_name, container, false);
        animation_zoom_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in);
        slide_out_buttom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_bottom);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        loader = new LoadingData(getActivity());

        et_document_name = v.findViewById(R.id.et_document_name);
        tv_save_name = v.findViewById(R.id.tv_save_name);

        tv_save_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_document_name.getText().toString().length()<1){
                    et_document_name.setError("Enter your Document name.");
                    et_document_name.requestFocus();
                } else {
                    if (itemClickDialog != null) {
                        itemClickDialog.onItemClick(et_document_name.getText().toString());
                    }
                }
            }
        });


        return v;
    }



    public void setOnDialogListener(OnItemClickDialog itemClickDialog) {
        this.itemClickDialog = itemClickDialog;
    }

    public interface OnItemClickDialog {
        void onItemClick(String document_name);
    }

}
