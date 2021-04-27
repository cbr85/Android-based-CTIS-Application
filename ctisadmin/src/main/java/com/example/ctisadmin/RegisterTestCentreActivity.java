package com.example.ctisadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterTestCentreActivity extends AppCompatActivity {

    EditText centreName;
    Button register;
    RecyclerView test_center_list;
    CentreOfficer centreOfficer = new CentreOfficer();
    List<TestCentre> testCentre = new ArrayList<>();

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
        test_center_list = findViewById(R.id.test_center_list);

        getListTester(new FirebaseCallback() {
            @Override
            public void onCallback(List<TestCentre> testCentres) {
                testCentre = testCentres;
                AdaptorTestCenter adapter = new AdaptorTestCenter(testCentres, centreOfficer, getApplicationContext());
                test_center_list.setAdapter(adapter);
                test_center_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String centreNameValue = centreName.getText().toString();

                if(centreNameValue.isEmpty() || centreNameValue.equalsIgnoreCase("")){
                    centreName.setError("Cannot be empty");
                    centreName.requestFocus();
                    return;
                }

                int find = 0;
                for(TestCentre tc:testCentre){
                    if(centreNameValue.equalsIgnoreCase(tc.getCentreName())){
                        find = 1;
                    }
                }
                if(find == 1){
                    Toast.makeText(getApplicationContext(), "Test Centre Already exist",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
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


    public interface FirebaseCallback{
        void onCallback(List<TestCentre> testCentres);
    }

    public void getListTester(final FirebaseCallback firebaseCallback){
        collectionReference.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<TestCentre> testCentres = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            TestCentre testCentre = document.toObject(TestCentre.class);
                            testCentres.add(testCentre);
                        }
                        firebaseCallback.onCallback(testCentres);
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