package com.example.ctisadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;

public class MenuActivity extends AppCompatActivity {

    Button recordTester;
    Button manageKit;
    Button generateReport;
    Button logout;
    CentreOfficer centreOfficer = new CentreOfficer();
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");

        recordTester = findViewById(R.id.bottom_record_tester);
        manageKit = findViewById(R.id.bottom_manage_test_kit_stock);
        generateReport = findViewById(R.id.bottom_generate_test_report);
        logout = findViewById(R.id.bottom_logout);

        recordTester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ListTesterActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                startActivity(intent);
            }
        });

        manageKit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ListTestKitActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                startActivity(intent);
            }
        });

        generateReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), TestReportActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                intent.putExtra("type", 1);
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