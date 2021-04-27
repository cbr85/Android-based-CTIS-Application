package com.example.ctis;

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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdaptorCovidTest extends RecyclerView.Adapter<AdaptorCovidTest.ViewHolder> {

    private List<CovidTest> obj;
    private Context context;
    private List<TestKit> testKits;

    private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    public AdaptorCovidTest(List<CovidTest> obj, Context context, List<TestKit> testKits){
        this.obj = obj;
        this.context = context;
        this.testKits = testKits;
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
        holder.test_result.setText(item.getResult());

        Date dateTest = new Date(Long.valueOf(item.getTestDate()));
        Date dateResult = new Date(Long.valueOf(item.getResultDate()));

        holder.test_date.setText(sdf2.format(dateTest));
        holder.result_date.setText(sdf2.format(dateResult));

        for(TestKit tk: testKits){
            if(tk.getKidID().equals(item.getTestKitID())){
                holder.testing_kit.setText(tk.getTestName());
            }
        }

    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView covid_test_id;
        public TextView test_result;
        public TextView test_date;
        public TextView result_date;
        public TextView testing_kit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            covid_test_id = itemView.findViewById(R.id.covid_test_id);
            test_result = itemView.findViewById(R.id.test_result);
            test_date = itemView.findViewById(R.id.test_date);
            testing_kit = itemView.findViewById(R.id.testing_kit);
            result_date = itemView.findViewById(R.id.result_date);
        }
    }
}
