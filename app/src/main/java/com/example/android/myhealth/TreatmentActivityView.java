package com.example.android.myhealth;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.android.myhealth.util.EatingTime;
import com.example.android.myhealth.util.db.DataBaseRegulator;
import com.example.android.myhealth.util.db.ReadAndWrite;
import com.example.android.myhealth.work.Medicament;
import com.example.android.myhealth.work.Treatment;
import com.example.android.myhealth.work.adapters.AdapterForMeds;
import com.example.android.myhealth.work.adapters.SwipeController;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TreatmentActivityView extends AppCompatActivity implements View.OnClickListener {
    private Treatment treatment;
    private String iin;
    private long id;
    private ReadAndWrite raw;
    private Button startTreatment;
    private RecyclerView recyclerView;
    private AdapterForMeds adapter;
    private RecyclerView.LayoutManager manager;

    private LinearLayout container;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_view);
        raw = new ReadAndWrite(new DataBaseRegulator(this));

        iin = getIntent().getStringExtra(DataBaseRegulator.IIN);
        id = getIntent().getLongExtra(DataBaseRegulator.ID, -1);
        treatment = raw.readTreatmentFromDB(iin, id);
        container = findViewById(R.id.container_linear_layout);
        startTreatment = findViewById(R.id.start_btn);
        startTreatment.setOnClickListener(this);
        recyclerView = findViewById(R.id.recycler_view);
        initializeView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:
                @SuppressLint("SimpleDateFormat")
                String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
                raw.writeStarTreatmentInDb(treatment.getId(), start);
                startTreatment.setEnabled(false);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("SetTextI18n")
    private void initializeView() {
        if (treatment.getStartTreatment() != null) {
            startTreatment.setEnabled(false);
        }
        if (treatment != null) {
            TextView diagnosisTextView = new TextView(this);
            diagnosisTextView.setText("Диагноз: " + treatment.getDiagnosis());
            diagnosisTextView.setTextSize(28);

            TextView tableTextView = new TextView(this);
            tableTextView.setText("Стол №" + treatment.getDiet());
            tableTextView.setTextSize(28);

            TextView padding = new TextView(this);
            padding.setHeight(30);

            manager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(manager);

            adapter = new AdapterForMeds(this, raw.readAllMedicamentFromDb(treatment.getId()));
            recyclerView.setAdapter(adapter);

            SwipeController controller = new SwipeController();

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(controller);
            itemTouchHelper.attachToRecyclerView(recyclerView);

//            container.addView(diagnosisTextView);
//            container.addView(tableTextView);
//            container.addView(padding);
//
//            for (Medicament medicament : raw.readAllMedicamentFromDb(treatment.getId())) {
//                TableLayout tableLayout = new TableLayout(this);
//                tableLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                tableLayout.setStretchAllColumns(true);
//                tableLayout.setClickable(true);
//                tableLayout.setBackgroundColor(Color.BLACK);
//                tableLayout.setPadding(2, 2, 2, 2);
//
//
//                TableRow tableRow1 = new TableRow(this);
//                tableRow1.setBackgroundColor(Color.BLACK);
//                TableRow tableRow2 = new TableRow(this);
//                tableRow2.setBackgroundColor(Color.BLACK);
//                TableRow tableRow3 = new TableRow(this);
//                tableRow3.setBackgroundColor(Color.BLACK);
//                TableRow tableRow4 = new TableRow(this);
//                tableRow4.setBackgroundColor(Color.BLACK);
//
//                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
//                layoutParams.setMargins(3, 3, 3, 3);
//
//
//                TextView textView1 = new TextView(this);
//                textView1.setText(medicament.getMedicamentName());
//                textView1.setLayoutParams(layoutParams);
//                textView1.setTextSize(18);
//                textView1.setPadding(25,0,0,0);
//                textView1.setBackgroundColor(Color.WHITE);
//
//                TextView textView2 = new TextView(this);
//                textView2.setText("По " + medicament.getDosage() + " мг");
//                textView2.setLayoutParams(layoutParams);
//                textView2.setPadding(25,0,0,0);
//                textView2.setBackgroundColor(Color.WHITE);
//
//                TextView textView3 = new TextView(this);
//                textView3.setText(medicament.getPerDiem() + " раза в день");
//                textView3.setLayoutParams(layoutParams);
//                textView3.setPadding(25,0,0,0);
//                textView3.setBackgroundColor(Color.WHITE);
//
//                TextView textView4 = new TextView(this);
//                textView4.setText(getEveryString(medicament.getEvery()));
//                textView4.setLayoutParams(layoutParams);
//                textView4.setPadding(25,0,0,0);
//                textView4.setBackgroundColor(Color.WHITE);
//
//                TextView textView5 = new TextView(this);
//                textView5.setText(medicament.getAmountOfDays() + " дней");
//                textView5.setLayoutParams(layoutParams);
//                textView5.setPadding(25,0,0,0);
//                textView5.setBackgroundColor(Color.WHITE);
//
//                TextView textView6 = new TextView(this);
//                textView6.setText(getEatingTimeString(medicament.getEatingTime()));
//                textView6.setLayoutParams(layoutParams);
//                textView6.setGravity(Gravity.CENTER_HORIZONTAL);
//                textView6.setBackgroundColor(Color.WHITE);
//
//                tableRow1.addView(textView1);
//                tableRow2.addView(textView2);
//                tableRow2.addView(textView3);
//                tableRow3.addView(textView4);
//                tableRow3.addView(textView5);
//                tableRow4.addView(textView6);
//
//                tableLayout.addView(tableRow1);
//                tableLayout.addView(tableRow2);
//                tableLayout.addView(tableRow3);
//                tableLayout.addView(tableRow4);
//
//                container.addView(tableLayout);
//
//                View view = new View(this);
//                view.setMinimumHeight(30);
//
//                container.addView(view);
//            }
        }
    }

//    private String getEveryString(Short every) {
//        switch (every) {
//            case 1:
//                return "Ежедневно";
//            case 2:
//                return "Через день";
//            default:
//                return "Каждый " + every + "-й день";
//        }
//    }
//
//    private String getEatingTimeString(EatingTime eatingTime) {
//        switch (eatingTime) {
//            case AFTER:
//                return "До еды";
//            case WHILE:
//                return "Во время еды";
//            case BEFORE:
//                return "После еды";
//                default:
//                    return "Не зависит от приема пищи";
//        }
//    }
}
