package com.example.android.myhealth;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PatientMainActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText iinET;
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main);

        iinET = findViewById(R.id.iin_edit_text);
        doneBtn = findViewById(R.id.done_btn);

        doneBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.done_btn:
                intent = new Intent(this, PatientActivity.class);
                break;
        }

        if (intent != null) {
            if (!iinET.getText().toString().equalsIgnoreCase("")) {
                intent.putExtra("iin", iinET.getText().toString());
                startActivity(intent);
            }
        }
    }
}
