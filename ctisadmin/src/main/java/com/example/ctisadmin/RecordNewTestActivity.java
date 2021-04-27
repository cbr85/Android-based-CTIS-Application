package com.example.ctisadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordNewTestActivity extends AppCompatActivity {

    EditText fullName;
    EditText userName;
    EditText password;
    EditText symptoms;
    Button record;
    Spinner patientType;
    Spinner patientTestKid;
    CentreOfficer centreOfficer = new CentreOfficer();
    List<String> spinnerArray =  new ArrayList<String>();
    List<TestKit> spinnerTestKit =  new ArrayList<>();
    Patient patient = new Patient();

    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("Patient");
    CollectionReference collectionReferenceTest = FirebaseFirestore.getInstance()
            .collection("CovidTest");
    CollectionReference collectionReferenceTestKit = FirebaseFirestore.getInstance()
            .collection("TestKit");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_new_test);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");
        patient = (Patient) i.getSerializableExtra("patient");

        fullName = findViewById(R.id.edit_text_full_name);
        userName = findViewById(R.id.edit_text_user_name);
        password = findViewById(R.id.edit_text_password);
        patientType = findViewById(R.id.spinner_patient_type);
        symptoms = findViewById(R.id.edit_text_symptoms);
        record = findViewById(R.id.bottom_record_patient);
        patientTestKid = findViewById(R.id.spinner_testKitId);
        setSpinnerTestKit();

        if(patient != null){
            fullName.setText(patient.getFullName());
            userName.setText(patient.getUserName());
            password.setText(patient.getPassword());
            String[] type = {"returnee", "quarantined", "close contact", "infected", "suspected"};
            for(int val = 0; val < type.length; val++){
                if(type[val].equals(patient.getPatientType())){
                    patientType.setSelection(val);
                }
            }
            symptoms.setText(patient.getSymptoms());
        }

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullNameValue = fullName.getText().toString();
                String userNameValue = userName.getText().toString();
                String passwordValue = password.getText().toString();
                String patientTypeValue = patientType.getSelectedItem().toString();
                String symptomsValue = symptoms.getText().toString();
                String testKidIdSpinnerValue = patientTestKid.getSelectedItem().toString();
                String testKidIdValue = "";
                for(TestKit tk:spinnerTestKit){
                    if(tk.getTestName().equals(testKidIdSpinnerValue)){
                        testKidIdValue = tk.getKidID();
                    }
                }

                if(fullNameValue.isEmpty() || fullNameValue.equalsIgnoreCase("")){
                    fullName.setError("Cannot be empty");
                    fullName.requestFocus();
                    return;
                }

                if(userNameValue.isEmpty() || userNameValue.equalsIgnoreCase("")){
                    userName.setError("Cannot be empty");
                    userName.requestFocus();
                    return;
                }

                if(passwordValue.isEmpty() || passwordValue.equalsIgnoreCase("")){
                    password.setError("Cannot be empty");
                    password.requestFocus();
                    return;
                }

                if(symptomsValue.isEmpty() || symptomsValue.equalsIgnoreCase("")){
                    symptoms.setError("Cannot be empty");
                    symptoms.requestFocus();
                    return;
                }
                recordPatient(fullNameValue, userNameValue, passwordValue, patientTypeValue, symptomsValue, testKidIdValue);
            }
        });
    }

    public void recordPatient(String fullNameValue, final String userNameValue, String passwordValue,
                              String patientTypeValue, String symptomsValue, final String testKidIdValue){
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
                    public void onSuccess(final QuerySnapshot queryDocumentSnapshots) {
                        int find = 0;
                        for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                            final Patient patient1 = documentSnapshots.toObject(Patient.class);
                            if(patient1.getUserName().equals(userNameValue) && patient1.getCentreId().equals(centreOfficer.getCentreId())){
                                updatePatient(patient, patient1);
                                collectionReferenceTestKit.get()
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
                                                boolean check = false;
                                                for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                                                    TestKit testKit = documentSnapshots.toObject(TestKit.class);
                                                    if(testKit.getKidID().equals(testKidIdValue)){
                                                        check = true;
                                                    }
                                                }
                                                if(!check){
                                                    Toast.makeText(getApplicationContext(), "Wrong Test Kid ID",
                                                            Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                else{
                                                    recordCovidTest(patient1.getPatientId(), centreOfficer, testKidIdValue);
                                                }
                                            }
                                        });
                                return;
                            }
                            if(patient1.getUserName().equals(userNameValue)){
                                find = 1;
                            }
                        }
                        if(find == 1){
                            Toast.makeText(getApplicationContext(), "Same username already apply, Please use other username",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        collectionReferenceTestKit.get()
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
                                        boolean check = false;
                                        for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                                            TestKit testKit = documentSnapshots.toObject(TestKit.class);
                                            if(testKit.getKidID().equals(testKidIdValue)){
                                                check = true;
                                            }
                                        }
                                        if(!check){
                                            Toast.makeText(getApplicationContext(), "Wrong Test Kid ID",
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        else{
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
                                                            recordCovidTest(documentReference.getId(), centreOfficer, testKidIdValue);
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
                                    }
                                });
                    }
                });
    }

    public void updatePatient(Patient patient, Patient patient1) {
        Toast.makeText(getApplicationContext(), "Patient already exist, Update Information",
                Toast.LENGTH_SHORT).show();
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
    }


    public void recordCovidTest(String patientId, CentreOfficer officer, String testKitId) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String time = String.valueOf(timestamp.getTime());
        CovidTest covidTest = new CovidTest("", time, "", "", "pending", patientId, officer.getCentreOfficerId(), testKitId);

        collectionReferenceTest.add(covidTest)
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
                        update.put("testID", documentReference.getId());
                        collectionReferenceTest.document(documentReference.getId()).update(update)
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
                                        Toast.makeText(getApplicationContext(), "New Test Covid has been recorded",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    public void setSpinnerTestKit(){
        collectionReferenceTestKit.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        spinnerArray.add("select");
                        for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                            TestKit testKit = documentSnapshots.toObject(TestKit.class);
                            if(testKit.getCentreId().equals(centreOfficer.getCentreId())){
                                spinnerArray.add(testKit.getTestName());
                                spinnerTestKit.add(testKit);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        patientTestKid.setAdapter(adapter);
                        patientType.setSelection(0);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to setup TestKit",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}