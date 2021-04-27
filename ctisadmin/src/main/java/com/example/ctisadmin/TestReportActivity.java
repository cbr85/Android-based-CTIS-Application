package com.example.ctisadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TestReportActivity extends AppCompatActivity {

    RecyclerView report_list;
    int typeReport;
    CentreOfficer centreOfficer = new CentreOfficer();
    CollectionReference collectionReferenceTester = FirebaseFirestore.getInstance()
            .collection("CentreOfficer");
    CollectionReference collectionReferenceCovidTest = FirebaseFirestore.getInstance()
            .collection("CovidTest");
    CollectionReference collectionReferencePatient = FirebaseFirestore.getInstance()
            .collection("Patient");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_report);

        report_list = findViewById(R.id.report_list);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");
        typeReport = i.getIntExtra("type", 1);

        getListTester(new FirebaseCallbackTester() {
            @Override
            public void onCallback(final List<CentreOfficer> testerList) {
                if(typeReport == 1){
                    getListCovidTest(new FirebaseCallbackCovidTest() {
                        @Override
                        public void onCallback(final List<CovidTest> covidTestList) {
                            getListPatient(new FirebaseCallbackPatient() {
                                @Override
                                public void onCallback(List<Patient> patients) {
                                    AdaptorTestReport adapter = new AdaptorTestReport(covidTestList,
                                            centreOfficer, getApplicationContext(),patients,testerList);
                                    report_list.setAdapter(adapter);
                                    report_list.setLayoutManager(
                                            new LinearLayoutManager(getApplicationContext()));
                                }
                            });
                        }
                    }, testerList);
                }
                else{
                    List<CentreOfficer> tempTester = new ArrayList<>();
                    for(CentreOfficer co : testerList){
                        if(co.getCentreOfficerId().equals(centreOfficer.getCentreOfficerId())){
                            tempTester.add(co);
                        }
                    }

                    final List<CentreOfficer> parseTester = tempTester;
                    getListCovidTest(new FirebaseCallbackCovidTest() {
                        @Override
                        public void onCallback(final List<CovidTest> covidTestList) {
                            getListPatient(new FirebaseCallbackPatient() {
                                @Override
                                public void onCallback(List<Patient> patients) {
                                    AdaptorTestReport adapter = new AdaptorTestReport(covidTestList,
                                            centreOfficer, getApplicationContext(),patients,parseTester);
                                    report_list.setAdapter(adapter);
                                    report_list.setLayoutManager(
                                            new LinearLayoutManager(getApplicationContext()));
                                }
                            });
                        }
                    }, tempTester);
                }


            }
        });

    }

    public interface FirebaseCallbackCovidTest{
        void onCallback(List<CovidTest> covidTestList);
    }

    public interface FirebaseCallbackTester{
        void onCallback(List<CentreOfficer> testerList);
    }

    public interface FirebaseCallbackPatient{
        void onCallback(List<Patient> testerList);
    }

    public void getListTester(final FirebaseCallbackTester firebaseCallback){
        collectionReferenceTester.get().addOnSuccessListener(
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

    public void getListCovidTest(final FirebaseCallbackCovidTest firebaseCallback, final List<CentreOfficer> centreOfficers){
        collectionReferenceCovidTest.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CovidTest> covidTests = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            CovidTest covidTest = document.toObject(CovidTest.class);
                            for(CentreOfficer co :centreOfficers){
                                if(covidTest.getCenterOfficeID().equals(co.getCentreOfficerId())){
                                    covidTests.add(covidTest);
                                }
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

    public void getListPatient(final FirebaseCallbackPatient firebaseCallback){
        collectionReferencePatient.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Patient> patients = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Patient patient = document.toObject(Patient.class);
                            patients.add(patient);

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
}