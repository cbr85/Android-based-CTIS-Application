package com.example.ctisadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;

public class MenuTesterActivity extends AppCompatActivity {

    Button newPatient;
    Button updateTestResult;
    Button generateReport;
    Button logout;
    CentreOfficer centreOfficer = new CentreOfficer();
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tester);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");

        newPatient = findViewById(R.id.bottom_new_patient);
        updateTestResult = findViewById(R.id.bottom_update_test_result);
        generateReport = findViewById(R.id.bottom_generate_test_report);
        logout = findViewById(R.id.bottom_logout);

        newPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ListPatientActivity.class);
                intent.putExtra("officer", centreOfficer);
                startActivity(intent);
            }
        });

        updateTestResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ResultListActivity.class);
                intent.putExtra("officer", centreOfficer);
                startActivity(intent);
            }
        });

        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), TestReportActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                intent.putExtra("type", 2);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}