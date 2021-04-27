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

public class ListPatientActivity extends AppCompatActivity {

    RecyclerView patient_list;
    Button add_patient;
    CentreOfficer centreOfficer = new CentreOfficer();
    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("Patient");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patient);

        patient_list = findViewById(R.id.patient_list);
        add_patient = findViewById(R.id.add_patient);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");

        getListTester(new FirebaseCallback() {
            @Override
            public void onCallback(List<Patient> patientsList) {
                AdaptorPatient adapter = new AdaptorPatient(patientsList, centreOfficer, getApplicationContext());
                patient_list.setAdapter(adapter);
                patient_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });

        add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), RecordNewTestActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                startActivity(intent);
            }
        });
    }

    public interface FirebaseCallback{
        void onCallback(List<Patient> patientsList);
    }

    public void getListTester(final FirebaseCallback firebaseCallback){
        collectionReference.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Patient> patients = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Patient tester = document.toObject(Patient.class);
                            if (tester.getCentreId().equals(centreOfficer.getCentreId())) {
                                patients.add(tester);
                            }
                        }
                        firebaseCallback.onCallback(patients);
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
            public void onCallback(List<Patient> patientsList) {
                AdaptorPatient adapter = new AdaptorPatient(patientsList, centreOfficer, getApplicationContext());
                patient_list.setAdapter(adapter);
                patient_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });
    }
}