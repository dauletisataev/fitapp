package com.example.android.myhealth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.android.myhealth.util.db.ReadAndWrite;
import com.example.android.myhealth.work.Patient;

import java.util.ArrayList;

public class DoctorMainActivity extends AppCompatActivity {
    private ArrayList<Patient> patients;
    private Button addPatient;
    private Button patientsList;
    private ReadAndWrite raw;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);


        patients = new ArrayList<>();
        addPatient = findViewById(R.id.add_patient);
        patientsList = findViewById(R.id.patients_list);

        addPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorMainActivity.this, PatientStartEditInfoActivity.class);
                startActivity(intent);
            }
        });

        patientsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorMainActivity.this, PatientListActivity.class);
                startActivity(intent);
            }
        });
    }
}
