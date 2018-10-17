package com.example.android.myhealth.util.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.myhealth.util.EatingTime;
import com.example.android.myhealth.work.Medicament;
import com.example.android.myhealth.work.Patient;
import com.example.android.myhealth.work.Treatment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReadAndWrite {
    private DataBaseRegulator dbr;

    public ReadAndWrite(DataBaseRegulator dbr) {
        this.dbr = dbr;
    }

    @SuppressLint("SimpleDateFormat")
    public void writeDb(Patient patient) {
        SQLiteDatabase db = dbr.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DataBaseRegulator.IIN, patient.getIIN());
        cv.put(DataBaseRegulator.PHONE_NUMBER, patient.getPhoneNumber());
        cv.put(DataBaseRegulator.FIRST_NAME, patient.getFirstName());
        cv.put(DataBaseRegulator.LAST_NAME, patient.getLastName());
        cv.put(DataBaseRegulator.PATRONYMIC, patient.getPatronymic());
        cv.put(DataBaseRegulator.BIRTH, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date(patient.getDateOfBirth())));
        cv.put(DataBaseRegulator.GENDER, patient.isGender() ? 1 : 0);

        Cursor c = db.query(DataBaseRegulator.PATIENT_TABLE, null, "iin = " + patient.getIIN(), null, null, null, null);

        if (c.moveToFirst()) {
            db.update(DataBaseRegulator.PATIENT_TABLE, cv, "iin = " + patient.getIIN(), null);
            do {
                System.out.println(
                        "IIN = " + c.getString(c.getColumnIndex(DataBaseRegulator.IIN)) +
                                ", phone = " + c.getString(c.getColumnIndex(DataBaseRegulator.PHONE_NUMBER)) +
                                ", fName = " + c.getString(c.getColumnIndex(DataBaseRegulator.FIRST_NAME)) +
                                ", lName = " + c.getString(c.getColumnIndex(DataBaseRegulator.LAST_NAME)) +
                                ", patronymic = " + c.getString(c.getColumnIndex(DataBaseRegulator.PATRONYMIC)) +
                                ", birth = " + c.getString(c.getColumnIndex(DataBaseRegulator.BIRTH)) +
                                ", gender = " + c.getString(c.getColumnIndex(DataBaseRegulator.GENDER))
                );
            } while (c.moveToNext());
        } else {
            db.insert(DataBaseRegulator.PATIENT_TABLE, null, cv);
        }
        c.close();
    }

    public Treatment writeDb(Treatment treatment) {
        SQLiteDatabase db = dbr.getWritableDatabase();
        ContentValues cv = new ContentValues();
        long id = treatment.getId();

        cv.put(DataBaseRegulator.DIAGNOSIS, treatment.getDiagnosis());
        cv.put(DataBaseRegulator.DIET, treatment.getDiet());
        cv.put(DataBaseRegulator.IIN, treatment.getIIN());
        cv.put(DataBaseRegulator.IS_END, treatment.isEnd() ? "true" : "false");


        Cursor c = db.query(DataBaseRegulator.TREATMENT_TABLE, null, DataBaseRegulator.ID + " = " + treatment.getId(), null, null, null, null);


        if (c.moveToFirst()) {
            db.update(DataBaseRegulator.TREATMENT_TABLE, cv, DataBaseRegulator.ID + " = " + treatment.getId(), null);
            do {
                System.out.println(
                        "id = " + c.getString(c.getColumnIndex(DataBaseRegulator.ID)) +
                                ", diagnosis = " + c.getString(c.getColumnIndex(DataBaseRegulator.DIAGNOSIS)) +
                                ", diet = " + c.getString(c.getColumnIndex(DataBaseRegulator.DIET)) +
                                ", iin = " + c.getString(c.getColumnIndex(DataBaseRegulator.IIN)) +
                                ", startTreatment = " + c.getString(c.getColumnIndex(DataBaseRegulator.TREATMENT_START)) +
                                ", isEnd = " + c.getString(c.getColumnIndex(DataBaseRegulator.IS_END))
                );
            } while (c.moveToNext());
        } else id = db.insert(DataBaseRegulator.TREATMENT_TABLE, null, cv);
        c.close();
        treatment.setId(id);
        return treatment;
    }

    public void writeDb(Medicament medicament) {
        SQLiteDatabase db = dbr.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DataBaseRegulator.MEDICAMENT_NAME, medicament.getMedicamentName());
        cv.put(DataBaseRegulator.DOSAGE, medicament.getDosage());
        cv.put(DataBaseRegulator.PER_DIEM, medicament.getPerDiem());
        cv.put(DataBaseRegulator.AMOUNT_OF_DAYS, medicament.getAmountOfDays());
        cv.put(DataBaseRegulator.EATING_TIME, medicament.getEatingTime().toString());
        cv.put(DataBaseRegulator.EVERY, medicament.getEvery());
        cv.put(DataBaseRegulator.TREATMENT_ID, medicament.getTreatmentID());

        Cursor c = db.query(DataBaseRegulator.MEDICAMENTS_TABLE, null,
                DataBaseRegulator.ID + " = " + medicament.getId(),
                null, null, null, null);

        if (c.moveToFirst()) {
            db.update(DataBaseRegulator.MEDICAMENTS_TABLE, cv,
                    DataBaseRegulator.ID + " = " + medicament.getId(),
                    null);
            do {
                System.out.println(
                        "id = " + c.getString(c.getColumnIndex(DataBaseRegulator.ID)) +
                                ", medName = " + c.getString(c.getColumnIndex(DataBaseRegulator.MEDICAMENT_NAME)) +
                                ", dosage = " + c.getString(c.getColumnIndex(DataBaseRegulator.DOSAGE)) +
                                ", perDiem = " + c.getString(c.getColumnIndex(DataBaseRegulator.PER_DIEM)) +
                                ", amountOfDays = " + c.getString(c.getColumnIndex(DataBaseRegulator.AMOUNT_OF_DAYS)) +
                                ", eatingTime = " + c.getString(c.getColumnIndex(DataBaseRegulator.EATING_TIME)) +
                                ", every = " + c.getString(c.getColumnIndex(DataBaseRegulator.EVERY)) +
                                ", treatmentID = " + c.getString(c.getColumnIndex(DataBaseRegulator.TREATMENT_ID)) +
                                ", count = " + c.getInt(c.getColumnIndex(DataBaseRegulator.COUNT))
                );
            } while (c.moveToNext());
        } else {
            cv.put(DataBaseRegulator.COUNT, 0);
            db.insert(DataBaseRegulator.MEDICAMENTS_TABLE, null, cv);
        }
        c.close();
    }


    @SuppressLint("SimpleDateFormat")
    public Patient readPatientFromDB(String iin, String phoneNumber) {
        Patient patient = new Patient(iin, phoneNumber);
        SQLiteDatabase db = dbr.getWritableDatabase();
        String selection = DataBaseRegulator.IIN + " = " + iin;

        Cursor c = db.query(DataBaseRegulator.PATIENT_TABLE, null, selection, null, null, null, null);
        if (c.moveToFirst()) {
            patient.setPhoneNumber(c.getString(c.getColumnIndex(DataBaseRegulator.PHONE_NUMBER)));
            patient.setFirstName(c.getString(c.getColumnIndex(DataBaseRegulator.FIRST_NAME)));
            patient.setLastName(c.getString(c.getColumnIndex(DataBaseRegulator.LAST_NAME)));
            patient.setPatronymic(c.getString(c.getColumnIndex(DataBaseRegulator.PATRONYMIC)));
            try {
                patient.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(c.getString(c.getColumnIndex(DataBaseRegulator.BIRTH))).getTime());
            } catch (ParseException e) {
                patient.setDateOfBirth(0);
            }
            patient.setGender(Byte.parseByte(c.getString(c.getColumnIndex(DataBaseRegulator.GENDER))) == 1);
        }
        c.close();
        return patient;
    }

    @SuppressLint("SimpleDateFormat")
    public Treatment readTreatmentFromDB(String iin, long treatmentID) {
        Treatment treatment = new Treatment(iin);
        SQLiteDatabase db = dbr.getWritableDatabase();

        Cursor c = db.query(DataBaseRegulator.TREATMENT_TABLE, null, DataBaseRegulator.ID + " = " + treatmentID,
                null, null, null, null);

        if (c.moveToFirst()) {
            treatment.setId(c.getInt(c.getColumnIndex(DataBaseRegulator.ID)));
            treatment.setDiet(c.getString(c.getColumnIndex(DataBaseRegulator.DIET)));
            treatment.setDiagnosis(c.getString(c.getColumnIndex(DataBaseRegulator.DIAGNOSIS)));
            treatment.setEnd(c.getString(c.getColumnIndex(DataBaseRegulator.IS_END)).equalsIgnoreCase("true"));
            try {
                treatment.setStartTreatment(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(c.getString(c.getColumnIndex(DataBaseRegulator.TREATMENT_START))));
            } catch (Exception e) {
                System.out.println("StartDate = null");
            }
        }
        c.close();
        System.out.println(treatment);
        return treatment;
    }

    public Medicament readMedicanemtFromDB(long treatmentID, int mediacamentID) {
        Medicament medicament = new Medicament(treatmentID);
        SQLiteDatabase db = dbr.getWritableDatabase();

        Cursor c = db.query(DataBaseRegulator.MEDICAMENTS_TABLE, null, DataBaseRegulator.ID + " = " + mediacamentID,
                null, null, null, null);

        if (c.moveToFirst()) {
            medicament.setId(c.getInt(c.getColumnIndex(DataBaseRegulator.ID)));
            medicament.setEvery(c.getShort(c.getColumnIndex(DataBaseRegulator.EVERY)));
            medicament.setEatingTime(EatingTime.valueOf(c.getString(c.getColumnIndex(DataBaseRegulator.EATING_TIME))));
            medicament.setAmountOfDays(c.getShort(c.getColumnIndex(DataBaseRegulator.AMOUNT_OF_DAYS)));
            medicament.setPerDiem(Byte.parseByte(c.getString(c.getColumnIndex(DataBaseRegulator.PER_DIEM))));
            medicament.setDosage(c.getInt(c.getColumnIndex(DataBaseRegulator.DOSAGE)));
            medicament.setMedicamentName(c.getString(c.getColumnIndex(DataBaseRegulator.MEDICAMENT_NAME)));
            medicament.setCount(c.getInt(c.getColumnIndex(DataBaseRegulator.COUNT)));
        }
        c.close();
        return medicament;
    }

    public ArrayList<String> readAllPatientsFromDb() {
        ArrayList<String> arrayList = new ArrayList<>();
        SQLiteDatabase db = dbr.getWritableDatabase();

        Cursor c = db.query(DataBaseRegulator.PATIENT_TABLE,
                new String[]{DataBaseRegulator.IIN, DataBaseRegulator.FIRST_NAME, DataBaseRegulator.LAST_NAME},
                null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                arrayList.add(
                        c.getString(c.getColumnIndex(DataBaseRegulator.IIN)) + " " +
                                c.getString(c.getColumnIndex(DataBaseRegulator.FIRST_NAME)) + " " +
                                c.getString(c.getColumnIndex(DataBaseRegulator.LAST_NAME)));
            } while (c.moveToNext());
        }
        c.close();
        return arrayList;
    }

    public Map<String, Long> readAllTreatmentsFromDbForPatient(String iin) {
        Map<String, Long> map = new HashMap<>();
        SQLiteDatabase db = dbr.getWritableDatabase();

        Cursor c = db.query(DataBaseRegulator.TREATMENT_TABLE,
                new String[]{DataBaseRegulator.DIAGNOSIS, DataBaseRegulator.ID, DataBaseRegulator.IS_END},
                DataBaseRegulator.IIN + " = " + iin, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                String diagnosis = c.getString(c.getColumnIndex(DataBaseRegulator.DIAGNOSIS));
                long id = c.getLong(c.getColumnIndex(DataBaseRegulator.ID));
                map.put(diagnosis, id);
            } while (c.moveToNext());
        }
        c.close();
        return map;
    }

    public Map<String, Integer> readAllMedicamentsFromDbForTreatment(long treatmentID) {
        Map<String, Integer> map = new HashMap<>();
        SQLiteDatabase db = dbr.getWritableDatabase();

        Cursor c = db.query(DataBaseRegulator.MEDICAMENTS_TABLE,
                new String[]{DataBaseRegulator.MEDICAMENT_NAME, DataBaseRegulator.ID},
                DataBaseRegulator.TREATMENT_ID + " = " + treatmentID,
                null, null, null, null);

        if (c.moveToFirst()) {
            do {
                String medName = c.getString(c.getColumnIndex(DataBaseRegulator.MEDICAMENT_NAME));
                int id = c.getInt(c.getColumnIndex(DataBaseRegulator.ID));
                map.put(medName, id);
            } while (c.moveToNext());
        }
        c.close();
        return map;
    }

    public ArrayList<Medicament> readAllMedicamentFromDb(long treatmentID) {
        ArrayList<Medicament> list = new ArrayList<>();
        SQLiteDatabase db = dbr.getWritableDatabase();

        Cursor c = db.query(DataBaseRegulator.MEDICAMENTS_TABLE,
                null,
                DataBaseRegulator.TREATMENT_ID + " = " + treatmentID,
                null, null, null, null);

        if (c.moveToFirst()) {
            do {
                Medicament medicament = new Medicament(treatmentID);

                medicament.setId(c.getInt(c.getColumnIndex(DataBaseRegulator.ID)));
                medicament.setEvery(c.getShort(c.getColumnIndex(DataBaseRegulator.EVERY)));
                medicament.setEatingTime(EatingTime.valueOf(c.getString(c.getColumnIndex(DataBaseRegulator.EATING_TIME))));
                medicament.setAmountOfDays(c.getShort(c.getColumnIndex(DataBaseRegulator.AMOUNT_OF_DAYS)));
                medicament.setPerDiem(Byte.parseByte(c.getString(c.getColumnIndex(DataBaseRegulator.PER_DIEM))));
                medicament.setDosage(c.getInt(c.getColumnIndex(DataBaseRegulator.DOSAGE)));
                medicament.setMedicamentName(c.getString(c.getColumnIndex(DataBaseRegulator.MEDICAMENT_NAME)));

                list.add(medicament);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void deletePatient(String iin) {
        SQLiteDatabase db = dbr.getWritableDatabase();

        Cursor c = db.query(DataBaseRegulator.TREATMENT_TABLE,
                new String[]{DataBaseRegulator.ID}, DataBaseRegulator.IIN + " = " + iin, null,
                null, null, null);
        if (c.moveToFirst()) {
            do {
                deleteTreatment(c.getLong(c.getColumnIndex(DataBaseRegulator.ID)));
            } while (c.moveToNext());
        }
        int delP = db.delete(DataBaseRegulator.PATIENT_TABLE, DataBaseRegulator.IIN + " = " + iin, null);
        System.out.println("Удалено: " + delP + " пациентов");
        c.close();
    }

    public void deleteTreatment(long treatmentID) {
        SQLiteDatabase db = dbr.getWritableDatabase();

        int delM = db.delete(DataBaseRegulator.MEDICAMENTS_TABLE, DataBaseRegulator.TREATMENT_ID + " = " + treatmentID, null);
        int delT = db.delete(DataBaseRegulator.TREATMENT_TABLE, DataBaseRegulator.ID + " = " + treatmentID, null);

        System.out.println("Удалено: " + delT + " лечений и " + delM + " медикаментов");
    }

    public void deleteMedicament(long treatmentID, int medicamentID) {
        SQLiteDatabase db = dbr.getWritableDatabase();

        int delM = db.delete(DataBaseRegulator.MEDICAMENTS_TABLE, DataBaseRegulator.TREATMENT_ID + " = " + treatmentID + " AND " +
                DataBaseRegulator.ID + " = " + medicamentID, null);
        System.out.println("Удалено: " + delM + " медикаментов");
    }

    public void writeStarTreatmentInDb(long treatmentID, String date) {
        SQLiteDatabase db = dbr.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DataBaseRegulator.TREATMENT_START, date);

        db.update(DataBaseRegulator.TREATMENT_TABLE, cv, DataBaseRegulator.ID + " = " + treatmentID, null);
    }

    public ArrayList<Medicament> readAllMedicamentforPatientSelectStartTreatment(final String IIN) {
        SQLiteDatabase db = dbr.getWritableDatabase();
        ArrayList<Medicament> medicaments = new ArrayList<>();

        for (Map.Entry<String, Long> treatments : readAllTreatmentsFromDbForPatient(IIN).entrySet()) {
            Treatment treatment = readTreatmentFromDB(IIN, treatments.getValue());
            if (treatment.getStartTreatment() != null && !treatment.isEnd()) {
                int day = (int) ((new Date().getTime() - treatment.getStartTreatment().getTime()) / 86400000);
                int max = 0;

                for (Map.Entry<String, Integer> entryMed : readAllMedicamentsFromDbForTreatment(treatment.getId()).entrySet()) {
                    Medicament medicament = readMedicanemtFromDB(treatment.getId(), entryMed.getValue());
                    int durationOfTreatment = ((medicament.getAmountOfDays() - 1) * medicament.getEvery());
                    if ((durationOfTreatment - day) >= 0) {
                        if ((day % medicament.getEvery()) == 0) {
                            int z = ((day + medicament.getEvery()) / medicament.getEvery() * medicament.getPerDiem()) - medicament.getCount();
                            if (z > medicament.getPerDiem()) {
                                incrementMedicamentCountInDb(medicament, z - medicament.getPerDiem());
                                medicament.setToDayIntake(medicament.getPerDiem());
                            } else medicament.setToDayIntake((short) z);
                            if (medicament.getToDayIntake() > 0) medicaments.add(medicament);
                        }
                    }
                    if (durationOfTreatment > max) max = durationOfTreatment;
                }

                if ((max - day) < 0) {
                    ContentValues cv = new ContentValues();
                    cv.put(DataBaseRegulator.IS_END, "true");
                    db.update(DataBaseRegulator.TREATMENT_TABLE, cv, DataBaseRegulator.ID + " = " + treatment.getId(), null);
                }
            }
        }
        return medicaments;
    }

    public void incrementMedicamentCountInDb(Medicament medicament, int increment) {
        SQLiteDatabase db = dbr.getWritableDatabase();
        ContentValues cv = new ContentValues();
        medicament.setCount(medicament.getCount() + increment);
        cv.put(DataBaseRegulator.COUNT, medicament.getCount());
        db.update(DataBaseRegulator.MEDICAMENTS_TABLE, cv, DataBaseRegulator.ID + " = " + medicament.getId(), null);
    }
}
