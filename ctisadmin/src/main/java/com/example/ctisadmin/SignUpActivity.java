package com.example.ctisadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText fullName;
    EditText userName;
    EditText password;
    Button signUp;

    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("CentreOfficer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = findViewById(R.id.edit_text_full_name);
        userName = findViewById(R.id.edit_text_user_name);
        password = findViewById(R.id.edit_text_password);
        signUp = findViewById(R.id.bottom_sign_up);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullNameValue = fullName.getText().toString();
                String userNameValue = userName.getText().toString();
                String passwordValue = password.getText().toString();
                signUp(fullNameValue, userNameValue, passwordValue);
            }
        });
    }

    public void signUp(String fullNameValue, final String userNameValue, String passwordValue) {
        final CentreOfficer centreOfficer = new CentreOfficer("", userNameValue,
                passwordValue, fullNameValue, "manager");

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
                                Toast.makeText(getApplicationContext(), "There a User with that userName",
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
                                                        Intent intent = new Intent(getApplication(), LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }
}