package com.example.android.myhealth.work;

import com.example.android.myhealth.util.EatingTime;

public class Medicament {
    private int id;
    private long treatmentID;
    private String medicamentName;
    private int dosage;
    private byte perDiem;
    private short amountOfDays;
    private EatingTime eatingTime;
    private short every;
    private int count;
    private short toDayIntake;
//comment
    public Medicament(long treatmentID) {
        id = -1;
        this.treatmentID = treatmentID;
    }

    public String getMedicamentName() {
        return medicamentName;
    }

    public void setMedicamentName(String medicamentName) {
        this.medicamentName = medicamentName;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    public byte getPerDiem() {
        return perDiem;
    }

    public void setPerDiem(byte perDiem) {
        this.perDiem = perDiem;
    }

    public short getAmountOfDays() {
        return amountOfDays;
    }

    public void setAmountOfDays(short amountOfDays) {
        this.amountOfDays = amountOfDays;
    }

    public short getEvery() {
        return every;
    }

    public void setEvery(short every) {
        this.every = every;
    }

    public EatingTime getEatingTime() {
        return eatingTime;
    }

    public void setEatingTime(EatingTime eatingTime) {
        this.eatingTime = eatingTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public short getToDayIntake() {
        return toDayIntake;
    }

    public void setToDayIntake(short toDayIntake) {
        this.toDayIntake = toDayIntake;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTreatmentID() {
        return treatmentID;
    }

    @Override
    public String toString() {
        return medicamentName + ": " + toDayIntake + " раза по " + dosage + "мг";
    }
}
