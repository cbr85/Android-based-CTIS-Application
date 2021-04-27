package com.example.ctisadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListTesterActivity extends AppCompatActivity {

    RecyclerView tester_list;
    Button add_tester;
    CentreOfficer centreOfficer = new CentreOfficer();
    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("CentreOfficer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tester);

        tester_list = findViewById(R.id.tester_list);
        add_tester = findViewById(R.id.add_tester);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");

        getListTester(new FirebaseCallback() {
                          @Override
                          public void onCallback(List<CentreOfficer> testerList) {
                              AdaptorTester adapter = new AdaptorTester(testerList, centreOfficer, getApplicationContext());
                              tester_list.setAdapter(adapter);
                              tester_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                          }
                      });

        add_tester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), RecordTesterActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                intent.putExtra("tester", (Serializable) null);
                startActivity(intent);
            }
        });

    }

    public interface FirebaseCallback{
        void onCallback(List<CentreOfficer> testerList);
    }

    public void getListTester(final FirebaseCallback firebaseCallback){
        collectionReference.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CentreOfficer> testerList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            CentreOfficer tester = document.toObject(CentreOfficer.class);
                            if (tester.getPosition().equalsIgnoreCase("tester")
                                    && centreOfficer.getCentreId().equals(tester.getCentreId())) {
                                testerList.add(tester);
                            }
                        }
                        firebaseCallback.onCallback(testerList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Failed to collect Location, Check database: ");
                    }
                });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getListTester(new FirebaseCallback() {
            @Override
            public void onCallback(List<CentreOfficer> testerList) {
                AdaptorTester adapter = new AdaptorTester(testerList, centreOfficer, getApplicationContext());
                tester_list.setAdapter(adapter);
                tester_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });
    }
}