package com.pmsadmin.giveattandence;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pmsadmin.R;

public class AttendanceJustification extends Activity {

    EditText etJustification;
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_justification_attendence);
        etJustification = (EditText) findViewById(R.id.etJustification);

        btSubmit = (Button) findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitJustification();
            }
        });

    }

    private void submitJustification() {


    }
}
