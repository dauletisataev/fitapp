package com.example.android.myhealth.work.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.myhealth.R;
import com.example.android.myhealth.util.EatingTime;
import com.example.android.myhealth.work.Medicament;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterForMeds extends RecyclerView.Adapter<AdapterForMeds.ViewHolder> {
    private LayoutInflater inflater;
    private List<Medicament> medicaments;

    public AdapterForMeds(Context context, List<Medicament> medicaments) {
        this.inflater = LayoutInflater.from(context);
        this.medicaments = medicaments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.medicament_recycle, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Medicament medicament = medicaments.get(position);
        holder.medNameTV.setText(medicament.getMedicamentName());
        holder.dosageTv.setText(medicament.getDosage() + "мг");
        holder.perDiemTV.setText(medicament.getPerDiem() + " раза в день");
        holder.everyTV.setText(getEveryString(medicament.getEvery()));
        holder.amounOfDaysTV.setText(medicament.getAmountOfDays() + " дней");
        holder.eatingTimeTV.setText(getEatingTimeString(medicament.getEatingTime()));
    }

    @Override
    public int getItemCount() {
        return medicaments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView medNameTV;
        private TextView dosageTv;
        private TextView perDiemTV;
        private TextView everyTV;
        private TextView amounOfDaysTV;
        private TextView eatingTimeTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medNameTV = itemView.findViewById(R.id.medicament_name);
            dosageTv = itemView.findViewById(R.id.dosage);
            perDiemTV = itemView.findViewById(R.id.per_diem);
            everyTV = itemView.findViewById(R.id.every);
            amounOfDaysTV = itemView.findViewById(R.id.amount_of_days);
            eatingTimeTV = itemView.findViewById(R.id.eating_time);
        }
    }

    private String getEveryString(Short every) {
        switch (every) {
            case 1:
                return "Ежедневно";
            case 2:
                return "Через день";
            default:
                return "Каждый " + every + "-й день";
        }
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
}
