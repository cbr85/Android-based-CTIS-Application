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

public class AdaptorPatient extends RecyclerView.Adapter<AdaptorPatient.ViewHolder> {

    private List<Patient> obj;
    private CentreOfficer centreOfficer;
    private Context context;

    public AdaptorPatient(List<Patient> obj, CentreOfficer centreOfficer, Context context){
        this.obj = obj;
        this.centreOfficer = centreOfficer;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_tester, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Patient item = obj.get(position);

        holder.tester_name.setText(item.getUserName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecordNewTestActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                intent.putExtra("patient", (Serializable) item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tester_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tester_name = itemView.findViewById(R.id.tester_name);
        }
    }
}
