package com.example.ctisadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class AdaptorTestReport extends RecyclerView.Adapter<AdaptorTestReport.ViewHolder> {

    private List<CovidTest> obj;
    private CentreOfficer centreOfficer;
    private Context context;
    private List<Patient> patients;
    private List<CentreOfficer> centreOfficers;

    public AdaptorTestReport(List<CovidTest> obj, CentreOfficer centreOfficer, Context context, List<Patient> patients, List<CentreOfficer> centreOfficers){
        this.obj = obj;
        this.centreOfficer = centreOfficer;
        this.context = context;
        this.patients = patients;
        this.centreOfficers = centreOfficers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_test_result, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CovidTest item = obj.get(position);
        holder.covid_test_id.setText(item.getTestID());

        for(Patient patient: patients){
            if(patient.getPatientId().equals(item.getPatientID())){
                holder.patient_name.setText(patient.getUserName());
            }
        }

        for(CentreOfficer centreOfficer: centreOfficers){
            if(centreOfficer.getCentreOfficerId().equals(item.getCenterOfficeID())){
                holder.tester_name.setText(centreOfficer.getUserName());
            }
        }
    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView covid_test_id;
        public TextView patient_name;
        public TextView tester_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            covid_test_id = itemView.findViewById(R.id.covid_test_id);
            patient_name = itemView.findViewById(R.id.patient_name);
            tester_name = itemView.findViewById(R.id.tester_name);
        }
    }
}
