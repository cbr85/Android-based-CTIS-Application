package com.example.ctis;

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

public class TestingHistoryActivity extends AppCompatActivity {

    RecyclerView test_list;

    Patient patient = new Patient();

    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("CovidTest");
    CollectionReference collectionReferenceKit = FirebaseFirestore.getInstance()
            .collection("TestKit");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_history);

        test_list = findViewById(R.id.test_list);

        Intent i = getIntent();
        patient = (Patient) i.getSerializableExtra("patient");

        getListHistory(new FirebaseCallback() {
            @Override
            public void onCallback(final List<CovidTest> covidTests) {
                getListKit(new FirebaseCallbackKit() {
                    @Override
                    public void onCallback(List<TestKit> testKits) {
                        AdaptorCovidTest adapter = new AdaptorCovidTest(covidTests, getApplicationContext(),testKits);
                        test_list.setAdapter(adapter);
                        test_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    }
                });
            }
        }, patient.getPatientId());

    }

    public interface FirebaseCallback{
        void onCallback(List<CovidTest> covidTests);
    }

    public interface FirebaseCallbackKit{
        void onCallback(List<TestKit> testKits);
    }

    public void getListHistory(final FirebaseCallback firebaseCallback, final String patientId){
        collectionReference.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<CovidTest> covidTests = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            CovidTest covidTest = document.toObject(CovidTest.class);
                            if(covidTest != null){
                                if (covidTest.getPatientID().equals(patientId)) {
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

    public void getListKit(final FirebaseCallbackKit firebaseCallback){
        collectionReferenceKit.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<TestKit> testKits = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            TestKit testKit = document.toObject(TestKit.class);
                            testKits.add(testKit);
                        }
                        firebaseCallback.onCallback(testKits);
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