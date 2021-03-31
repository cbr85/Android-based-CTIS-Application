package com.example.ctisadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RecordNewTestActivity extends AppCompatActivity {

    EditText fullName;
    EditText userName;
    EditText password;
    EditText symptoms;
    Button record;
    Spinner patientType;
    CentreOfficer centreOfficer = new CentreOfficer();

    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("Patient");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new_test);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");

        fullName = findViewById(R.id.edit_text_full_name);
        userName = findViewById(R.id.edit_text_user_name);
        password = findViewById(R.id.edit_text_password);
        patientType = findViewById(R.id.spinner_patient_type);
        symptoms = findViewById(R.id.edit_text_symptoms);
        record = findViewById(R.id.bottom_record_patient);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullNameValue = fullName.getText().toString();
                String userNameValue = userName.getText().toString();
                String passwordValue = password.getText().toString();
                String patientTypeValue = patientType.getSelectedItem().toString();
                String symptomsValue = symptoms.getText().toString();
                recordPatient(fullNameValue, userNameValue, passwordValue, patientTypeValue, symptomsValue);
            }
        });
    }

    public void recordPatient(String fullNameValue, final String userNameValue, String passwordValue,
                              String patientTypeValue, String symptomsValue){
        final Patient patient = new Patient("", userNameValue,
                passwordValue, fullNameValue, patientTypeValue, symptomsValue, centreOfficer.getCentreId());
        collectionReference.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                            Patient patient1 = documentSnapshots.toObject(Patient.class);
                            if(patient1.getUserName().equals(userNameValue) && patient1.getCentreId().equals(centreOfficer.getCentreId())){
                                Toast.makeText(getApplicationContext(), "UserName already exist, Update Information",
                                        Toast.LENGTH_LONG).show();
                                Map<String, Object> update = new HashMap<>();
                                update.put("symptoms", patient.getSymptoms());
                                update.put("patientType", patient.getPatientType());
                                collectionReference.document(patient1.getPatientId()).update(update)
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
                                                Toast.makeText(getApplicationContext(), "The Test has been Updated",
                                                        Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        });
                                return;
                            }
                        }
                        collectionReference.add(patient)
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Map<String, Object> update = new HashMap<>();
                                        update.put("patientId", documentReference.getId());
                                        collectionReference.document(documentReference.getId()).update(update)
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
                                                        Toast.makeText(getApplicationContext(), "New Test has been recorded",
                                                                Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }
}