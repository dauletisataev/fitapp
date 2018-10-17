package com.example.android.myhealth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.example.android.myhealth.miBandHelper.util.BM;
import com.example.android.myhealth.miBandHelper.util.MiBand;
import com.example.android.myhealth.miBandHelper.util.Profile;
import com.example.android.myhealth.miBandHelper.util.Protocol;
import com.example.android.myhealth.miBandHelper.util.UserInfo;
import com.example.android.myhealth.util.EatingTime;
import com.example.android.myhealth.util.db.DataBaseRegulator;
import com.example.android.myhealth.util.db.ReadAndWrite;
import com.example.android.myhealth.work.Medicament;
import com.example.android.myhealth.work.Patient;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;


public class PatientActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, DialogInterface.OnClickListener{
    private String iin;
    private ReadAndWrite raw;
    private Patient patient;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<Medicament> medicamentAdapter;
    private ArrayList<Medicament> medicaments;
    private Map<String, Long> treatments;
    private MiBand miBand;
    private boolean isShowDialog;
    private Handler dialogHandler;
    private Medicament medicament;

    private ListView treatmentLV;
    private ListView medicamentsLV;
    private TextView fullName;
    private TextView mibandTV;
    private Button mbBtn;
    private Button vibrationBtn;
    private Button heartRateBtn;
    private Button dialogBtn;
    private Handler heartHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        raw = new ReadAndWrite(new DataBaseRegulator(this));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        medicamentAdapter = new ArrayAdapter<>(this, R.layout.adapter_textview_edition);

        iin = getIntent().getStringExtra("iin");
        patient = raw.readPatientFromDB(iin, null);

        fullName = findViewById(R.id.full_name);


        treatmentLV = findViewById(R.id.treatments_list_view);
        treatmentLV.setAdapter(adapter);
        treatmentLV.setOnItemClickListener(this);
        medicamentsLV = findViewById(R.id.medicaments_list_view);
        medicamentsLV.setAdapter(medicamentAdapter);
        medicamentsLV.setOnItemClickListener(this);

        mbBtn = findViewById(R.id.mb_btn);
        mibandTV = findViewById(R.id.miband_info);
        vibrationBtn = findViewById(R.id.vibration_btn);
        heartRateBtn = findViewById(R.id.heartrate_btn);
        dialogBtn = findViewById(R.id.dialog_btn);

        miBand = new MiBand();

        mbBtn.setOnClickListener(this);
        vibrationBtn.setOnClickListener(this);
        heartRateBtn.setOnClickListener(this);
        dialogBtn.setOnClickListener(this);

        medicaments = raw.readAllMedicamentforPatientSelectStartTreatment(patient.getIIN());

        medicamentAdapter.addAll(medicaments);
        onView();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @SuppressLint("HandlerLeak")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mb_btn:
                miBand.setBluetoothDevice(BM.getAdapter().getRemoteDevice("C8:0F:10:3E:3B:40"));

