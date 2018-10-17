package com.example.android.myhealth;

import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.myhealth.util.db.DataBaseRegulator;
import com.example.android.myhealth.util.db.ReadAndWrite;

public class PatientListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, DialogInterface.OnClickListener{
    private ListView listView;
    private ArrayAdapter<String> patientAdapter;
    private ReadAndWrite raw;
    private Button updateBtn;
    private Button deleteBtn;
    private TextView selectedTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        raw = new ReadAndWrite(new DataBaseRegulator(this));

        listView = findViewById(R.id.list_item);
        patientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, raw.readAllPatientsFromDb());
        listView.setAdapter(patientAdapter);
        updateBtn = findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(this);
        deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(this);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_btn:
                Intent intent = new Intent(PatientListActivity.this, PatientEditInfoActivity.class);
                intent.putExtra(DataBaseRegulator.IIN, selectedTV.getText().toString().split(" ")[0]);
                startActivityForResult(intent, 1);
                break;
            case R.id.delete_btn:
                showDialog(1);
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                raw.deletePatient(selectedTV.getText().toString().split(" ")[0]);
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
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Удаление");
        adb.setMessage("Вы действительно хотите удалить пациента " + selectedTV.getText().toString());
        adb.setPositiveButton("ДА", this);
        adb.setNegativeButton("НЕТ", this);
        return adb.create();
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        AlertDialog alertDialog = (AlertDialog) dialog;
        alertDialog.setMessage("Вы действительно хотите удалить пациента " + selectedTV.getText().toString());    }

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
        initializeAdapter();
    }

    private void initializeAdapter() {
        patientAdapter.clear();
        patientAdapter.addAll(raw.readAllPatientsFromDb());
        patientAdapter.notifyDataSetChanged();
    }
}
