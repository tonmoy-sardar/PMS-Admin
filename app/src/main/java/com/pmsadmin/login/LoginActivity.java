package com.pmsadmin.login;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pmsadmin.MethodUtils;
import com.pmsadmin.R;
import com.pmsadmin.dialog.ErrorMessageDialog;

public class LoginActivity extends AppCompatActivity {

    TextView tv_copyright, tv_tender, tv_forgot;
    EditText et_login, et_password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MethodUtils.setStickyBar(LoginActivity.this);
        viewBind();
        setFont();
        //setValidation();
        tv_copyright.setLinkTextColor(getResources().getColor(R.color.link_color));
    }

   /* private void setValidation() {
        if (et_login.getText().toString().equals("")) {
            MethodUtils.errorMsg(activity, activity.getString(R.string.enter_email));
        } else if (!MethodUtils.isValidEmail(et_login.getText().toString())) {
            MethodUtils.errorMsg(activity, activity.getString(R.string.enter_valid_email));
        } else if (et_password.getText().toString().equals("")) {
            MethodUtils.errorMsg(activity, activity.getString(R.string.enter_password));
        } else if (!ConnectionDetector.isConnectingToInternet(activity)) {
            MethodUtils.errorMsg(activity, activity.getString(R.string.no_internet));
        }
    }*/

    private void setFont() {
        tv_copyright.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        tv_tender.setTypeface(MethodUtils.getBoldFont(LoginActivity.this));
        tv_forgot.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        et_login.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        et_password.setTypeface(MethodUtils.getNormalFont(LoginActivity.this));
        btn_login.setTypeface(MethodUtils.getBoldFont(LoginActivity.this));
    }

    private void viewBind() {
        tv_copyright = findViewById(R.id.tv_copyright);
        tv_tender = findViewById(R.id.tv_tender);
        tv_forgot = findViewById(R.id.tv_forgot);
        et_login = findViewById(R.id.et_login);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void errorMsg(Context context, String msg) {
        ErrorMessageDialog.getInstant(context).show(msg);
    }
}
