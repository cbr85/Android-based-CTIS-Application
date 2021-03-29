package com.example.ctisadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    EditText userName;
    EditText password;
    Button login;
    TextView signUp;

    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("CentreOfficer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = findViewById(R.id.edit_text_user_name);
        password = findViewById(R.id.edit_text_password);
        login = findViewById(R.id.bottom_login);
        signUp = findViewById(R.id.text_view_sign_up);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameValue = userName.getText().toString();
                String passwordValue = password.getText().toString();
                login(userNameValue, passwordValue);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), SignUpActivity.class);
                startActivity(intent);
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
                            CentreOfficer officer = documentSnapshots.toObject(CentreOfficer.class);
                            if(officer.getUserName().equals(userNameValue) &&
                                    officer.getPassword().equals(passwordValue)){
                                result = 1;
                                if(officer.getCentreId() == null || officer.getCentreId().equals("")){
                                    Intent intent = new Intent(LoginActivity.this, RegisterTestCentreActivity.class);
                                    intent.putExtra("officer", officer);
                                    startActivity(intent);
                                    finish();
                                }
                                else{

                                    if(officer.getPosition().equalsIgnoreCase("manager")){
                                        Intent intent = new Intent(getApplication(), MenuActivity.class);
                                        intent.putExtra("officer", officer);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Intent intent  = new Intent(getApplication(), MenuTesterActivity.class);
                                        intent.putExtra("officer", officer);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            }
                        }
                        if(result == 0){
                            Toast.makeText(getApplicationContext(), "There no User with that userName",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}