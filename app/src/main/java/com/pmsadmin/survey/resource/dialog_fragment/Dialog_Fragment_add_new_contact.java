package com.pmsadmin.survey.resource.dialog_fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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
import com.pmsadmin.survey.resource.AddContactActivity;
import com.pmsadmin.utils.progressloader.LoadingData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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

public class Dialog_Fragment_add_new_contact extends DialogFragment {

    Dialog dialog;
    View v;
    Animation animation_zoom_in;
    Animation slide_out_buttom;
    EditText et_designation,et_name,et_contact_number,et_email;
    TextView tv_add_contact;
    private LoadingData loader;
    String designation_id="",tender_id="";
    OnItemClickDialog itemClickDialog;


    public void setData(String id,String tender_id) {
        designation_id = id;
        this.tender_id = tender_id;
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
        v = inflater.inflate(R.layout.dialog_add_new_contact, container, false);
        animation_zoom_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in);
        slide_out_buttom = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_bottom);
        System.out.println("Current CLASS===>>>" + getClass().getSimpleName());

        loader = new LoadingData(getActivity());

        et_name = v.findViewById(R.id.et_name);
        et_contact_number = v.findViewById(R.id.et_contact_number);
        et_email = v.findViewById(R.id.et_email);
        tv_add_contact = v.findViewById(R.id.tv_add_contact);

        tv_add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidation()){
                    add_contact_details();
                }
            }
        });


        return v;
    }


    private void add_contact_details() {
        try {
            loader.show_with_label("Please wait");

            JsonObject object = new JsonObject();
            object.addProperty("tender", tender_id);
            object.addProperty("designation", designation_id);
            JsonArray field_details = new JsonArray();

            JsonObject field_details_name_obj = new JsonObject();
            field_details_name_obj.addProperty("field_label", "Name");
            field_details_name_obj.addProperty("field_value", et_name.getText().toString());
            field_details_name_obj.addProperty("field_type", "text");
            field_details.add(field_details_name_obj);

            JsonObject field_details_contact_no_obj = new JsonObject();
            field_details_contact_no_obj.addProperty("field_label", "Contact Number");
            field_details_contact_no_obj.addProperty("field_value", et_contact_number.getText().toString());
            field_details_contact_no_obj.addProperty("field_type", "number");
            field_details.add(field_details_contact_no_obj);

            JsonObject field_details_email_obj = new JsonObject();
            field_details_email_obj.addProperty("field_label", "Email");
            field_details_email_obj.addProperty("field_value", et_email.getText().toString());
            field_details_email_obj.addProperty("field_type", "text");
            field_details.add(field_details_email_obj);

            object.add("field_details", field_details);
            System.out.println("object======>>>>"+object.toString());


            Retrofit retrofit = AppConfig.getRetrofit(ApiList.BASE_URL);
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);

            final Call<ResponseBody> register = apiInterface.call_contact_details_add("Token " +
                    LoginShared.getLoginDataModel(getActivity()).getToken(), "application/json", object);


            register.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (loader != null && loader.isShowing())
                        loader.dismiss();

                    if (response.code() == 201 || response.code()==200) {
                        System.out.println("Service URL==>" + response.raw().request().url());
                        try {
                            String responseString = response.body().string();
                            System.out.println("response output==========>>>"+responseString);
                            JSONObject jsonObject = new JSONObject(responseString);
                            if (jsonObject.getBoolean("status")){
                                if (itemClickDialog != null) {
                                    itemClickDialog.onItemClick();
                                }
                                Toast.makeText(getActivity(),"Contact Added successfully.",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(),"Something went wrong, Please try again.",Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean checkValidation() {
        if (et_name.getText().toString().length() < 1) {
            et_name.setError("Enter your Name.");
            et_name.requestFocus();
            return false;
        } else if (et_contact_number.getText().toString().length() < 1) {
            et_contact_number.setError("Enter your Contact Number.");
            et_contact_number.requestFocus();
            return false;
        } else if (et_email.getText().toString().length() < 1) {
            et_email.setError("Enter your Email.");
            et_email.requestFocus();
            return false;
        }  else if (!emailValidator(et_email.getText().toString())) {
            et_email.setError("This email is not valid.");
            et_email.requestFocus();
            return false;
        }else {
            return true;
        }
    }


    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void setOnDialogListener(OnItemClickDialog itemClickDialog) {
        this.itemClickDialog = itemClickDialog;
    }

    public interface OnItemClickDialog {
        void onItemClick();
    }

}
