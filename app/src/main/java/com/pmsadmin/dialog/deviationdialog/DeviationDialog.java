package com.pmsadmin.dialog.deviationdialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.pmsadmin.R;
import com.pmsadmin.utils.SpacesItemDecoration;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeviationDialog extends Dialog implements View.OnClickListener {
    Activity activity;
    RecyclerView rv_items;
    ImageView ivDismissDialog;
    View view;
    public DeviationDialog(Activity activity) {
        super(activity);
        this.activity=activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = activity.getLayoutInflater().inflate(R.layout.dialog_deviations, null);
        setContentView(view);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        WindowManager.LayoutParams wmlp = getWindow().getAttributes();
        wmlp.width = wmlp.MATCH_PARENT;
        wmlp.height = wmlp.MATCH_PARENT;
        wmlp.gravity = Gravity.TOP;

        viewBind();
        clickEvent();
        fadeInAnimation();
        setRecyclerViewChild();

    }

    private void clickEvent() {
        ivDismissDialog.setOnClickListener(this);
    }

    private void viewBind() {
        rv_items=view.findViewById(R.id.rv_items);
        ivDismissDialog=view.findViewById(R.id.ivDismissDialog);
    }

    private void fadeInAnimation() {

        Animation animZoomIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
        view.startAnimation(animZoomIn);
    }

    private void dismissDialogWithFadeOutAnim() {
        Animation animSlide = AnimationUtils.loadAnimation(activity, R.anim.fade_out);
        view.startAnimation(animSlide);
        animSlide.cancel();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                dismiss();
            }
        }, 700);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivDismissDialog:
                dismissDialogWithFadeOutAnim();
                break;
        }
    }

    private void setRecyclerViewChild() {
        DeviationDialogAdapter adapter = new DeviationDialogAdapter(activity);
        rv_items.addItemDecoration(new DividerItemDecoration(activity, LinearLayoutManager.VERTICAL) {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                // Do not draw the divider
            }
        });
        LinearLayoutManager horizontalLayoutManager =
                new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        rv_items.setLayoutManager(horizontalLayoutManager);
        SpacesItemDecoration decoration = new SpacesItemDecoration((int) 2);
        rv_items.addItemDecoration(decoration);
        rv_items.setAdapter(adapter);
    }
}
