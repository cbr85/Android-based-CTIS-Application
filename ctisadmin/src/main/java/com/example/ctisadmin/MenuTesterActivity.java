package com.example.ctisadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuTesterActivity extends AppCompatActivity {

    Button newPatient;
    Button updateTestResult;
    CentreOfficer centreOfficer = new CentreOfficer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tester);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");

        newPatient = findViewById(R.id.bottom_new_patient);
        updateTestResult = findViewById(R.id.bottom_update_test_result);

        newPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), RecordNewTestActivity.class);
                intent.putExtra("officer", centreOfficer);
                startActivity(intent);
            }
        });

        updateTestResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
  
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Logout from app",
                Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}