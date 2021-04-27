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

import java.util.HashMap;
import java.util.Map;

public class ManageTestKitStockActivity extends AppCompatActivity {


    CentreOfficer centreOfficerUpper = new CentreOfficer();;
    TestKit testKit = new TestKit();;
    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("TestKit");

    EditText test_kit_name;
    EditText test_kit_stock;
    Button test_kit_record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_test_kit_stock);

        Intent i = getIntent();
        centreOfficerUpper = (CentreOfficer) i.getSerializableExtra("officer");
        testKit = (TestKit) i.getSerializableExtra("testKit");

        test_kit_name = findViewById(R.id.test_kit_name);
        test_kit_stock = findViewById(R.id.test_kit_stock);
        test_kit_record = findViewById(R.id.test_kit_record);

        if(testKit != null){
            test_kit_name.setText(testKit.getTestName());
            test_kit_stock.setText(String.valueOf(testKit.getAvailableStock()));
        }

        test_kit_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String testKitName = test_kit_name.getText().toString();
                int testKitStock;
                try{
                    testKitStock = Integer.parseInt(test_kit_stock.getText().toString());
                }
                catch (Exception e){
                    testKitStock = 0;
                }

                if(testKitName.isEmpty() || testKitName.equalsIgnoreCase("")){
                    test_kit_name.setError("Cannot be empty");
                    test_kit_name.requestFocus();
                    return;
                }

                if(testKitStock == 0){
                    test_kit_stock.setError("Stock cannot set to 0 or empty");
                    test_kit_stock.requestFocus();
                    return;
                }

                if(testKit != null){
                    updateTestKit(testKitName, testKitStock, testKit);
                }else{
                    recordTestKit(testKitName, testKitStock);
                }

            }
        });

    }

    public void recordTestKit(final String testKitName, final int testKitStock) {
        final TestKit testKit = new TestKit("", testKitName, testKitStock, centreOfficerUpper.getCentreId());

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
                            TestKit testKit1 = documentSnapshots.toObject(TestKit.class);
                            if(testKit1.getTestName().equals(testKitName)){
                                Toast.makeText(getApplicationContext(), "Test Kit Name already been used",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                        collectionReference.add(testKit)
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
                                        update.put("kidID", documentReference.getId());
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
                                                        Toast.makeText(getApplicationContext(), "New Account has been set for new Test Kit",
                                                                Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    public void updateTestKit(final String testKitName, final int testKitStock, TestKit testKit) {

        Map<String, Object> update = new HashMap<>();
        update.put("testName", testKitName);
        update.put("availableStock", testKitStock);
        collectionReference.document(testKit.getKidID()).update(update)
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
                        Toast.makeText(getApplicationContext(), "Test Kit Updated",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}