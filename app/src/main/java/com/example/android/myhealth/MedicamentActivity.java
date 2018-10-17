package com.example.android.myhealth;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.android.myhealth.util.EatingTime;
import com.example.android.myhealth.util.db.DataBaseRegulator;
import com.example.android.myhealth.util.db.ReadAndWrite;
import com.example.android.myhealth.work.Medicament;

public class MedicamentActivity extends AppCompatActivity {
    private ReadAndWrite raw;
    private long treatmentID;
    private int medicamentID;
    private Medicament medicament;
    private EditText medicamentNameET;
    private EditText dosageET;
    private EditText perDiemET;
    private EditText amounOfDaysET;
    private RadioGroup eatingTimeRG;
    private EditText everyET;
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicament);
        raw = new ReadAndWrite(new DataBaseRegulator(this));

        treatmentID = getIntent().getLongExtra(DataBaseRegulator.TREATMENT_ID, -1);
        medicamentID = getIntent().getIntExtra(DataBaseRegulator.ID, -1);
        medicament = raw.readMedicanemtFromDB(treatmentID, medicamentID);

        medicamentNameET = findViewById(R.id.medicament_name);
        dosageET = findViewById(R.id.dosage_edit_text);
        perDiemET = findViewById(R.id.per_diem_edit_text);
        amounOfDaysET = findViewById(R.id.amount_of_days_edit_text);
        eatingTimeRG = findViewById(R.id.eating_time_radio_group);
        everyET = findViewById(R.id.every_edit_text);
        doneBtn = findViewById(R.id.done_btn);

        initializeField();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initializeMedicament()) {
                    setResult(RESULT_OK, null);
                    raw.writeDb(medicament);
                    finish();
                }
            }
        });
    }

    private void initializeField() {
        if (medicament.getMedicamentName() != null) {
            medicamentNameET.setText(medicament.getMedicamentName());
            medicamentNameET.setEnabled(false);
        }

        if (medicament.getDosage() > 0) {
            dosageET.setText(String.valueOf(medicament.getDosage()));
            dosageET.setEnabled(false);
        }
        if (medicament.getPerDiem() > 0) {
            perDiemET.setText(String.valueOf(medicament.getPerDiem()));
            perDiemET.setEnabled(false);
        }
        if (medicament.getAmountOfDays() > 0) {
            amounOfDaysET.setText(String.valueOf(medicament.getAmountOfDays()));
            amounOfDaysET.setEnabled(false);
        }
        if (medicament.getEatingTime() != null) {
            RadioButton radioButton = null;
            switch (medicament.getEatingTime()) {
                case BEFORE:
                    radioButton = findViewById(R.id.before_eating_radio_button);
                    break;
                case WHILE:
                    radioButton = findViewById(R.id.while_eating_radio_button);
                    break;
                case AFTER:
                    radioButton = findViewById(R.id.after_eating_radio_button);
                    break;
            }
            if (radioButton != null) radioButton.setChecked(true);
        }
        if (medicament.getEvery() > 0) {
            everyET.setText(String.valueOf(medicament.getEvery()));
            everyET.setEnabled(false);
        }
    }

    private boolean initializeMedicament() {
        String name;
        int dosage;
        byte perDiem;
        short amounOfDays;
        EatingTime eatingTime;
        short every;

        try {

            name = medicamentNameET.getText().toString();
            dosage = Integer.parseInt(dosageET.getText().toString());
            perDiem = Byte.parseByte(perDiemET.getText().toString());
            amounOfDays = Short.parseShort(amounOfDaysET.getText().toString());
            eatingTime = null;
            every = Short.parseShort(everyET.getText().toString());

            switch (eatingTimeRG.getCheckedRadioButtonId()) {
                case R.id.before_eating_radio_button:
                    eatingTime = EatingTime.BEFORE;
                    break;
                case R.id.while_eating_radio_button:
                    eatingTime = EatingTime.WHILE;
                    break;
                case R.id.after_eating_radio_button:
                    eatingTime = EatingTime.AFTER;
                    break;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        medicament.setMedicamentName(name);
        medicament.setDosage(dosage);
        medicament.setPerDiem(perDiem);
        medicament.setAmountOfDays(amounOfDays);
        medicament.setEatingTime(eatingTime);
        medicament.setEvery(every);

        return true;
    }
}
