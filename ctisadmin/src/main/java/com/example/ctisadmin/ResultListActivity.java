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
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ResultListActivity extends AppCompatActivity {

    Button search_button_testId;
    EditText search_text_testId;
    RecyclerView test_list;

    CentreOfficer centreOfficer = new CentreOfficer();
    List<Patient> patients = new ArrayList<>();
    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("CovidTest");
    CollectionReference collectionReferencePatient = FirebaseFirestore.getInstance()
            .collection("Patient");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");

        search_button_testId = findViewById(R.id.search_button_testId);
        search_text_testId = findViewById(R.id.search_text_testId);
        test_list = findViewById(R.id.test_list);

        setupPatient(new FirebaseCallbackPatient() {
            @Override
            public void onCallback(List<Patient> patientList) {
                patients = patientList;
                getListCovidTest(new FirebaseCallback() {
                    @Override
                    public void onCallback(List<CovidTest> covidTestList) {
                        List<CovidTest> covidTests = new ArrayList<>();
                        for(CovidTest ct:covidTestList){
                            if(ct.getCenterOfficeID().equalsIgnoreCase(centreOfficer.getCentreOfficerId())){
                                covidTests.add(ct);
                            }
                        }
                        AdaptorCovidTest adapter = new AdaptorCovidTest(covidTests, centreOfficer, getApplicationContext(), patients);
                        test_list.setAdapter(adapter);
                        test_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                }, "");

                search_button_testId.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String searchingVal = search_text_testId.getText().toString();

                        if(searchingVal.isEmpty() || searchingVal.equalsIgnoreCase("")){
                            search_text_testId.setError("Cannot be empty");
                            search_text_testId.requestFocus();
                            return;
                        }

                        getListCovidTest(new FirebaseCallback() {
                            @Override
                            public void onCallback(List<CovidTest> covidTestList) {
                                List<CovidTest> covidTests = new ArrayList<>();
                                for(CovidTest ct:covidTestList){
                                    if(ct.getCenterOfficeID().equalsIgnoreCase(centreOfficer.getCentreOfficerId())){
                                        covidTests.add(ct);
                                    }
                                }
                                AdaptorCovidTest adapter = new AdaptorCovidTest(covidTests, centreOfficer, getApplicationContext(), patients);
                                test_list.setAdapter(adapter);
                                test_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            }
                        }, searchingVal);
                    }
                });
            }
        });

    }

    public interface FirebaseCallback{
        void onCallback(List<CovidTest> covidTestList);
    }

    public interface FirebaseCallbackPatient{
        void onCallback(List<Patient> patientList);
    }

    public void getListCovidTest(final FirebaseCallback firebaseCallback, final String search){
        collectionReference.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CovidTest> covidTests = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            CovidTest covidTest = document.toObject(CovidTest.class);
                            if(!search.equals("")){
                                if (covidTest.getTestID().equals(search)) {
                                    covidTests.add(covidTest);
                                }
                            }else{
                                covidTests.add(covidTest);
                            }
                        }
                        firebaseCallback.onCallback(covidTests);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Failed to collect Location, Check database: ");
                    }
                });
    }

    public void setupPatient(final FirebaseCallbackPatient firebaseCallbackPatient){
        collectionReferencePatient.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Patient patient = document.toObject(Patient.class);
                            patients.add(patient);
                        }
                        firebaseCallbackPatient.onCallback(patients);
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
        getListCovidTest(new FirebaseCallback() {
            @Override
            public void onCallback(List<CovidTest> covidTestList) {
                List<CovidTest> covidTests = new ArrayList<>();
                for(CovidTest ct:covidTestList){
                    if(ct.getCenterOfficeID().equalsIgnoreCase(centreOfficer.getCentreOfficerId())){
                        covidTests.add(ct);
                    }
                }
                AdaptorCovidTest adapter = new AdaptorCovidTest(covidTests, centreOfficer, getApplicationContext(), patients);
                test_list.setAdapter(adapter);
                test_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        }, "");
    }

}