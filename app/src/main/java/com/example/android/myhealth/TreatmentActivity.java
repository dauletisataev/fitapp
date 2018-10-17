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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.myhealth.util.db.DataBaseRegulator;
import com.example.android.myhealth.util.db.ReadAndWrite;
import com.example.android.myhealth.work.Treatment;

import java.util.Map;

public class TreatmentActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, DialogInterface.OnClickListener {
    private ReadAndWrite raw;
    private ArrayAdapter<String> medicamentArrayAdapter;
    private Map<String, Integer> medicaments;
    private Treatment treatment;
    private String iin;
    private long id;
    private EditText diagnosisET;
    private EditText tableNumberET;
    private ListView mediacementsLV;
    private Button addBtn;
    private Button doneBtn;
    private Button updateBtn;
    private Button deleteBtn;
    private TextView selectedTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment);

        raw = new ReadAndWrite(new DataBaseRegulator(this));

        iin = getIntent().getStringExtra(DataBaseRegulator.IIN);
        id = getIntent().getLongExtra(DataBaseRegulator.ID, -1);
        treatment = raw.readTreatmentFromDB(iin, id);

        medicamentArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        diagnosisET = findViewById(R.id.diagnosis_edit_text);
        tableNumberET = findViewById(R.id.table_number_edit_text);
        mediacementsLV = findViewById(R.id.medicaments_list_view);
        mediacementsLV.setAdapter(medicamentArrayAdapter);
        mediacementsLV.setOnItemClickListener(this);
        addBtn = findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
        doneBtn = findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(this);
        updateBtn = findViewById(R.id.update_btn);
        updateBtn.setOnClickListener(this);
        deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(this);

        initializeField();
        System.out.println("TREATMENT_ID = " + treatment.getId());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
        selectedTV = (TextView) view;
        selectedTV.setBackgroundColor(Color.LTGRAY);
        updateBtn.setEnabled(true);
        if(!treatment.isEnd()) {
            deleteBtn.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        treatment.setDiagnosis(diagnosisET.getText().toString());
        treatment.setDiet(tableNumberET.getText().toString());
        if (!treatment.getDiagnosis().equalsIgnoreCase("")) {
            switch (v.getId()) {
                case R.id.add_btn:
                    if (!diagnosisET.getText().toString().equals("")) {
                        initializeTreatment();
                        Intent intent = new Intent(this, MedicamentActivity.class);
                        intent.putExtra(DataBaseRegulator.TREATMENT_ID, treatment.getId());
                        intent.putExtra(DataBaseRegulator.ID, -1);
                        startActivityForResult(intent, 1);
                    }
                    break;
                case R.id.update_btn:
                    Intent intent = new Intent(this, MedicamentActivity.class);
                    intent.putExtra(DataBaseRegulator.TREATMENT_ID, treatment.getId());
                    intent.putExtra(DataBaseRegulator.ID, medicaments.get(selectedTV.getText().toString()));
                    startActivityForResult(intent, 1);
                    break;
                case R.id.delete_btn:
                    showDialog(1);
                    break;
                case R.id.done_btn:
                    setResult(RESULT_OK, null);
                    initializeTreatment();
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                raw.deleteMedicament(treatment.getId(), medicaments.get(selectedTV.getText().toString()));
                initializedAdapter();
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
        alertDialog.setMessage("Вы действительно хотите удалить медикамент " + selectedTV.getText().toString());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Удаление");
        adb.setMessage("Вы действительно хотите удалить медикамент " + selectedTV.getText().toString());
        adb.setPositiveButton("ДА", this);
        adb.setNegativeButton("НЕТ", this);
        return adb.create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            initializedAdapter();
        }
    }

    private void initializeField () {
        if (treatment.getDiagnosis() != null) diagnosisET.setText(treatment.getDiagnosis());
        if (treatment.getDiet() != null) tableNumberET.setText(treatment.getDiet());
        if (treatment.isEnd()) {
            diagnosisET.setEnabled(false);
            tableNumberET.setEnabled(false);
            addBtn.setEnabled(false);
        }
        initializedAdapter();
    }

    private void initializedAdapter() {
        medicamentArrayAdapter.clear();
        medicaments = raw.readAllMedicamentsFromDbForTreatment(treatment.getId());
        for (Map.Entry<String, Integer> entry : medicaments.entrySet()) {
            medicamentArrayAdapter.add(entry.getKey());
        }
        medicamentArrayAdapter.notifyDataSetChanged();
    }

    private void initializeTreatment() {
        treatment = raw.writeDb(treatment);
    }
}
