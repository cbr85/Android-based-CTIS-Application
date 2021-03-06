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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordTesterActivity extends AppCompatActivity {

    EditText fullName;
    EditText userName;
    EditText password;
    Button record;
    CentreOfficer centreOfficerUpper = new CentreOfficer();;
    CentreOfficer centreOfficerTester = new CentreOfficer();;

    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("CentreOfficer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_tester);

        Intent i = getIntent();
        centreOfficerUpper = (CentreOfficer) i.getSerializableExtra("officer");
        centreOfficerTester = (CentreOfficer) i.getSerializableExtra("tester");

        fullName = findViewById(R.id.edit_text_full_name);
        userName = findViewById(R.id.edit_text_user_name);
        password = findViewById(R.id.edit_text_password);
        record = findViewById(R.id.bottom_record_tester);

        if(centreOfficerTester != null){
            fullName.setText(centreOfficerTester.getFullName());
            userName.setText(centreOfficerTester.getUserName());
            password.setText(centreOfficerTester.getPassword());
        }

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullNameValue = fullName.getText().toString();
                String userNameValue = userName.getText().toString();
                String passwordValue = password.getText().toString();

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

                if(passwordValue.length() < 6){
                    password.setError("Password must more then 6 character");
                    password.requestFocus();
                    return;
                }


                if(centreOfficerTester != null){
                    updateTester(fullNameValue, userNameValue, passwordValue, centreOfficerTester);
                }
                else{
                    recordTester(fullNameValue, userNameValue, passwordValue);
                }
            }
        });
    }

    public void recordTester(String fullNameValue, final String userNameValue, String passwordValue) {
        final CentreOfficer centreOfficer = new CentreOfficer("", userNameValue,
                passwordValue, fullNameValue, "tester", centreOfficerUpper.getCentreId());

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
                            CentreOfficer officer = documentSnapshots.toObject(CentreOfficer.class);
                            if(officer.getUserName().equals(userNameValue)){
                                Toast.makeText(getApplicationContext(), "Username already been used",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        collectionReference.add(centreOfficer)
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
                                        update.put("centreOfficerId", documentReference.getId());
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
                                                        Toast.makeText(getApplicationContext(), "New Account has been set for new Tester",
                                                                Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    public void updateTester(final String fullNameValue, final String userNameValue, String passwordValue, CentreOfficer centreOfficerTester) {

        Map<String, Object> update = new HashMap<>();
        update.put("userName", userNameValue);
        update.put("password", passwordValue);
        update.put("fullName", fullNameValue);
        collectionReference.document(centreOfficerTester.getCentreOfficerId()).update(update)
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
                        Toast.makeText(getApplicationContext(), "Tester Information Updated",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}