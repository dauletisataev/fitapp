package com.example.android.myhealth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{
    private Button patientBtn;
    private Button doctorBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        patientBtn = findViewById(R.id.patient_btn);
        doctorBtn = findViewById(R.id.doctor_btn);

        patientBtn.setOnClickListener(this);
        doctorBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.patient_btn:
                intent = new Intent(this, PatientMainActivity.class);
                break;
            case R.id.doctor_btn:
                intent = new Intent(this, DoctorMainActivity.class);
                break;
        }
        if (intent != null) startActivity(intent);
    }
}
