package com.example.android.myhealth;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.android.myhealth.util.db.DataBaseRegulator;

public class PatientStartEditInfoActivity extends AppCompatActivity {
    private EditText iin;
    private EditText phoneNumber;
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info_edit_start);

        iin = findViewById(R.id.iin);
        phoneNumber = findViewById(R.id.phone_number);
        doneBtn = findViewById(R.id.done_btn);

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String iinString = iin.getText().toString();
                String phoneNumberString = phoneNumber.getText().toString();

                if (!iinString.isEmpty() && !phoneNumberString.isEmpty()){
                    Intent intent = new Intent(PatientStartEditInfoActivity.this, PatientEditInfoActivity.class);
                    intent.putExtra(DataBaseRegulator.IIN, iinString);
                    intent.putExtra(DataBaseRegulator.PHONE_NUMBER, phoneNumberString);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