                if (miBand.getBluetoothDevice() != null) {
                    BM.initializeCallBack(miBand);
                    BM.setBluetoothGatt(miBand.getBluetoothDevice().connectGatt(PatientActivity.this,false, BM.getCallback(), BluetoothDevice.TRANSPORT_LE));

                    if (BM.getBluetoothGatt().connect()) {
                        mibandTV.setText(R.string.miband_enabled);
                    }
                }
                break;
            case R.id.vibration_btn:
                BM.getBluetoothGatt().connect();
                BM.getCallback().writeCharacteristic(Profile.UUID_SERVICE_VIBRATION, Profile.UUID_CHAR_VIBRATION, Protocol.VIBRATION_10_TIMES_WITH_LED);
                break;
            case R.id.heartrate_btn:
                UserInfo userInfo = new UserInfo(20271234, 1, 25, 180, 78, "Tima", 0);
                byte[] data = userInfo.getBytes(miBand.getBluetoothDevice().getAddress());
                BM.getCallback().writeCharacteristic(Profile.UUID_SERVICE_MILI, Profile.UUID_CHAR_USER_INFO, data);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BM.getCallback().setNotifyListener(Profile.UUID_SERVICE_HEARTRATE, Profile.UUID_NOTIFICATION_HEARTRATE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BM.getCallback().writeCharacteristic(Profile.UUID_SERVICE_HEARTRATE, Profile.UUID_CHAR_HEARTRATE, Protocol.START_HEART_RATE_SCAN);

                heartHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        showHeartRate(String.valueOf(msg.what));
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int value = 0;
                        while (true) {
                            if (miBand.getHeartRate() != value)  {
                                value = miBand.getHeartRate();
                                heartHandler.sendEmptyMessage(miBand.getHeartRate());
                            }
                        }
                    }
                }).start();
                break;
            case R.id.dialog_btn:
                dialogHandler = new Handler() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                    @Override
                    public void handleMessage(Message msg) {
                        if (isShowDialog) {
                            BM.getBluetoothGatt().connect();
                            BM.getCallback().writeCharacteristic(Profile.UUID_SERVICE_VIBRATION, Profile.UUID_CHAR_VIBRATION, Protocol.VIBRATION_10_TIMES_WITH_LED);
                            showDialog(2);
                            isShowDialog = false;
                        }
                    }
                };
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isShowDialog) {
                            try {
                                Thread.sleep(15000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isShowDialog = true;
                            dialogHandler.sendEmptyMessage(1);
                        }
                    }
                }).start();
                break;
        }
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case 1:
                AlertDialog alertDialog = (AlertDialog) dialog;
                alertDialog.setTitle(medicament.getMedicamentName());
                alertDialog.setMessage(medicament.getDosage() + " мг, " + getEatingTimeString(medicament.getEatingTime()));
                break;
            case 2:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.medicaments_list_view:
                medicament = medicaments.get(position);
                showDialog(1);
                break;
            case R.id.treatments_list_view:
                Intent intent = new Intent(PatientActivity.this, TreatmentActivityView.class);
                intent.putExtra(DataBaseRegulator.IIN, patient.getIIN());
                intent.putExtra(DataBaseRegulator.ID, treatments.get(((TextView) view).getText().toString()));
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        switch (id) {
            case 1:
                adb.setTitle(medicament.getMedicamentName());
                adb.setMessage(medicament.getDosage() + " мг, " + getEatingTimeString(medicament.getEatingTime()));
                adb.setPositiveButton("Принял", this);
                break;
            case 2:
                Date date = new Date();
                adb.setTitle(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " Время принимать лекарства!");
                adb.setMessage("Примите пожалуйста 1 таблетку Кларитромицина в дозировке 500мг и запейте водой");
                break;
        }
        return adb.create();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                raw.incrementMedicamentCountInDb(medicament, 1);
                initializeAdapter();
                break;
            case Dialog.BUTTON_NEGATIVE:
                break;
            case Dialog.BUTTON_NEUTRAL:
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void onView() {
        if (patient != null) {
            fullName.setText(patient.getLastName() + " " + patient.getFirstName() + " " + patient.getPatronymic());

            initializeAdapter();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        initializeAdapter();
    }

    private void initializeAdapter() {
        adapter.clear();
        treatments = raw.readAllTreatmentsFromDbForPatient(iin);
        for (Map.Entry<String, Long> entry : treatments.entrySet()) {
            adapter.add(entry.getKey());
        }
        adapter.notifyDataSetChanged();

        medicamentAdapter.clear();
        medicaments = raw.readAllMedicamentforPatientSelectStartTreatment(patient.getIIN());
        medicamentAdapter.addAll(medicaments);
        medicamentAdapter.notifyDataSetChanged();
    }

    private String getEatingTimeString(EatingTime eatingTime) {
        switch (eatingTime) {
            case AFTER:
                return "До еды";
            case WHILE:
                return "Во время еды";
            case BEFORE:
                return "После еды";
            default:
                return "Не зависит от приема пищи";
        }
    }

    private void showHeartRate(String heartRate) {
        heartRateBtn.setText(heartRate + " уд/мин");
    }
}
