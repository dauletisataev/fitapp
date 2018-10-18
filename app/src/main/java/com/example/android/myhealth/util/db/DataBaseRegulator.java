package com.example.android.myhealth.util.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseRegulator extends SQLiteOpenHelper {
    public static final String DB_NAME = "MyHealth";
    public static final String IIN = "iin";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PATRONYMIC = "patronymic";
    public static final String BIRTH = "birth";
    public static final String GENDER = "gender";
    public static final String ID = "id";
    public static final String DIAGNOSIS = "diagnosis";
    public static final String DIET = "diet";
    public static final String MEDICAMENT_NAME = "mediacamentName";
    public static final String DOSAGE = "dosage";
    public static final String PER_DIEM = "perDiem";
    public static final String AMOUNT_OF_DAYS = "amountOfDays";
    public static final String EATING_TIME = "eatingTime";
    public static final String EVERY = "every";
    public static final String PATIENT_TABLE = "patient";
    public static final String TREATMENT_TABLE = "treatment";
    public static final String MEDICAMENTS_TABLE = "medicaments";
    public static final String TREATMENT_ID = "treatmentID";
    public static final String TREATMENT_START = "treatmentStart";
    public static final String IS_END = "isEnd";
    public static final String COUNT = "count";
    public static final String MEDICATION_TIME = "medicationTime";
    public static final String TIME_OF_DAY = "timeOfDay";
    public static final String ID_MED = "idMed";
    public static final String INDEX = "index";
    public static final String IS_EQUALLY = "isEqually";

    public DataBaseRegulator(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + PATIENT_TABLE + " (" +
                IIN + " text, " +
                PHONE_NUMBER + " text, " +
                FIRST_NAME + " text, " +
                LAST_NAME + " text, " +
                PATRONYMIC + " text, " +
                BIRTH + " text, " +
                GENDER + " blob" + ");");

        db.execSQL("create table " + TREATMENT_TABLE + " (" +
                ID + " integer primary key autoincrement," +
                DIAGNOSIS + " text," +
                DIET + " text," +
                IIN + " text," +
                TREATMENT_START + " text," +
                IS_END + " text" +
                ");");

        db.execSQL("create table " + MEDICAMENTS_TABLE + " (" +
                ID + " integer primary key autoincrement," +
                MEDICAMENT_NAME + " text," +
                DOSAGE + " integer," +
                PER_DIEM + " integer," +
                AMOUNT_OF_DAYS + " integer," +
                EATING_TIME + " text," +
                EVERY + " integer," +
                TREATMENT_ID + " text," +
                COUNT + " integer" +
                IS_EQUALLY + " text" + ");");

        db.execSQL("create table " + MEDICATION_TIME + " (" +
                TIME_OF_DAY + "text,"  +
                ID_MED + "integer," +
                INDEX + "integer" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
