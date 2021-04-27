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

public class AdaptorTestKit extends RecyclerView.Adapter<AdaptorTestKit.ViewHolder> {

    private List<TestKit> obj;
    private CentreOfficer centreOfficer;
    private Context context;

    public AdaptorTestKit(List<TestKit> obj, CentreOfficer centreOfficer, Context context){
        this.obj = obj;
        this.centreOfficer = centreOfficer;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_test_kit, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TestKit item = obj.get(position);

        holder.test_kit_name.setText(item.getTestName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ManageTestKitStockActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                intent.putExtra("testKit", (Serializable) item);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView test_kit_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            test_kit_name = itemView.findViewById(R.id.test_kit_name);
        }
    }
}
