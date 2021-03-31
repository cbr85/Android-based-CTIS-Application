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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RegisterTestCentreActivity extends AppCompatActivity {

    EditText centreName;
    Button register;
    CentreOfficer centreOfficer = new CentreOfficer();

    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("TestCentre");
    CollectionReference collectionOfficerReference = FirebaseFirestore.getInstance()
            .collection("CentreOfficer");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_test_centre);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");

        centreName = findViewById(R.id.edit_text_test_centre_name);
        register = findViewById(R.id.bottom_register_test_center);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String centreNameValue = centreName.getText().toString();
                registerTestCentre(centreNameValue);
            }
        });
    }

    public void registerTestCentre(String centreNameValue){
        TestCentre testCentre = new TestCentre("", centreNameValue);
        collectionReference.add(testCentre)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(final DocumentReference documentReference) {
                        Map<String, Object> update = new HashMap<>();
                        update.put("centreId", documentReference.getId());
                        centreOfficer.setCentreId(documentReference.getId());
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
                                        Map<String, Object> updateOfficer = new HashMap<>();
                                        updateOfficer.put("centreId", centreOfficer.getCentreId());

                                        collectionOfficerReference.document(centreOfficer.getCentreOfficerId()).update(updateOfficer)
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
                                                        Toast.makeText(getApplicationContext(), "Register new Test Centre",
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(getApplication(), MenuActivity.class);
                                                        intent.putExtra("officer", (Serializable) centreOfficer);
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