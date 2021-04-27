package com.example.ctisadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

public class AdaptorCovidTest extends RecyclerView.Adapter<AdaptorCovidTest.ViewHolder> {

    private List<CovidTest> obj;
    private CentreOfficer centreOfficer;
    private List<Patient> patients;
    private Context context;

    public AdaptorCovidTest(List<CovidTest> obj, CentreOfficer centreOfficer, Context context, List<Patient> patients){
        this.obj = obj;
        this.centreOfficer = centreOfficer;
        this.context = context;
        this.patients = patients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_covid_test, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CovidTest item = obj.get(position);

        holder.covid_test_id.setText(item.getTestID());

        for(Patient p :patients){
            if(p.getPatientId().equals(item.getPatientID())){
                holder.patient_name.setText(p.getUserName());
            }
        }

        holder.patient_status.setText(item.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.getStatus().equals("pending")){
                    Intent intent = new Intent(context, UpdateResultActivity.class);
                    intent.putExtra("covidTest", (Serializable) item);
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context, "Already Completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView covid_test_id;
        public TextView patient_name;
        public TextView patient_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            covid_test_id = itemView.findViewById(R.id.covid_test_id);
            patient_name = itemView.findViewById(R.id.patient_name);
            patient_status = itemView.findViewById(R.id.patient_status);
        }
    }
}
