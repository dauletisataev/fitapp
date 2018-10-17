package com.example.android.myhealth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.myhealth.util.db.DataBaseRegulator;
import com.example.android.myhealth.util.db.ReadAndWrite;
import com.example.android.myhealth.work.Patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class PatientEditInfoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, DialogInterface.OnClickListener{
    private ReadAndWrite raw;
    private Patient patient;
    private ArrayAdapter<String> treatmentArrayAdapter;
    private Map<String, Long> treatments;
    private EditText lastNameET;
    private EditText firstNameET;
    private EditText patronymicET;
    private RadioGroup genderRG;
    private ListView treatmentLV;
    private Button addBtn;
    private Button updateBtn;
    private Button deleteBtn;
    private Button doneBtn;
    private TextView dateOfBirthTV;
    private TextView selectedTV;

    private int myYear = 1992;
    private int myMonth = 8;
    private int myDay = 28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_info);
        raw = new ReadAndWrite(new DataBaseRegulator(this));

        patient = raw.readPatientFromDB(getIntent().getStringExtra(DataBaseRegulator.IIN), getIntent().getStringExtra(DataBaseRegulator.PHONE_NUMBER));
        treatmentArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        lastNameET = findViewById(R.id.last_name_edit_text);
        firstNameET = findViewById(R.id.first_name_edit_text);
        patronymicET = findViewById(R.id.patronymic_edit_text);
        genderRG = findViewById(R.id.gender_radio_group);

        treatmentLV = findViewById(R.id.treatments_list_view);
        treatmentLV.setAdapter(treatmentArrayAdapter);
        treatmentLV.setOnItemClickListener(this);

        dateOfBirthTV = findViewById(R.id.date_of_birthday);
        dateOfBirthTV.setOnClickListener(this);
        addBtn = findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
        updateBtn = findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(this);
        deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(this);
        doneBtn = findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(this);

        initializeField();
    }



    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.add_btn:
                raw.writeDb(patient);
                intent = new Intent(this, TreatmentActivity.class);
                intent.putExtra(DataBaseRegulator.IIN, patient.getIIN());
                intent.putExtra(DataBaseRegulator.ID, -1L);
                startActivityForResult(intent, 1);
                break;
            case R.id.update_btn:
                intent = new Intent(this, TreatmentActivity.class);
                intent.putExtra(DataBaseRegulator.IIN, patient.getIIN());
                intent.putExtra(DataBaseRegulator.ID, treatments.get(selectedTV.getText().toString()));
                startActivityForResult(intent, 1);
                break;
            case R.id.delete_btn:
//                raw.deleteTreatment(treatments.get(selectedTV.getText().toString()));
//                initializeAdapter();
                showDialog(2);
                break;
            case R.id.date_of_birthday:
                showDialog(1);
                break;
            case R.id.done_btn:
                patient.setFirstName(firstNameET.getText().toString());
                patient.setLastName(lastNameET.getText().toString());
                patient.setPatronymic(patronymicET.getText().toString());
                try {
                    patient.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirthTV.getText().toString()).getTime());
                } catch (ParseException e) {
                    patient.setDateOfBirth(-1);
                }
                patient.setGender(genderRG.getCheckedRadioButtonId() == R.id.male_radio_btn);
                raw.writeDb(patient);
                finish();
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                raw.deleteTreatment(treatments.get(selectedTV.getText().toString()));
                initializeAdapter();
                break;
            case Dialog.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
            case Dialog.BUTTON_NEUTRAL:
                break;
        }
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        AlertDialog alertDialog = (AlertDialog) dialog;
        switch (id) {
            case 2:
                alertDialog.setMessage("Вы действительно хотите удалить лечение " + selectedTV.getText());
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
        selectedTV = (TextView) view;
        selectedTV.setBackgroundColor(Color.LTGRAY);
        updateBtn.setEnabled(true);
        deleteBtn.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            initializeAdapter();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                DatePickerDialog dpd = new DatePickerDialog(this, myCallBack, myYear, myMonth,myDay);
                return dpd;
            case 2:
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setTitle("Удаление");
                adb.setMessage("Вы действительно хотите удалить лечение " + selectedTV.getText());
                adb.setPositiveButton("ДА", this);
                adb.setNegativeButton("НЕТ", this);
                return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myYear = year;
            myMonth = month;
            myDay = dayOfMonth;
            dateOfBirthTV.setText(String.format("%04d-%02d-%02d", myYear, myMonth, myDay));
        }
    };

    private void initializeField() {
        if (patient.getFirstName() != null) firstNameET.setText(patient.getFirstName());
        if (patient.getLastName() != null) lastNameET.setText(patient.getLastName());
        if (patient.getPatronymic() != null) patronymicET.setText(patient.getPatronymic());
        if (!patient.isGender()) ((RadioButton) findViewById(R.id.female_radio_btn)).setChecked(true);
        if (patient.getDateOfBirth() != -1) dateOfBirthTV.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date(patient.getDateOfBirth())));

        initializeAdapter();
    }

    private void initializeAdapter() {
        treatmentArrayAdapter.clear();
        treatments = raw.readAllTreatmentsFromDbForPatient(patient.getIIN());
        for (Map.Entry<String, Long> entry : treatments.entrySet()) {
            treatmentArrayAdapter.add(entry.getKey());
        }
        treatmentArrayAdapter.notifyDataSetChanged();
    }
}
