package com.example.android.myhealth.work;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Treatment {
    private long id;
    private final String IIN;
    private String diagnosis;
    private String diet;
    private Date startTreatment;
    private boolean isEnd;

    public Treatment(String IIN) {
        this.id = -1;
        this.IIN = IIN;
        this.diagnosis = "";
        this.diet = "";
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getIIN() {
        return IIN;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartTreatment() {
        return startTreatment;
    }

    public void setStartTreatment(Date startTreatment) {
        this.startTreatment = startTreatment;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    @Override
    public String toString() {
        return "Treatment{" +
                "id=" + id +
                ", IIN='" + IIN + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                ", diet='" + diet + '\'' +
                ", startTreatment=" + startTreatment +
                ", isEnd=" + isEnd +
                '}';
    }
}
