package com.example.ctisadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class UpdateResultActivity extends AppCompatActivity {

    EditText update_test_result;
    Button bottom_update_test_result;
    CovidTest covidTest = new CovidTest();

    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("CovidTest");
    CollectionReference collectionReferenceKit = FirebaseFirestore.getInstance()
            .collection("TestKit");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_result);

        update_test_result = findViewById(R.id.update_test_result);
        bottom_update_test_result = findViewById(R.id.bottom_update_test_result);

        Intent i = getIntent();
        covidTest = (CovidTest) i.getSerializableExtra("covidTest");

        bottom_update_test_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resultVal = update_test_result.getText().toString();

                if(resultVal.isEmpty() || resultVal.equalsIgnoreCase("")){
                    update_test_result.setError("Cannot be empty");
                    update_test_result.requestFocus();
                    return;
                }
                updateResult(resultVal);
            }
        });
    }

    public void updateResult(String fullNameValue){

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = String.valueOf(timestamp.getTime());

        Map<String, Object> update = new HashMap<>();
        update.put("result", fullNameValue);
        update.put("resultDate", time);
        update.put("status", "complete");
        collectionReference.document(covidTest.getTestID()).update(update)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        collectionReferenceKit.document(covidTest.getTestKitID()).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        TestKit testKit = documentSnapshot.toObject(TestKit.class);
                                        int currentStock = testKit.getAvailableStock() - 1;
                                        Map<String, Object> update = new HashMap<>();
                                        update.put("availableStock", currentStock);
                                        collectionReferenceKit.document(covidTest.getTestKitID()).update(update)
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                })
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "Covid Test has been Updated",
                                                                Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });
    }
}