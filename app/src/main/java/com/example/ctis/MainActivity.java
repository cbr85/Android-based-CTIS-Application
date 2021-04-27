package com.example.ctis;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    EditText userName;
    EditText password;
    Button login;

    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("Patient");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.edit_text_user_name);
        password = findViewById(R.id.edit_text_password);
        login = findViewById(R.id.bottom_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameValue = userName.getText().toString();
                String passwordValue = password.getText().toString();

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
                login(userNameValue, passwordValue);
            }
        });
    }

    public void login(final String userNameValue, final String passwordValue){
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
                        int result = 0;
                        for(QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                            Patient patient = documentSnapshots.toObject(Patient.class);
                            if(patient.getUserName().equals(userNameValue) &&
                                    patient.getPassword().equals(passwordValue)){
                                result = 1;
                                Toast.makeText(getApplicationContext(), "Success login",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent  = new Intent(getApplication(), TestingHistoryActivity.class);
                                intent.putExtra("patient", patient);
                                startActivity(intent);
                                finish();
                            }
                            else if(patient.getUserName().equals(userNameValue) &&
                                    !patient.getPassword().equals(passwordValue)){
                                result = 2;
                            }
                        }
                        if(result == 0){
                            Toast.makeText(getApplicationContext(), "Incorrect username",
                                    Toast.LENGTH_LONG).show();
                        }
                        else if(result == 2){
                            Toast.makeText(getApplicationContext(), "Incorrect password",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}