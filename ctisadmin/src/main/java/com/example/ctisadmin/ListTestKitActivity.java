package com.example.ctisadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListTestKitActivity extends AppCompatActivity {

    RecyclerView tester_list;
    Button add_tester;
    CentreOfficer centreOfficer = new CentreOfficer();
    CollectionReference collectionReference = FirebaseFirestore.getInstance()
            .collection("TestKit");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_test_kit);

        tester_list = findViewById(R.id.tester_list);
        add_tester = findViewById(R.id.add_tester);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");

        getListTester(new FirebaseCallback() {
                          @Override
                          public void onCallback(List<TestKit> testKitList) {
                              AdaptorTestKit adapter = new AdaptorTestKit(testKitList, centreOfficer, getApplicationContext());
                              tester_list.setAdapter(adapter);
                              tester_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                          }
                      });

        add_tester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ManageTestKitStockActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                intent.putExtra("testKit", (Serializable) null);
                startActivity(intent);
            }
        });

    }

    public interface FirebaseCallback{
        void onCallback(List<TestKit> testerList);
    }

    public void getListTester(final FirebaseCallback firebaseCallback){
        collectionReference.get().addOnSuccessListener(
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<TestKit> testKitList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            TestKit testKit = document.toObject(TestKit.class);
                            if(testKit != null){
                                if (testKit.getCentreId().equals(centreOfficer.getCentreId())) {
                                    testKitList.add(testKit);
                                }
                            }
                        }
                        firebaseCallback.onCallback(testKitList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Failed to collect Location, Check database: ");
                    }
                });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getListTester(new FirebaseCallback() {
            @Override
            public void onCallback(List<TestKit> testKitList) {
                AdaptorTestKit adapter = new AdaptorTestKit(testKitList, centreOfficer, getApplicationContext());
                tester_list.setAdapter(adapter);
                tester_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }
        });
    }
}